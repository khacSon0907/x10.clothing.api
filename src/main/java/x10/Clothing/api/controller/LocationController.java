package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.locationService.ILocationService;
import x10.Clothing.api.service.locationService.service.DistrictResponse;
import x10.Clothing.api.service.locationService.service.ProvinceResponse;
import x10.Clothing.api.service.locationService.service.WardResponse;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final ILocationService locationService;

    @GetMapping("/provinces")
    public ResponseEntity<ApiResponse<List<ProvinceResponse>>> getProvinces(
            HttpServletRequest request
    ) {

        List<ProvinceResponse> provinces =
                locationService.getProvinces();

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "LOCATION_PROVINCES_SUCCESS",
                        "Get provinces successfully",
                        provinces,
                        request.getRequestURI(),
                        UUID.randomUUID().toString()
                )
        );
    }

    @GetMapping("/provinces/{provinceCode}/districts")
    public ResponseEntity<ApiResponse<List<DistrictResponse>>> getDistricts(
            @PathVariable String provinceCode,
            HttpServletRequest request
    ) {

        List<DistrictResponse> districts =
                locationService.getDistricts(provinceCode);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "LOCATION_DISTRICTS_SUCCESS",
                        "Get districts successfully",
                        districts,
                        request.getRequestURI(),
                        UUID.randomUUID().toString()
                )
        );
    }

    @GetMapping("/districts/{districtCode}/wards")
    public ResponseEntity<ApiResponse<List<WardResponse>>> getWards(
            @PathVariable String districtCode,
            HttpServletRequest request
    ) {

        List<WardResponse> wards =
                locationService.getWards(districtCode);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200,
                        "LOCATION_WARDS_SUCCESS",
                        "Get wards successfully",
                        wards,
                        request.getRequestURI(),
                        UUID.randomUUID().toString()
                )
        );
    }
}