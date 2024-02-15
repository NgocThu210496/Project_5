package ra.project_5.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project_5.model.dto.request.ChangePasswordRequest;
import ra.project_5.model.dto.request.SignInRequest;
import ra.project_5.model.dto.request.SignUpRequest;
import ra.project_5.model.dto.request.UserUpdateRequest;
import ra.project_5.model.dto.response.SignInResponse;
import ra.project_5.model.dto.response.SignUpResponse;
import ra.project_5.model.dto.response.UserResponse;
import ra.project_5.model.entity.User;

import java.util.List;

public interface UserService {
    SignUpResponse register(SignUpRequest signUpRequest);
    SignInResponse login(SignInRequest signInRequest);
   List<UserResponse> findAll();
    UserResponse getInForUser(long userId);
    UserResponse updateInForUser(long userId, UserUpdateRequest userUpdateRequest);
    Long changePassword(long userId, ChangePasswordRequest changePasswordRequest);
    Page<UserResponse>findAll(Pageable pageable);
    boolean findUserId(long userId);
    User addRoleRorUser(long userId);
    UserResponse addRole(User user);
    List<UserResponse>findByFullNameContaining(String name);
    User findUserById(long id);



}
