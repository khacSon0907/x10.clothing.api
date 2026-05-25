package x10.Clothing.api.service.category.createCategoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.ICategoryRepository;
import x10.Clothing.api.common.domain.entities.CategoryEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.category.CategoryError;

import java.time.Instant;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCategoryImpl implements ICreateCategoryUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public CreateCategoryResp createCategory(CreateCategoryReq req) {
        // validate uniqueness by name
        categoryRepository.findByName(req.getName()).ifPresent(c -> {
            throw new BusinessException(CategoryError.CATEGORY_EXISTS);
        });

        // generate slug
        String slug = generateSlug(req.getName());

        // ensure slug unique
        int suffix = 0;
        String candidate = slug;
        while (categoryRepository.findBySlug(candidate).isPresent()) {
            suffix++;
            candidate = slug + "-" + suffix;
        }

        CategoryEntity entity = CategoryEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(req.getName())
                .slug(candidate)
                .description(req.getDescription())
                .active(req.getActive() == null ? true : req.getActive())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        CategoryEntity saved = categoryRepository.save(entity);

        return CreateCategoryResp.builder()
                .id(saved.getId())
                .name(saved.getName())
                .slug(saved.getSlug())
                .description(saved.getDescription())
                .active(saved.isActive())
                .build();
    }

    private String generateSlug(String input) {
        if (input == null) return "";
        String nowhitespace = input.trim().toLowerCase(Locale.ROOT);
        // remove non-alphanumeric except hyphen
        String normalized = Pattern.compile("[^a-z0-9\\-\\s]")
                .matcher(nowhitespace)
                .replaceAll("");
        String slug = normalized.replaceAll("\\s+", "-");
        return slug;
    }
}
