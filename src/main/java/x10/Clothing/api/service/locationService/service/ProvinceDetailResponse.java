package x10.Clothing.api.service.locationService.service;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceDetailResponse {

    private Integer code;

    private String name;

    private List<DistrictResponse> districts;
}