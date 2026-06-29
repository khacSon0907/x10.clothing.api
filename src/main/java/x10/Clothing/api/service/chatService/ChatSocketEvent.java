package x10.Clothing.api.service.chatService;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSocketEvent {

    private String type;
    private ChatConversationResponse conversation;
    private ChatMessageResponse message;
}
