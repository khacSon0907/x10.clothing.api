package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import x10.Clothing.api.common.domain.enums.SenderType;
import x10.Clothing.api.service.chatService.ChatConversationResponse;
import x10.Clothing.api.service.chatService.ChatMessageResponse;
import x10.Clothing.api.service.chatService.ICoreChatService;
import x10.Clothing.api.service.chatService.SendChatMessageRequest;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ICoreChatService chatService;

    @GetMapping("/conversations/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ChatConversationResponse> getOrCreateMyConversation(HttpServletRequest request) {
        ChatConversationResponse response = chatService.getOrCreateMyConversation(getCurrentUserId());
        return ApiResponse.success(
                200,
                "CHAT.GET_MY_CONVERSATION_SUCCESS",
                "Lay hoi thoai cua khach hang thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/conversations/{conversationId}/messages")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ChatMessageResponse>> getMyMessages(
            @PathVariable("conversationId") String conversationId,
            HttpServletRequest request
    ) {
        List<ChatMessageResponse> response = chatService.getMessages(conversationId, getCurrentUserId(), false);
        return ApiResponse.success(
                200,
                "CHAT.GET_MESSAGES_SUCCESS",
                "Lay tin nhan thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/conversations/{conversationId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ChatMessageResponse> sendCustomerMessage(
            @PathVariable("conversationId") String conversationId,
            @Valid @RequestBody SendChatMessageRequest chatRequest,
            HttpServletRequest request
    ) {
        ChatMessageResponse response = chatService.sendCustomerMessage(
                conversationId,
                getCurrentUserId(),
                chatRequest
        );
        return ApiResponse.success(
                201,
                "CHAT.SEND_MESSAGE_SUCCESS",
                "Gui tin nhan thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/support/conversations")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ChatConversationResponse>> getSupportConversations(HttpServletRequest request) {
        List<ChatConversationResponse> response = chatService.getSupportConversations();
        return ApiResponse.success(
                200,
                "CHAT.SUPPORT_GET_CONVERSATIONS_SUCCESS",
                "Lay danh sach hoi thoai ho tro thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/support/conversations/{conversationId}/accept")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ChatConversationResponse> acceptConversation(
            @PathVariable("conversationId") String conversationId,
            HttpServletRequest request
    ) {
        ChatConversationResponse response = chatService.acceptConversation(conversationId, getCurrentUserId());
        return ApiResponse.success(
                200,
                "CHAT.SUPPORT_ACCEPT_SUCCESS",
                "Nhan xu ly hoi thoai thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/support/conversations/{conversationId}/close")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ChatConversationResponse> closeConversation(
            @PathVariable("conversationId") String conversationId,
            HttpServletRequest request
    ) {
        ChatConversationResponse response = chatService.closeConversation(conversationId, getCurrentUserId());
        return ApiResponse.success(
                200,
                "CHAT.SUPPORT_CLOSE_SUCCESS",
                "Dong hoi thoai thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/support/conversations/{conversationId}/messages")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ChatMessageResponse>> getSupportMessages(
            @PathVariable("conversationId") String conversationId,
            HttpServletRequest request
    ) {
        List<ChatMessageResponse> response = chatService.getMessages(conversationId, getCurrentUserId(), true);
        return ApiResponse.success(
                200,
                "CHAT.SUPPORT_GET_MESSAGES_SUCCESS",
                "Lay tin nhan ho tro thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    @PostMapping("/support/conversations/{conversationId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ChatMessageResponse> sendSupportMessage(
            @PathVariable("conversationId") String conversationId,
            @Valid @RequestBody SendChatMessageRequest chatRequest,
            HttpServletRequest request
    ) {
        ChatMessageResponse response = chatService.sendSupportMessage(
                conversationId,
                getCurrentUserId(),
                getSupportSenderType(),
                chatRequest
        );
        return ApiResponse.success(
                201,
                "CHAT.SUPPORT_SEND_MESSAGE_SUCCESS",
                "Gui tin nhan ho tro thanh cong",
                response,
                request.getRequestURI(),
                null
        );
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        return authentication.getPrincipal().toString();
    }

    private SenderType getSupportSenderType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            boolean admin = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("ROLE_ADMIN"::equals);
            if (admin) {
                return SenderType.ADMIN;
            }
        }

        return SenderType.STAFF;
    }
}
