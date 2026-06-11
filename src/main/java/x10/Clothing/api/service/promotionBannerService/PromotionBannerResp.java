package x10.Clothing.api.service.promotionBannerService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionBannerResp {
    private String id;
    private String title;
    private boolean active;
    private Integer sortOrder;
    private Instant startDate;
    private Instant endDate;
}
