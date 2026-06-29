package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import x10.Clothing.api.service.refundService.ICoreRefundService;
import x10.Clothing.api.service.refundService.RefundDecisionRequest;
import x10.Clothing.api.service.refundService.RefundRequest;
import x10.Clothing.api.service.refundService.RefundResponse;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefundController {

    private final ICoreRefundService coreRefundService;

    @PostMapping("/refund-request")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RefundResponse> requestRefund(
            @Valid @RequestBody RefundRequest request,
            HttpServletRequest httpRequest) {

        RefundResponse response = coreRefundService.requestRefund(getCurrentUserId(), request);

        return ApiResponse.success(
                201,
                "REFUND.REQUEST_SUCCESS",
                "Gui yeu cau hoan tien thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/my-refunds")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RefundResponse>> getMyRefunds(HttpServletRequest httpRequest) {
        List<RefundResponse> response = coreRefundService.getMyRefunds(getCurrentUserId());

        return ApiResponse.success(
                200,
                "REFUND.GET_MY_SUCCESS",
                "Lay danh sach yeu cau hoan tien cua toi thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/refunds")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<RefundResponse>> getAllRefunds(HttpServletRequest httpRequest) {
        List<RefundResponse> response = coreRefundService.getAllRefunds();

        return ApiResponse.success(
                200,
                "REFUND.GET_ALL_SUCCESS",
                "Lay danh sach yeu cau hoan tien thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PostMapping("/refunds/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RefundResponse> approveRefund(
            @PathVariable("id") String id,
            @RequestBody(required = false) RefundDecisionRequest request,
            HttpServletRequest httpRequest) {

        RefundResponse response = coreRefundService.approveRefund(id, request);

        return ApiResponse.success(
                200,
                "REFUND.APPROVE_SUCCESS",
                "Duyet yeu cau hoan tien thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PostMapping("/refunds/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RefundResponse> rejectRefund(
            @PathVariable("id") String id,
            @RequestBody(required = false) RefundDecisionRequest request,
            HttpServletRequest httpRequest) {

        RefundResponse response = coreRefundService.rejectRefund(id, request);

        return ApiResponse.success(
                200,
                "REFUND.REJECT_SUCCESS",
                "Tu choi yeu cau hoan tien thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PostMapping("/refunds/{id}/mark-received")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RefundResponse> markReturnReceived(
            @PathVariable("id") String id,
            @RequestBody(required = false) RefundDecisionRequest request,
            HttpServletRequest httpRequest) {

        RefundResponse response = coreRefundService.markReturnReceived(id, request);

        return ApiResponse.success(
                200,
                "REFUND.MARK_RECEIVED_SUCCESS",
                "Xac nhan da nhan va kiem tra hang tra ve thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PostMapping("/refunds/{id}/mark-refunded")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RefundResponse> markRefunded(
            @PathVariable("id") String id,
            @RequestBody(required = false) RefundDecisionRequest request,
            HttpServletRequest httpRequest) {

        RefundResponse response = coreRefundService.markRefunded(id, request);

        return ApiResponse.success(
                200,
                "REFUND.MARK_REFUNDED_SUCCESS",
                "Xac nhan da hoan tien thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        return authentication.getPrincipal().toString();
    }
}
