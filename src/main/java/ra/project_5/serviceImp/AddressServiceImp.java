package ra.project_5.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.mapper.MapperAddress;
import ra.project_5.model.dto.request.AddressRequest;
import ra.project_5.model.dto.response.AddressResponse;
import ra.project_5.model.entity.Address;
import ra.project_5.model.entity.User;
import ra.project_5.repository.AddressRepository;
import ra.project_5.repository.UserRepository;
import ra.project_5.service.AddressService;
import ra.project_5.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressServiceImp implements AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MapperAddress mapperAddress;
    @Autowired
    private UserService userService;
    @Override
    public AddressResponse save(long userId, AddressRequest addressRequest) {
        User user = userRepository.findById(userId).get();
        Address address = new Address();
        address.setFullAddress(addressRequest.getFullAddress());
        address.setPhone(addressRequest.getPhone());
        address.setUserAddr(user);
        address.setReceiveName(addressRequest.getReceiveName());
        return mapperAddress.mapperEntityToResponse(addressRepository.save(address));
    }

    @Override
    public Set<AddressResponse> findListByUserId(long userId) {
        userService.getInForUser(userId);
        try {
            return addressRepository.findAllByUsersIs(userId).stream()
                    .map(addressEntity -> mapperAddress.mapperEntityToResponse(addressEntity))
                    .collect(Collectors.toSet());
        } catch (Exception e){
            throw new CustomException("Address not fount");
        }
    }

    @Override
    public AddressResponse findByUserAndAddress(long userId, long addressId) {
        userService.getInForUser(userId);
        try {
            return mapperAddress.mapperEntityToResponse(addressRepository.findByUserAddr_IdAndAddressId(userId,addressId));
        }catch (Exception e){
            throw new CustomException("Erorr Address not Fount:" + e.getMessage());

        }

    }
}
