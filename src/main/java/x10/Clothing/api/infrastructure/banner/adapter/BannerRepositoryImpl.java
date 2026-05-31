package x10.Clothing.api.infrastructure.banner.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.Clothing.api.Repository.IBannerRepository;
import x10.Clothing.api.common.domain.entities.BannerEntity;
import x10.Clothing.api.infrastructure.banner.db.mongodb.BannerMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements IBannerRepository {

    private final BannerMongoRepository bannerMongoRepository;

    @Override
    public BannerEntity save(BannerEntity banner) {
        var doc = BannerMapper.toDocument(banner);
        var saved = bannerMongoRepository.save(doc);
        return BannerMapper.toEntity(saved);
    }

    @Override
    public Optional<BannerEntity> findById(String id) {
        return bannerMongoRepository.findById(id).map(BannerMapper::toEntity);
    }

    @Override
    public List<BannerEntity> findAll() {
        return bannerMongoRepository.findAll()
                .stream()
                .map(BannerMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<BannerEntity> findAllActive() {
        return bannerMongoRepository.findByActiveTrue()
                .stream()
                .map(BannerMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        bannerMongoRepository.deleteById(id);
    }
}
