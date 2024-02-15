package ra.project_5.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.UserRequest;
import ra.project_5.model.dto.response.UserResponse;
import ra.project_5.model.entity.User;
@Component
public class MapperUser implements MapperGeneric<User, UserRequest, UserResponse>{
    @Override
    public User mapperRequestToEntity(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserResponse mapperEntityToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .listRoles(user.getListRoles())
                .build();
    }
}
