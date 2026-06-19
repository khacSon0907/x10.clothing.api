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
import x10.Clothing.api.service.orderService.ICoreShippingRuleService;
import x10.Clothing.api.service.orderService.ShippingRuleRequest;
import x10.Clothing.api.service.orderService.ShippingRuleResponse;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-rules")
@RequiredArgsConstructor
public class ShippingRuleController {

    private final ICoreShippingRuleService shippingRuleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ShippingRuleResponse> createRule(
            @Valid @RequestBody ShippingRuleRequest request,
            HttpServletRequest httpRequest
    ) {
        ShippingRuleResponse response = shippingRuleService.createRule(request);
        return ApiResponse.success(
                201,
                "SHIPPING_RULE.CREATE_SUCCESS",
                "Tao cau hinh phi ship thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ShippingRuleResponse> updateRule(
            @PathVariable("id") String id,
            @Valid @RequestBody ShippingRuleRequest request,
            HttpServletRequest httpRequest
    ) {
        ShippingRuleResponse response = shippingRuleService.updateRule(id, request);
        return ApiResponse.success(
                200,
                "SHIPPING_RULE.UPDATE_SUCCESS",
                "Cap nhat cau hinh phi ship thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ShippingRuleResponse>> getAllRules(HttpServletRequest httpRequest) {
        List<ShippingRuleResponse> response = shippingRuleService.getAllRules();
        return ApiResponse.success(
                200,
                "SHIPPING_RULE.LIST_SUCCESS",
                "Lay danh sach cau hinh phi ship thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ShippingRuleResponse> getActiveRule(HttpServletRequest httpRequest) {
        ShippingRuleResponse response = shippingRuleService.getActiveRule();
        return ApiResponse.success(
                200,
                "SHIPPING_RULE.ACTIVE_SUCCESS",
                "Lay cau hinh phi ship dang ap dung thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable("id") String id) {
        shippingRuleService.deleteRule(id);
    }
}
