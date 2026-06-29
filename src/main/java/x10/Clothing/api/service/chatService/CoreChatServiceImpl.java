package x10.Clothing.api.service.chatService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.chat.ConversationEntity;
import x10.Clothing.api.common.domain.entities.chat.MessageEntity;
import x10.Clothing.api.common.domain.entities.user.UserEntity;
import x10.Clothing.api.common.domain.enums.ConversationStatus;
import x10.Clothing.api.common.domain.enums.SenderType;
import x10.Clothing.api.infrastructure.chat.adapter.ConversationMapper;
import x10.Clothing.api.infrastructure.chat.adapter.MessageMapper;
import x10.Clothing.api.infrastructure.chat.db.mongodb.ConversationDocument;
import x10.Clothing.api.infrastructure.chat.db.mongodb.ConversationMongoRepository;
import x10.Clothing.api.infrastructure.chat.db.mongodb.MessageMongoRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreChatServiceImpl implements ICoreChatService {

    private static final String SUPPORT_TOPIC = "/topic/chat/support";
    private static final String CONVERSATION_TOPIC_PREFIX = "/topic/chat/";

    private final ConversationMongoRepository conversationRepository;
    private final MessageMongoRepository messageRepository;
    private final IUserRepository userRepository;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public ChatConversationResponse getOrCreateMyConversation(String customerId) {
        ConversationDocument conversation = conversationRepository.findByCustomerId(customerId)
                .orElseGet(() -> createConversation(customerId));

        return toConversationResponse(conversationMapper.toEntity(conversation));
    }

    @Override
    public List<ChatConversationResponse> getSupportConversations() {
        return conversationRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(conversationMapper::toEntity)
                .map(this::toConversationResponse)
                .toList();
    }

    @Override
    public ChatConversationResponse acceptConversation(String conversationId, String supportUserId) {
        ConversationDocument conversation = getConversationDocument(conversationId);
        conversation.setAdminId(supportUserId);
        conversation.setStatus(ConversationStatus.ADMIN_HANDLING);
        conversation.setUpdatedAt(Instant.now());

        ConversationEntity saved = conversationMapper.toEntity(conversationRepository.save(conversation));
        ChatConversationResponse response = toConversationResponse(saved);
        publishConversationEvent("CONVERSATION_ACCEPTED", response, null);
        return response;
    }

    @Override
    public ChatConversationResponse closeConversation(String conversationId, String supportUserId) {
        ConversationDocument conversation = getConversationDocument(conversationId);
        assignSupportIfEmpty(conversation, supportUserId);

        conversation.setStatus(ConversationStatus.CLOSED);
        conversation.setUpdatedAt(Instant.now());

        ConversationEntity saved = conversationMapper.toEntity(conversationRepository.save(conversation));
        ChatConversationResponse response = toConversationResponse(saved);
        publishConversationEvent("CONVERSATION_CLOSED", response, null);
        return response;
    }

    @Override
    public List<ChatMessageResponse> getMessages(String conversationId, String currentUserId, boolean supportUser) {
        ConversationDocument conversation = getConversationDocument(conversationId);
        if (!supportUser && !currentUserId.equals(conversation.getCustomerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Khong co quyen xem hoi thoai nay");
        }

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId).stream()
                .map(messageMapper::toEntity)
                .map(this::toMessageResponse)
                .toList();
    }

    @Override
    public ChatMessageResponse sendCustomerMessage(
            String conversationId,
            String customerId,
            SendChatMessageRequest request
    ) {
        ConversationDocument conversation = getConversationDocument(conversationId);
        if (!customerId.equals(conversation.getCustomerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Khong co quyen gui tin vao hoi thoai nay");
        }

        if (ConversationStatus.CLOSED.equals(conversation.getStatus())) {
            conversation.setStatus(ConversationStatus.WAITING_ADMIN);
        }

        validateMessageRequest(request);
        return saveAndPublishMessage(conversation, customerId, SenderType.USER, request, true);
    }

    @Override
    public ChatMessageResponse sendSupportMessage(
            String conversationId,
            String supportUserId,
            SenderType senderType,
            SendChatMessageRequest request
    ) {
        ConversationDocument conversation = getConversationDocument(conversationId);
        assignSupportIfEmpty(conversation, supportUserId);

        conversation.setStatus(ConversationStatus.ADMIN_HANDLING);
        conversation.setUnreadCount(0);
        validateMessageRequest(request);
        return saveAndPublishMessage(conversation, supportUserId, senderType, request, false);
    }

    private ConversationDocument createConversation(String customerId) {
        Instant now = Instant.now();
        ConversationDocument conversation = ConversationDocument.builder()
                .customerId(customerId)
                .status(ConversationStatus.WAITING_ADMIN)
                .unreadCount(0)
                .createdAt(now)
                .updatedAt(now)
                .build();

        ConversationDocument saved = conversationRepository.save(conversation);
        publishConversationEvent(
                "CONVERSATION_CREATED",
                toConversationResponse(conversationMapper.toEntity(saved)),
                null
        );
        return saved;
    }

    private ConversationDocument getConversationDocument(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay hoi thoai"));
    }

    private void assignSupportIfEmpty(ConversationDocument conversation, String supportUserId) {
        if (!StringUtils.hasText(conversation.getAdminId())) {
            conversation.setAdminId(supportUserId);
        }
    }

    private ChatMessageResponse saveAndPublishMessage(
            ConversationDocument conversation,
            String senderId,
            SenderType senderType,
            SendChatMessageRequest request,
            boolean incrementUnread
    ) {
        Instant now = Instant.now();
        String content = normalize(request.getContent());
        String imageUrl = normalize(request.getImageUrl());
        String imagePublicId = normalize(request.getImagePublicId());

        MessageEntity message = MessageEntity.builder()
                .conversationId(conversation.getId())
                .senderId(senderId)
                .senderType(senderType)
                .content(content)
                .imageUrl(imageUrl)
                .imagePublicId(imagePublicId)
                .seen(false)
                .createdAt(now)
                .build();

        MessageEntity savedMessage = messageMapper.toEntity(
                messageRepository.save(messageMapper.toDocument(message))
        );

        conversation.setLastMessage(resolveLastMessage(savedMessage));
        conversation.setLastSender(senderType);
        conversation.setLastMessageTime(now);
        conversation.setUpdatedAt(now);
        if (incrementUnread) {
            conversation.setUnreadCount((conversation.getUnreadCount() == null ? 0 : conversation.getUnreadCount()) + 1);
        }

        ConversationEntity savedConversation = conversationMapper.toEntity(conversationRepository.save(conversation));
        ChatConversationResponse conversationResponse = toConversationResponse(savedConversation);
        ChatMessageResponse messageResponse = toMessageResponse(savedMessage);

        publishConversationEvent("MESSAGE_CREATED", conversationResponse, messageResponse);
        return messageResponse;
    }

    private void publishConversationEvent(
            String type,
            ChatConversationResponse conversation,
            ChatMessageResponse message
    ) {
        ChatSocketEvent event = ChatSocketEvent.builder()
                .type(type)
                .conversation(conversation)
                .message(message)
                .build();

        if (conversation != null && conversation.getId() != null) {
            messagingTemplate.convertAndSend(CONVERSATION_TOPIC_PREFIX + conversation.getId(), event);
        }
        messagingTemplate.convertAndSend(SUPPORT_TOPIC, event);
    }

    private ChatConversationResponse toConversationResponse(ConversationEntity conversation) {
        return ChatConversationResponse.builder()
                .id(conversation.getId())
                .customerId(conversation.getCustomerId())
                .customerName(resolveUserName(conversation.getCustomerId()))
                .customerAvatarUrl(resolveUserAvatar(conversation.getCustomerId()))
                .adminId(conversation.getAdminId())
                .adminName(resolveUserName(conversation.getAdminId()))
                .adminAvatarUrl(resolveUserAvatar(conversation.getAdminId()))
                .lastMessage(conversation.getLastMessage())
                .lastSender(conversation.getLastSender())
                .lastMessageTime(conversation.getLastMessageTime())
                .unreadCount(conversation.getUnreadCount())
                .status(conversation.getStatus())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .build();
    }

    private ChatMessageResponse toMessageResponse(MessageEntity message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .senderName(resolveUserName(message.getSenderId()))
                .senderAvatarUrl(resolveUserAvatar(message.getSenderId()))
                .senderType(message.getSenderType())
                .content(message.getContent())
                .imageUrl(message.getImageUrl())
                .imagePublicId(message.getImagePublicId())
                .seen(message.getSeen())
                .createdAt(message.getCreatedAt())
                .build();
    }

    private void validateMessageRequest(SendChatMessageRequest request) {
        if (request == null
                || (!StringUtils.hasText(request.getContent()) && !StringUtils.hasText(request.getImageUrl()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tin nhan can co noi dung hoac anh");
        }
    }

    private String normalize(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String resolveLastMessage(MessageEntity message) {
        if (StringUtils.hasText(message.getContent())) {
            return message.getContent();
        }

        if (StringUtils.hasText(message.getImageUrl())) {
            return "[Anh]";
        }

        return "";
    }

    private String resolveUserName(String userId) {
        if (!StringUtils.hasText(userId)) {
            return null;
        }

        return userRepository.findById(userId)
                .map(this::displayName)
                .orElse(userId);
    }

    private String resolveUserAvatar(String userId) {
        if (!StringUtils.hasText(userId)) {
            return null;
        }

        return userRepository.findById(userId)
                .map(UserEntity::getAvatarUrl)
                .orElse(null);
    }

    private String displayName(UserEntity user) {
        if (StringUtils.hasText(user.getUsername())) {
            return user.getUsername();
        }

        if (StringUtils.hasText(user.getEmail())) {
            return user.getEmail();
        }

        return user.getId();
    }
}
