package x10.Clothing.api.service.categorySerrvice.getCategoryUc;

import x10.Clothing.api.service.categorySerrvice.createCategoryUc.CreateCategoryResp;

public interface IGetCategoryBySlugUc {
    CreateCategoryResp getBySlug(String slug);
}

