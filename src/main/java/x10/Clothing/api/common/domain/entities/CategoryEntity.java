package x10.Clothing.api.common.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class CategoryEntity {
    private String id;
    private String name;
    private String description;
    private String  slug;
    // active / hidden
    private boolean active;

    private Instant createdAt;
    private Instant updatedAt;
}
