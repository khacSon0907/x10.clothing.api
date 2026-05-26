package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.categorySerrvice.ICoreCategoryService;
import x10.Clothing.api.service.categorySerrvice.createCategoryUc.CreateCategoryReq;
import x10.Clothing.api.service.categorySerrvice.createCategoryUc.CreateCategoryResp;
import x10.Clothing.api.service.categorySerrvice.updateCategoryUc.UpdateCategoryReq;
import x10.Clothing.api.service.categorySerrvice.updateCategoryUc.UpdateCategoryResp;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICoreCategoryService coreCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateCategoryResp> createCategory(
            @Valid @RequestBody CreateCategoryReq req,
            HttpServletRequest request
    ) {
        CreateCategoryResp resp = coreCategoryService.createCategory(req);
        return ApiResponse.success(
                201,
                "CATEGORY.CREATE_SUCCESS",
                "Tạo danh mục thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CreateCategoryResp>> listCategories(HttpServletRequest request) {
        List<CreateCategoryResp> list = coreCategoryService.getAll();
        return ApiResponse.success(
                200,
                "CATEGORY.LIST_SUCCESS",
                "Lấy danh sách danh mục thành công",
                list,
                request.getRequestURI(),
                null
        );
    }

    @GetMapping("/{slug}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CreateCategoryResp> getBySlug(
            @PathVariable("slug") String slug,
            HttpServletRequest request
    ) {
        CreateCategoryResp resp = coreCategoryService.getBySlug(slug);
        return ApiResponse.success(
                200,
                "CATEGORY.GET_SUCCESS",
                "Lấy thông tin danh mục thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UpdateCategoryResp> updateCategory(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateCategoryReq req,
            HttpServletRequest request
    ) {
        // Set the ID from path variable
        req.setId(id);
        UpdateCategoryResp resp = coreCategoryService.updateCategory(req);
        return ApiResponse.success(
                200,
                "CATEGORY.UPDATE_SUCCESS",
                "Cập nhật danh mục thành công",
                resp,
                request.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable("id") String id,
            HttpServletRequest request
    ) {
        coreCategoryService.deleteCategory(id);
    }
}
