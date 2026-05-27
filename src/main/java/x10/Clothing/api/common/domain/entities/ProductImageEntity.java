package x10.Clothing.api.common.domain.entities;


public class ProductImageEntity {

    private String id;

    private String url;

    private String publicId; // Cloudinary public_id

    private Boolean main; // ảnh chính hay không

    private Integer sortOrder; // thứ tự hiển thị ảnh
}