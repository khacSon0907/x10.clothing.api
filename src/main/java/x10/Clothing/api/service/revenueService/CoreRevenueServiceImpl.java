package x10.Clothing.api.service.revenueService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.Clothing.api.Repository.IOrderRepository;
import x10.Clothing.api.common.domain.entities.order.OrderEntity;
import x10.Clothing.api.common.domain.entities.order.OrderItem;
import x10.Clothing.api.common.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoreRevenueServiceImpl implements ICoreRevenueService {

    private final IOrderRepository orderRepository;

    @Override
    public RevenueResponse getDailyRevenue(LocalDate date) {
        LocalDate targetDate = defaultToday(date);
        return buildRevenueResponse(
                "DAILY",
                targetDate,
                targetDate,
                findConfirmedOrdersBetween(targetDate, targetDate)
        );
    }

    @Override
    public RevenueResponse getWeeklyRevenue(LocalDate date) {
        LocalDate targetDate = defaultToday(date);
        LocalDate startDate = targetDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = targetDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return buildRevenueResponse(
                "WEEKLY",
                startDate,
                endDate,
                findConfirmedOrdersBetween(startDate, endDate)
        );
    }

    @Override
    public RevenueResponse getMonthlyRevenue(LocalDate date) {
        LocalDate targetDate = defaultToday(date);
        LocalDate startDate = targetDate.withDayOfMonth(1);
        LocalDate endDate = targetDate.withDayOfMonth(targetDate.lengthOfMonth());

        return buildRevenueResponse(
                "MONTHLY",
                startDate,
                endDate,
                findConfirmedOrdersBetween(startDate, endDate)
        );
    }

    @Override
    public RevenueResponse getTotalRevenue() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.CONFIRMED);

        return buildRevenueResponse(
                "TOTAL",
                null,
                null,
                orders
        );
    }

    @Override
    public List<TopSellingProductResponse> getTopSellingProducts(Integer limit) {
        int safeLimit = normalizeLimit(limit);
        Map<String, ProductSalesSummary> summaries = new LinkedHashMap<>();

        orderRepository.findByStatus(OrderStatus.CONFIRMED).stream()
                .filter(order -> order.getItems() != null)
                .flatMap(order -> order.getItems().stream())
                .filter(item -> item.getProductId() != null && !item.getProductId().isBlank())
                .forEach(item -> {
                    ProductSalesSummary summary = summaries.computeIfAbsent(
                            item.getProductId(),
                            productId -> new ProductSalesSummary(
                                    productId,
                                    item.getProductName(),
                                    item.getProductImage()
                            )
                    );

                    summary.add(item);
                });

        return summaries.values().stream()
                .sorted(Comparator
                        .comparingLong(ProductSalesSummary::getTotalQuantitySold)
                        .reversed()
                        .thenComparing(ProductSalesSummary::getTotalRevenue, Comparator.reverseOrder()))
                .limit(safeLimit)
                .map(ProductSalesSummary::toResponse)
                .toList();
    }

    private List<OrderEntity> findConfirmedOrdersBetween(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        return orderRepository.findByStatusAndCreatedAtBetween(
                OrderStatus.CONFIRMED,
                startDateTime,
                endDateTime
        );
    }

    private RevenueResponse buildRevenueResponse(String type, LocalDate startDate, LocalDate endDate, List<OrderEntity> orders) {
        BigDecimal totalRevenue = orders.stream()
                .map(OrderEntity::getTotalAmount)
                .filter(totalAmount -> totalAmount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RevenueResponse.builder()
                .type(type)
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue)
                .totalOrders(orders.size())
                .build();
    }

    private LocalDate defaultToday(LocalDate date) {
        return date == null ? LocalDate.now() : date;
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            return 10;
        }

        return Math.min(limit, 100);
    }

    private static class ProductSalesSummary {
        private final String productId;
        private String productName;
        private String productImage;
        private long totalQuantitySold;
        private BigDecimal totalRevenue = BigDecimal.ZERO;

        private ProductSalesSummary(String productId, String productName, String productImage) {
            this.productId = productId;
            this.productName = productName;
            this.productImage = productImage;
        }

        private void add(OrderItem item) {
            int quantity = item.getQuantity() == null ? 0 : item.getQuantity();
            BigDecimal itemRevenue = item.getTotalPrice() == null ? BigDecimal.ZERO : item.getTotalPrice();

            if (productName == null || productName.isBlank()) {
                productName = item.getProductName();
            }

            if (productImage == null || productImage.isBlank()) {
                productImage = item.getProductImage();
            }

            totalQuantitySold += quantity;
            totalRevenue = totalRevenue.add(itemRevenue);
        }

        private long getTotalQuantitySold() {
            return totalQuantitySold;
        }

        private BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        private TopSellingProductResponse toResponse() {
            return TopSellingProductResponse.builder()
                    .productId(productId)
                    .productName(productName)
                    .productImage(productImage)
                    .totalQuantitySold(totalQuantitySold)
                    .totalRevenue(totalRevenue)
                    .build();
        }
    }
}
