package x10.Clothing.api.service.favoriteService.getFavoriteUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IFavoriteRepository;
import x10.Clothing.api.common.domain.entities.favorite.FavoriteEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetFavoriteUcImpl implements IGetFavoriteUc {

    private final IFavoriteRepository favoriteRepository;

    @Override
    public GetFavoriteUcResp execute(String userId) {
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

        return mapToResp(favorite);
    }

    private GetFavoriteUcResp mapToResp(FavoriteEntity favorite) {
        List<GetFavoriteUcResp.FavoriteItemResp> items = favorite.getItems() == null ? new ArrayList<>() :
                favorite.getItems().stream()
                        .map(item -> GetFavoriteUcResp.FavoriteItemResp.builder()
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .productImage(item.getProductImage())
                                .build())
                        .collect(Collectors.toList());

        return GetFavoriteUcResp.builder()
                .favoriteId(favorite.getFavoriteId())
                .userId(favorite.getUserId())
                .items(items)
                .totalItems(favorite.getTotalItems())
                .createdAt(favorite.getCreatedAt())
                .updatedAt(favorite.getUpdatedAt())
                .build();
    }
}

