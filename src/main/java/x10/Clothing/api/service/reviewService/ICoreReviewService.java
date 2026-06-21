package x10.Clothing.api.service.reviewService;

import java.util.List;

public interface ICoreReviewService {

    ReviewResponse createReview(ReviewRequest request);

    ReviewResponse getReviewById(String id);

    List<ReviewResponse> getAllReviews();

    List<ReviewResponse> getReviewsByProductId(String productId);

    ReviewSummaryResponse getReviewSummaryByProductId(String productId);

    ReviewResponse updateReview(String id, ReviewRequest request);

    void deleteReview(String id);
}
