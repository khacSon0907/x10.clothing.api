package x10.Clothing.api.service.addressService.getAddressUc;

import x10.Clothing.api.service.addressService.AddressResponse;

public interface IGetAddressUc {
    AddressResponse execute(String userId, String addressId);
}
