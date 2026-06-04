package x10.Clothing.api.service.favoriteService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.service.favoriteService.addFavoriteItemUc.AddFavoriteItemReq;
import x10.Clothing.api.service.favoriteService.addFavoriteItemUc.AddFavoriteItemResp;
import x10.Clothing.api.service.favoriteService.addFavoriteItemUc.IAddFavoriteItemUc;
import x10.Clothing.api.service.favoriteService.clearFavoriteUc.IClearFavoriteUc;
import x10.Clothing.api.service.favoriteService.getFavoriteUc.GetFavoriteUcResp;
import x10.Clothing.api.service.favoriteService.getFavoriteUc.IGetFavoriteUc;
import x10.Clothing.api.service.favoriteService.removeFavoriteItemUc.IRemoveFavoriteItemUc;

@Service
@RequiredArgsConstructor
public class CoreFavoriteServiceImpl implements ICoreFavoriteService {

    private final IGetFavoriteUc getFavoriteUc;
    private final IAddFavoriteItemUc addFavoriteItemUc;
    private final IRemoveFavoriteItemUc removeFavoriteItemUc;
    private final IClearFavoriteUc clearFavoriteUc;

    @Override
    public GetFavoriteUcResp getFavorite(String userId) {
        return getFavoriteUc.execute(userId);
    }

    @Override
    public AddFavoriteItemResp addItem(String userId, AddFavoriteItemReq req) {
        return addFavoriteItemUc.execute(userId, req);
    }

    @Override
    public String removeItem(String userId, String productId) {
        return removeFavoriteItemUc.execute(userId, productId);
    }

    @Override
    public String clearFavorite(String userId) {
        return clearFavoriteUc.execute(userId);
    }
}

