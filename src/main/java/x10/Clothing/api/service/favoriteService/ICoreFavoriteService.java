package x10.Clothing.api.service.favoriteService;

import x10.Clothing.api.service.favoriteService.addFavoriteItemUc.AddFavoriteItemReq;
import x10.Clothing.api.service.favoriteService.addFavoriteItemUc.AddFavoriteItemResp;
import x10.Clothing.api.service.favoriteService.getFavoriteUc.GetFavoriteUcResp;

public interface ICoreFavoriteService {
    GetFavoriteUcResp getFavorite(String userId);
    AddFavoriteItemResp addItem(String userId, AddFavoriteItemReq req);
    String removeItem(String userId, String productId);
    String clearFavorite(String userId);
}

