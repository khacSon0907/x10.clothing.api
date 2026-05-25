package x10.Clothing.api.service.category.createCategoryUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryResp {
    private String id;
    private String name;
    private String slug;
    private String description;
    private boolean active;
}

