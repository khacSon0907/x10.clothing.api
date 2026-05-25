package x10.Clothing.api.service.category;

import x10.Clothing.api.service.category.createCategoryUc.CreateCategoryReq;
import x10.Clothing.api.service.category.createCategoryUc.CreateCategoryResp;

import java.util.List;

public interface ICoreCategoryService {
    CreateCategoryResp createCategory(CreateCategoryReq req);
    List<CreateCategoryResp> getAll();
    CreateCategoryResp getBySlug(String slug);
}
