package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.favorite.FavoriteEntity;

import java.util.Optional;

public interface IFavoriteRepository {

    Optional<FavoriteEntity> findByUserId(String userId);

    FavoriteEntity save(FavoriteEntity favorite);

    void deleteByUserId(String userId);
}

