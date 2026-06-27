package x10.Clothing.api.infrastructure.chat.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMongoRepository
        extends MongoRepository<MessageDocument, String> {

    List<MessageDocument> findByConversationIdOrderByCreatedAtAsc(
            String conversationId
    );

}