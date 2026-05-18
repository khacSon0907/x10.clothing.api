package x10.Clothing.api.infrastructure.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.Clothing.api.Repository.IUserRepository;
import x10.Clothing.api.common.domain.entities.UserEntity;
import x10.Clothing.api.infrastructure.user.adapter.mapper.UserMapper;
import x10.Clothing.api.infrastructure.user.db.mongodb.UserDocument;
import x10.Clothing.api.infrastructure.user.db.mongodb.UserMongoRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {

    private final UserMongoRepository userMongoRepository;

    @Override
    public UserEntity save(UserEntity user) {

        UserDocument document = UserMapper.toDocument(user);

        UserDocument savedDocument = userMongoRepository.save(document);

        return UserMapper.toEntity(savedDocument);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {

        return userMongoRepository.findByEmail(email)
                .map(UserMapper::toEntity);
    }
}