package x10.Clothing.api.service.categorySerrvice.getCategoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.ICategoryRepository;
import x10.Clothing.api.service.categorySerrvice.createCategoryUc.CreateCategoryResp;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.category.CategoryError;

@Service
@RequiredArgsConstructor
public class GetCategoryBySlugImpl implements IGetCategoryBySlugUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public CreateCategoryResp getBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(c -> CreateCategoryResp.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .description(c.getDescription())
                        .active(c.isActive())
                        .bannerUrl(c.getBannerUrl())
                        .build())
                .orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_NOT_FOUND));
    }
}

