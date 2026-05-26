package x10.Clothing.api.service.categorySerrvice.createCategoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.ICategoryRepository;
import x10.Clothing.api.common.domain.entities.CategoryEntity;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.category.CategoryError;

import java.text.Normalizer;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCategoryImpl implements ICreateCategoryUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public CreateCategoryResp createCategory(CreateCategoryReq req) {

        validateRequest(req);

        // check duplicate name (ignore case)
        categoryRepository.findByNameIgnoreCase(req.getName())
                .ifPresent(category -> {
                    throw new BusinessException(CategoryError.CATEGORY_EXISTS);
                });

        // generate slug
        String baseSlug = generateSlug(req.getName());

        // ensure unique slug
        String finalSlug = generateUniqueSlug(baseSlug);

        CategoryEntity entity = CategoryEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(req.getName().trim())
                .slug(finalSlug)
                .description(req.getDescription())
                .active(req.getActive() == null || req.getActive())
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

    /**
     * Generate SEO friendly slug
     *
     * Example:
     * "Áo Thun Nam" -> "ao-thun-nam"
     */
    private String generateSlug(String input) {

        if (input == null || input.isBlank()) {
            return "";
        }

        // lowercase + trim
        String slug = input.toLowerCase(Locale.ROOT).trim();

        // remove vietnamese accents
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // replace đ
        slug = slug.replace("đ", "d");

        // remove special characters
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");

        // replace spaces with -
        slug = slug.replaceAll("\\s+", "-");

        // remove duplicate -
        slug = slug.replaceAll("-+", "-");

        // remove leading/trailing -
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }

    /**
     * Ensure slug uniqueness
     *
     * Example:
     * ao-thun
     * ao-thun-1
     * ao-thun-2
     */
    private String generateUniqueSlug(String baseSlug) {

        String candidate = baseSlug;
        int suffix = 1;

        while (categoryRepository.findBySlug(candidate).isPresent()) {
            candidate = baseSlug + "-" + suffix;
            suffix++;
        }

        return candidate;
    }

    private void validateRequest(CreateCategoryReq req) {

        if (req == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (req.getName() == null || req.getName().isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
    }
}