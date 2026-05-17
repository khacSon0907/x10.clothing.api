package x10.Clothing.api.Repository;



import x10.Clothing.api.common.domain.entities.UserEntity;

import java.util.Optional;

public interface IUserRepository {

    UserEntity save(UserEntity user);

    Optional<UserEntity> findByEmail(String email);
}