package x10.Clothing.api.common.domain.entities;

import java.util.List;

public class ColorVariantEntity {
    private String id;

    private String colorName; // Black, White, Red

    private String colorCode; // #000000, #FFFFFF

    private List<ProductImageEntity> images;

    private List<SizeVariantEntity> sizes ;
}