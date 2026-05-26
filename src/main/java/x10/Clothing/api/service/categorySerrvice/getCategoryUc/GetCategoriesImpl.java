package x10.Clothing.api.service.categorySerrvice.getCategoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.ICategoryRepository;
import x10.Clothing.api.common.domain.entities.CategoryEntity;
import x10.Clothing.api.service.categorySerrvice.createCategoryUc.CreateCategoryResp;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetCategoriesImpl implements IGetCategoriesUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public List<CreateCategoryResp> getAll() {
        List<CategoryEntity> list = categoryRepository.findAll();
        return list.stream().map(c -> CreateCategoryResp.builder()
                .id(c.getId())
                .name(c.getName())
                .slug(c.getSlug())
                .description(c.getDescription())
                .active(c.isActive())
                .build()
        ).collect(Collectors.toList());
    }
}

