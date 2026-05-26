package x10.Clothing.api.service.categorySerrvice.getCategoryUc;

import x10.Clothing.api.service.categorySerrvice.createCategoryUc.CreateCategoryResp;

import java.util.List;

public interface IGetCategoriesUc {
    List<CreateCategoryResp> getAll();
}

