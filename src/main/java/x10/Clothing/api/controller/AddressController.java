package x10.Clothing.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import x10.Clothing.api.service.addressService.AddressResponse;
import x10.Clothing.api.service.addressService.ICoreAddressService;
import x10.Clothing.api.service.addressService.createAddressUc.CreateAddressRequest;
import x10.Clothing.api.service.addressService.updateAddressUc.UpdateAddressRequest;
import x10.Clothing.api.share.response.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final ICoreAddressService addressService;

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AddressResponse> createAddress(
            @PathVariable("userId") String userId,
            @Valid @RequestBody CreateAddressRequest request,
            HttpServletRequest servletRequest
    ) {
        AddressResponse response = addressService.createAddress(userId, request);
        return ApiResponse.success(
                201,
                "ADDRESS.CREATE_SUCCESS",
                "Tạo địa chỉ thành công",
                response,
                servletRequest.getRequestURI(),
                null
        );
    }

    @PutMapping("/{addressId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AddressResponse> updateAddress(
            @PathVariable("userId") String userId,
            @PathVariable("addressId") String addressId,
            @Valid @RequestBody UpdateAddressRequest request,
            HttpServletRequest servletRequest
    ) {
        AddressResponse response = addressService.updateAddress(userId, addressId, request);
        return ApiResponse.success(
                200,
                "ADDRESS.UPDATE_SUCCESS",
                "Cập nhật địa chỉ thành công",
                response,
                servletRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/{addressId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AddressResponse> getAddress(
            @PathVariable("userId") String userId,
            @PathVariable("addressId") String addressId,
            HttpServletRequest servletRequest
    ) {
        AddressResponse response = addressService.getAddress(userId, addressId);
        return ApiResponse.success(
                200,
                "ADDRESS.GET_SUCCESS",
                "Lấy thông tin địa chỉ thành công",
                response,
                servletRequest.getRequestURI(),
                null
        );
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<AddressResponse>> getAddresses(
            @PathVariable("userId") String userId,
            HttpServletRequest servletRequest
    ) {
        List<AddressResponse> response = addressService.getAddressesByUserId(userId);
        return ApiResponse.success(
                200,
                "ADDRESS.GET_ALL_SUCCESS",
                "Lấy danh sách địa chỉ thành công",
                response,
                servletRequest.getRequestURI(),
                null
        );
    }

    @DeleteMapping("/{addressId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> deleteAddress(
            @PathVariable("userId") String userId,
            @PathVariable("addressId") String addressId,
            HttpServletRequest servletRequest
    ) {
        addressService.deleteAddress(userId, addressId);
        return ApiResponse.success(
                200,
                "ADDRESS.DELETE_SUCCESS",
                "Xóa địa chỉ thành công",
                "ADDRESS_DELETED",
                servletRequest.getRequestURI(),
                null
        );
    }
}
