package x10.Clothing.api.common.domain.entities.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.Clothing.api.common.domain.enums.SenderType;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    private String id ;

    private String conversationId;

    private String senderId;

    /**
     * CUSTOMER | ADMIN | AI
     */
    private SenderType senderType;

    /**
     * Nội dung
     */
    private String content;

    /**
     * Đã xem chưa
     */
    private Boolean seen;

    /**
     * Thời gian gửi
     */
    private Instant createdAt;
}
