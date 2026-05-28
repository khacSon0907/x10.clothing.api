package x10.Clothing.api.common.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import x10.Clothing.api.common.domain.enums.ImageTargetType;

@AllArgsConstructor
@Builder
@Data
public class ImageSignatureRequest {

    private ImageTargetType targetType;
}
