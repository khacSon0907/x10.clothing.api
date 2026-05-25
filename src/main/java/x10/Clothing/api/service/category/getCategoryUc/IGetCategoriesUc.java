package x10.Clothing.api.service.category.getCategoryUc;

import x10.Clothing.api.service.category.createCategoryUc.CreateCategoryResp;

import java.util.List;

public interface IGetCategoriesUc {
    List<CreateCategoryResp> getAll();
}

