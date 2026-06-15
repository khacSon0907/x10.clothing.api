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
import x10.Clothing.api.common.domain.enums.OrderStatus;
import x10.Clothing.api.service.orderService.ICoreOrderService;
import x10.Clothing.api.service.orderService.OrderResponse;
import x10.Clothing.api.service.orderService.createOrderUc.CreateOrderRequest;
import x10.Clothing.api.service.orderService.updateOrderUc.UpdateOrderRequest;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final ICoreOrderService coreOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            HttpServletRequest httpRequest
    ) {
        OrderResponse response = coreOrderService.createOrder(request);
        return ApiResponse.success(
                201,
                "ORDER.CREATE_SUCCESS",
                "Tao don hang thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<OrderResponse>> getAllOrders(HttpServletRequest httpRequest) {
        List<OrderResponse> response = coreOrderService.getAllOrders();
        return ApiResponse.success(
                200,
                "ORDER.GET_ALL_SUCCESS",
                "Lay danh sach don hang thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<OrderResponse>> getOrdersByUserId(
            @PathVariable("userId") String userId,
            HttpServletRequest httpRequest
    ) {
        List<OrderResponse> response = coreOrderService.getOrdersByUserId(userId);
        return ApiResponse.success(
                200,
                "ORDER.GET_BY_USER_SUCCESS",
                "Lay danh sach don hang cua nguoi dung thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<OrderResponse> getOrder(
            @PathVariable("orderId") String orderId,
            HttpServletRequest httpRequest
    ) {
        OrderResponse response = coreOrderService.getOrder(orderId);
        return ApiResponse.success(
                200,
                "ORDER.GET_SUCCESS",
                "Lay chi tiet don hang thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PutMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<OrderResponse> updateOrder(
            @PathVariable("orderId") String orderId,
            @Valid @RequestBody UpdateOrderRequest request,
            HttpServletRequest httpRequest
    ) {
        OrderResponse response = coreOrderService.updateOrder(orderId, request);
        return ApiResponse.success(
                200,
                "ORDER.UPDATE_SUCCESS",
                "Cap nhat don hang thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PutMapping("/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<OrderResponse> confirmOrder(
            @PathVariable("orderId") String orderId,
            HttpServletRequest httpRequest
    ) {
        UpdateOrderRequest request = new UpdateOrderRequest();
        request.setStatus(OrderStatus.CONFIRMED);

        OrderResponse response = coreOrderService.updateOrder(orderId, request);

        return ApiResponse.success(
                200,
                "ORDER.CONFIRM_SUCCESS",
                "Xac nhan don hang thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PutMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<OrderResponse> cancelOrder(
            @PathVariable("orderId") String orderId,
            @RequestBody(required = false) UpdateOrderRequest request,
            HttpServletRequest httpRequest
    ) {
        UpdateOrderRequest cancelRequest = request == null ? new UpdateOrderRequest() : request;
        cancelRequest.setStatus(OrderStatus.CANCELLED);

        OrderResponse response = coreOrderService.updateOrder(orderId, cancelRequest);

        return ApiResponse.success(
                200,
                "ORDER.CANCEL_SUCCESS",
                "Huy don hang thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> deleteOrder(
            @PathVariable("orderId") String orderId,
            HttpServletRequest httpRequest
    ) {
        coreOrderService.deleteOrder(orderId);
        return ApiResponse.success(
                200,
                "ORDER.DELETE_SUCCESS",
                "Xoa don hang thanh cong",
                "ORDER_DELETED",
                httpRequest.getRequestURI(),
                null
        );
    }
}
