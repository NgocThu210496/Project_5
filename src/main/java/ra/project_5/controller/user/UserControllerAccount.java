package ra.project_5.controller.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.model.dto.request.AddressRequest;
import ra.project_5.model.dto.request.ChangePasswordRequest;
import ra.project_5.model.dto.request.UserUpdateRequest;
import ra.project_5.model.dto.response.AddressResponse;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.UserResponse;
import ra.project_5.repository.UserRepository;
import ra.project_5.service.AddressService;
import ra.project_5.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user/")
public class UserControllerAccount {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressService addressService;

    @GetMapping("account/{userId}")
    public ResponseEntity<?>inForAccountById(@PathVariable long userId){
        UserResponse userResponse= userService.getInForUser(userId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Thông tin người dùng");
        baseResponse.setData(userResponse);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping("account/{userId}")
    public ResponseEntity<?>UpdateInfoAccountById(
            @PathVariable long userId,
            @RequestBody UserUpdateRequest userUpdate){
        try {
            BaseResponse baseResponse = new BaseResponse();
            UserResponse userResponse = userService.updateInForUser(userId,userUpdate);
            baseResponse.setStatusCode(200);
            baseResponse.setMessage("Cập nhập thông tin người dùng thành công! " + userId);
            baseResponse.setData(userResponse);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e){
            throw new CustomException(e.getMessage());
        }

    }
    @PutMapping("account/{userId}/change-password")
    public ResponseEntity<?>changePassword(
            @PathVariable long userId,
            @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userId,changePasswordRequest);
        return ResponseEntity.ok("Đổi mật khẩu thành công!");
    }

    @PostMapping("account/{userId}/address")
    public ResponseEntity<?>addNewAddress(
            @Valid
            @PathVariable long userId,
            @RequestBody AddressRequest addressRequest){
        return ResponseEntity.ok(addressService.save(userId,addressRequest));
    }
    @GetMapping("account/{userId}/address")
    public ResponseEntity<?>getListAddressById(@PathVariable long userId){
        Set<AddressResponse> addressList = addressService.findListByUserId(userId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh sách địa chỉ của userId " +userId);
        baseResponse.setData(addressList);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("account/{userId}/address/{addressId}")
    public ResponseEntity<?>findAddressById(
            @PathVariable long userId,
            @PathVariable long addressId
    ){
        return ResponseEntity.ok(addressService.findByUserAndAddress(userId,addressId));
    }

}
