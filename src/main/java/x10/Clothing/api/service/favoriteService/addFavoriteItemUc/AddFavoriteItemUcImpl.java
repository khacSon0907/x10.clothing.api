package x10.Clothing.api.service.favoriteService.addFavoriteItemUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IFavoriteRepository;
import x10.Clothing.api.Repository.IProductRepository;
import x10.Clothing.api.common.domain.entities.FavoriteEntity;
import x10.Clothing.api.common.domain.entities.FavoriteItem;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.product.ProductError;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddFavoriteItemUcImpl implements IAddFavoriteItemUc {

    private final IFavoriteRepository favoriteRepository;
    private final IProductRepository productRepository;

    @Override
    public AddFavoriteItemResp execute(String userId, AddFavoriteItemReq req) {
        Optional<ProductEntity> productOpt = productRepository.findById(req.getProductId());
        if (productOpt.isEmpty()) {
            throw new BusinessException(ProductError.PRODUCT_NOT_FOUND);
        }

        ProductEntity product = productOpt.get();

        FavoriteEntity favorite = favoriteRepository.findByUserId(userId)
                .orElseGet(() -> {
                    FavoriteEntity newFav = FavoriteEntity.builder()
                            .favoriteId(UUID.randomUUID().toString())
                            .userId(userId)
                            .items(new ArrayList<>())
                            .totalItems(0)
                            .createdAt(Instant.now())
                            .updatedAt(Instant.now())
                            .build();
                    return favoriteRepository.save(newFav);
                });

        // check if already exists
        boolean exists = favorite.getItems().stream()
                .anyMatch(i -> i.getProductId().equals(req.getProductId()));

        if (!exists) {
            FavoriteItem item = FavoriteItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .productImage(null)
                    .price(product.getPrice())
                    .build();
            favorite.getItems().add(item);
            favorite.setTotalItems(favorite.getItems().size());
            favorite.setUpdatedAt(Instant.now());
            favoriteRepository.save(favorite);
        }

        return AddFavoriteItemResp.builder()
                .favoriteId(favorite.getFavoriteId())
                .userId(userId)
                .productId(req.getProductId())
                .build();
    }
}
