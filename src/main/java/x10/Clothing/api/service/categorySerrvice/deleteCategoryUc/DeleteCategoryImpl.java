package x10.Clothing.api.service.categorySerrvice.deleteCategoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.ICategoryRepository;
import x10.Clothing.api.share.exception.BusinessException;
import x10.Clothing.api.share.exception.category.CategoryError;

@Service
@RequiredArgsConstructor
public class DeleteCategoryImpl implements IDeleteCategoryUc {

    private final ICategoryRepository categoryRepository;

    @Override
    public void deleteCategory(String categoryId) {

        validateRequest(categoryId);

        // Check if category exists
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(CategoryError.CATEGORY_NOT_FOUND));

        if (categoryRepository.existsByParentId(categoryId)) {
            throw new BusinessException(CategoryError.CATEGORY_HAS_CHILDREN);
        }

        // Delete the category
        categoryRepository.deleteById(categoryId);
    }

    private void validateRequest(String categoryId) {

        if (categoryId == null || categoryId.isBlank()) {
            throw new IllegalArgumentException("Category ID is required");
        }
    }
}

