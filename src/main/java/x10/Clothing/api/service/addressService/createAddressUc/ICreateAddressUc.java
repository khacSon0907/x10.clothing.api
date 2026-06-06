package x10.Clothing.api.service.addressService.createAddressUc;

import x10.Clothing.api.service.addressService.AddressResponse;

public interface ICreateAddressUc {
    AddressResponse execute(String userId, CreateAddressRequest request);
}
