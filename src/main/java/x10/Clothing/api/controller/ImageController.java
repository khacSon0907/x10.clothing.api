package x10.Clothing.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.common.domain.dto.request.ImageSignatureRequest;
import x10.Clothing.api.common.domain.dto.respone.ImageSignatureResponse;
import x10.Clothing.api.service.imageService.IImageSignatureService;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final IImageSignatureService imageSignatureService;


    @PostMapping("/signature")
    public ResponseEntity<ImageSignatureResponse> generateSignature(
            @RequestBody ImageSignatureRequest request
    ) {
        ImageSignatureResponse response = imageSignatureService.generateSignature(request);
        return ResponseEntity.ok(response);
    }
}