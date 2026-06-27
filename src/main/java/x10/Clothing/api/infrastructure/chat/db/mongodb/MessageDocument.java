package x10.Clothing.api.infrastructure.chat.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.Clothing.api.common.domain.enums.SenderType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class MessageDocument {

    @Id
    private String id;

    /**
     * Thuộc cuộc hội thoại nào
     */
    private String conversationId;

    /**
     * ID người gửi
     */
    private String senderId;

    /**
     * CUSTOMER | ADMIN | AI
     */
    private SenderType senderType;

    /**
     * Nội dung tin nhắn
     */
    private String content;

    /**
     * Đã xem chưa
     */
    @Builder.Default
    private Boolean seen = false;

    /**
     * Thời gian gửi
     */
    private Instant createdAt;
}