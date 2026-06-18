package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.banner.BannerEntity;

import java.util.List;
import java.util.Optional;

public interface IBannerRepository {
    BannerEntity save(BannerEntity banner);
    Optional<BannerEntity> findById(String id);
    List<BannerEntity> findAll();
    List<BannerEntity> findAllActive();
    void deleteById(String id);
}
