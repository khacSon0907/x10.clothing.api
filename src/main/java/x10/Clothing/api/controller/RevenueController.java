package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import x10.Clothing.api.service.revenueService.ICoreRevenueService;
import x10.Clothing.api.service.revenueService.RevenueResponse;
import x10.Clothing.api.service.revenueService.TopSellingProductResponse;
import x10.Clothing.api.share.response.ApiResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenue")
@RequiredArgsConstructor
public class RevenueController {

    private final ICoreRevenueService coreRevenueService;

    @GetMapping("/daily")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RevenueResponse> getDailyRevenue(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            HttpServletRequest httpRequest) {

        RevenueResponse response = coreRevenueService.getDailyRevenue(date);

        return ApiResponse.success(
                200,
                "REVENUE.GET_DAILY_SUCCESS",
                "Lay doanh thu theo ngay thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/weekly")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RevenueResponse> getWeeklyRevenue(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            HttpServletRequest httpRequest) {

        RevenueResponse response = coreRevenueService.getWeeklyRevenue(date);

        return ApiResponse.success(
                200,
                "REVENUE.GET_WEEKLY_SUCCESS",
                "Lay doanh thu theo tuan thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/monthly")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RevenueResponse> getMonthlyRevenue(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            HttpServletRequest httpRequest) {

        RevenueResponse response = coreRevenueService.getMonthlyRevenue(date);

        return ApiResponse.success(
                200,
                "REVENUE.GET_MONTHLY_SUCCESS",
                "Lay doanh thu theo thang thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<RevenueResponse> getTotalRevenue(HttpServletRequest httpRequest) {
        RevenueResponse response = coreRevenueService.getTotalRevenue();

        return ApiResponse.success(
                200,
                "REVENUE.GET_TOTAL_SUCCESS",
                "Lay tong doanh thu thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/top-products")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<TopSellingProductResponse>> getTopSellingProducts(
            @RequestParam(value = "limit", required = false) Integer limit,
            HttpServletRequest httpRequest) {

        List<TopSellingProductResponse> response = coreRevenueService.getTopSellingProducts(limit);

        return ApiResponse.success(
                200,
                "REVENUE.GET_TOP_PRODUCTS_SUCCESS",
                "Lay danh sach san pham ban chay thanh cong",
                response,
                httpRequest.getRequestURI(),
                null
        );
    }
}
