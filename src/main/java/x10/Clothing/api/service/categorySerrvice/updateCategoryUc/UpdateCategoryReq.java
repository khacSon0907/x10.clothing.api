package x10.Clothing.api.service.categorySerrvice.updateCategoryUc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class UpdateCategoryReq {

    @NotBlank(message = "Category ID không được để trống")
    private String id;

    @NotBlank(message = "Category name không được để trống")
    @Size(min = 1, max = 255, message = "Category name phải từ 1 đến 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Description không được vượt quá 1000 ký tự")
    private String description;

    private String bannerUrl;

    private Boolean active;
}

