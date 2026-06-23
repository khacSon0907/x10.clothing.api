package x10.Clothing.api.service.revenueService;

import java.time.LocalDate;

public interface ICoreRevenueService {

    RevenueResponse getDailyRevenue(LocalDate date);

    RevenueResponse getWeeklyRevenue(LocalDate date);

    RevenueResponse getMonthlyRevenue(LocalDate date);

    RevenueResponse getTotalRevenue();

    java.util.List<TopSellingProductResponse> getTopSellingProducts(Integer limit);
}
