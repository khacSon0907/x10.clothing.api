package x10.Clothing.api.service.imageService;

import x10.Clothing.api.common.domain.dto.request.ImageSignatureRequest;
import x10.Clothing.api.common.domain.dto.respone.ImageSignatureResponse;

public interface IImageSignatureService {

    ImageSignatureResponse generateSignature(ImageSignatureRequest request);
}