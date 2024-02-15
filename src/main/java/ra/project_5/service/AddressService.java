package ra.project_5.service;

import ra.project_5.model.dto.request.AddressRequest;
import ra.project_5.model.dto.response.AddressResponse;

import java.util.List;
import java.util.Set;

public interface AddressService {
    AddressResponse save(long userId, AddressRequest addressRequest);
    Set<AddressResponse> findListByUserId(long userId);
    AddressResponse findByUserAndAddress(long userId, long addressId);
}
