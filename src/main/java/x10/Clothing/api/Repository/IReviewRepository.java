package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.review.ReviewEntity;

import java.util.List;
import java.util.Optional;

public interface IReviewRepository {

    ReviewEntity save(ReviewEntity review);

    Optional<ReviewEntity> findById(String id);

    List<ReviewEntity> findAll();

    List<ReviewEntity> findByProductId(String productId);

    void deleteById(String id);
}
