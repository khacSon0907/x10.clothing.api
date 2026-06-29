package x10.Clothing.api.service.chatService;

import java.util.List;
import x10.Clothing.api.common.domain.enums.SenderType;

public interface ICoreChatService {

    ChatConversationResponse getOrCreateMyConversation(String customerId);

    List<ChatConversationResponse> getSupportConversations();

    ChatConversationResponse acceptConversation(String conversationId, String supportUserId);

    ChatConversationResponse closeConversation(String conversationId, String supportUserId);

    List<ChatMessageResponse> getMessages(String conversationId, String currentUserId, boolean supportUser);

    ChatMessageResponse sendCustomerMessage(String conversationId, String customerId, SendChatMessageRequest request);

    ChatMessageResponse sendSupportMessage(
            String conversationId,
            String supportUserId,
            SenderType senderType,
            SendChatMessageRequest request
    );
}
