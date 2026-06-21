package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import x10.Clothing.api.service.reviewService.ICoreReviewService;
import x10.Clothing.api.service.reviewService.ReviewRequest;
import x10.Clothing.api.service.reviewService.ReviewResponse;
import x10.Clothing.api.service.reviewService.ReviewSummaryResponse;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ICoreReviewService coreReviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewResponse> createReview(
            @Valid @RequestBody ReviewRequest request,
            HttpServletRequest httpRequest) {

        ReviewResponse response = coreReviewService.createReview(request);

        return ApiResponse.success(
                201,
                "REVIEW.CREATE_SUCCESS",
                "Create review successfully",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ReviewResponse>> getAllReviews(HttpServletRequest httpRequest) {
        List<ReviewResponse> response = coreReviewService.getAllReviews();

        return ApiResponse.success(
                200,
                "REVIEW.GET_ALL_SUCCESS",
                "Get reviews successfully",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReviewResponse> getReviewById(
            @PathVariable("id") String id,
            HttpServletRequest httpRequest) {

        ReviewResponse response = coreReviewService.getReviewById(id);

        return ApiResponse.success(
                200,
                "REVIEW.GET_SUCCESS",
                "Get review successfully",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ReviewResponse>> getReviewsByProductId(
            @PathVariable("productId") String productId,
            HttpServletRequest httpRequest) {

        List<ReviewResponse> response = coreReviewService.getReviewsByProductId(productId);

        return ApiResponse.success(
                200,
                "REVIEW.GET_BY_PRODUCT_SUCCESS",
                "Get product reviews successfully",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/product/{productId}/summary")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReviewSummaryResponse> getReviewSummaryByProductId(
            @PathVariable("productId") String productId,
            HttpServletRequest httpRequest) {

        ReviewSummaryResponse response = coreReviewService.getReviewSummaryByProductId(productId);

        return ApiResponse.success(
                200,
                "REVIEW.GET_SUMMARY_SUCCESS",
                "Get product review summary successfully",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReviewResponse> updateReview(
            @PathVariable("id") String id,
            @Valid @RequestBody ReviewRequest request,
            HttpServletRequest httpRequest) {

        ReviewResponse response = coreReviewService.updateReview(id, request);

        return ApiResponse.success(
                200,
                "REVIEW.UPDATE_SUCCESS",
                "Update review successfully",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable("id") String id) {
        coreReviewService.deleteReview(id);
    }
}
