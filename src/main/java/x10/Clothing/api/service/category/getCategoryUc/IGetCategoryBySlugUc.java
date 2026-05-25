package x10.Clothing.api.service.category.getCategoryUc;

import x10.Clothing.api.service.category.createCategoryUc.CreateCategoryResp;

public interface IGetCategoryBySlugUc {
    CreateCategoryResp getBySlug(String slug);
}

