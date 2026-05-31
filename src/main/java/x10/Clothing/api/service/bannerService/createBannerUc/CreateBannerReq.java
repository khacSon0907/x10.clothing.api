package x10.Clothing.api.service.bannerService.createBannerUc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBannerReq {

    @NotBlank(message = "Tiêu đề banner không được để trống")
    @Size(max = 200, message = "Tiêu đề banner không được vượt quá 200 ký tự")
    private String title;

    private String subtitle;

    @NotBlank(message = "URL ảnh không được để trống")
    private String imageUrl;

    private String linkUrl;

    private Boolean active;

    private Integer sortOrder;
}
