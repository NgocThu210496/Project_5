package ra.project_5.mapper;

import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.AddressRequest;
import ra.project_5.model.dto.response.AddressResponse;
import ra.project_5.model.entity.Address;
@Component
public class MapperAddress implements MapperGeneric<Address, AddressRequest, AddressResponse>{
    @Override
    public Address mapperRequestToEntity(AddressRequest addressRequest) {
        return Address.builder()
                .fullAddress(addressRequest.getFullAddress())
                .phone(addressRequest.getPhone())
                .receiveName(addressRequest.getReceiveName())
                .build();
    }

    @Override
    public AddressResponse mapperEntityToResponse(Address address) {
        return AddressResponse.builder()
                .userId(address.getUserAddr().getId())
                .addressId(address.getAddressId())
                .fullAddress(address.getFullAddress())
                .phone(address.getPhone())
                .receiveName(address.getReceiveName())
                .build();
    }
}
