package x10.Clothing.api.service.locationService;

import x10.Clothing.api.service.locationService.service.DistrictResponse;
import x10.Clothing.api.service.locationService.service.ProvinceResponse;
import x10.Clothing.api.service.locationService.service.WardResponse;

import java.util.List;

public interface ILocationService {

        List<ProvinceResponse> getProvinces();

        List<DistrictResponse> getDistricts(String provinceCode);

        List<WardResponse> getWards(String districtCode);
}
