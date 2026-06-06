package x10.Clothing.api.service.addressService.getAddressesByUserIdUc;

import x10.Clothing.api.service.addressService.AddressResponse;
import java.util.List;

public interface IGetAddressesByUserIdUc {
    List<AddressResponse> execute(String userId);
}
