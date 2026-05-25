package x10.Clothing.api.service.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.service.category.createCategoryUc.CreateCategoryReq;
import x10.Clothing.api.service.category.createCategoryUc.CreateCategoryResp;
import x10.Clothing.api.service.category.createCategoryUc.ICreateCategoryUc;
import x10.Clothing.api.service.category.getCategoryUc.IGetCategoriesUc;
import x10.Clothing.api.service.category.getCategoryUc.IGetCategoryBySlugUc;

import java.util.List;

@Service
@AllArgsConstructor
public class CoreCategoryServiceImpl implements ICoreCategoryService {

    private final ICreateCategoryUc createCategoryUc;
    private final IGetCategoriesUc getCategoriesUc;
    private final IGetCategoryBySlugUc getCategoryBySlugUc;

    @Override
    public CreateCategoryResp createCategory(CreateCategoryReq req) {
        return createCategoryUc.createCategory(req);
    }

    @Override
    public List<CreateCategoryResp> getAll() {
        return getCategoriesUc.getAll();
    }

    @Override
    public CreateCategoryResp getBySlug(String slug) {
        return getCategoryBySlugUc.getBySlug(slug);
    }
}
