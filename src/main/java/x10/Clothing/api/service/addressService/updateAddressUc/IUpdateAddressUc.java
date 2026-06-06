package x10.Clothing.api.service.addressService.updateAddressUc;

import x10.Clothing.api.service.addressService.AddressResponse;

public interface IUpdateAddressUc {
    AddressResponse execute(String userId, String addressId, UpdateAddressRequest request);
}
