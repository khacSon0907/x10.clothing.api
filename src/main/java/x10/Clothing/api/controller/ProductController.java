package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.common.domain.entities.ProductEntity;
import x10.Clothing.api.service.productService.ICoreProductService;
import x10.Clothing.api.service.productService.createProductUc.CreateProductRequest;
import x10.Clothing.api.service.productService.createProductUc.CreateProductResponse;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ICoreProductService coreProductService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            HttpServletRequest httpRequest) {

        CreateProductResponse response = coreProductService.createProduct(request);

        return ApiResponse.success(
                201,
                "PRODUCT.CREATE_SUCCESS",
                "Tạo sản phẩm thành công",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<ProductEntity>> getAllProducts(HttpServletRequest httpRequest) {
        
        List<ProductEntity> response = coreProductService.getAllProducts();

        return ApiResponse.success(
                200,
                "PRODUCT.GET_ALL_SUCCESS",
                "Lấy danh sách sản phẩm thành công",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }
}
