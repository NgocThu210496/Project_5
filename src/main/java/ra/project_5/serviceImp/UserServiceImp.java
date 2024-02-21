package ra.project_5.serviceImp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project_5.advice.exception.BadRequestException;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.advice.exception.UserNotFoundException;
import ra.project_5.mapper.MapperUser;
import ra.project_5.model.dto.request.ChangePasswordRequest;
import ra.project_5.model.dto.request.SignInRequest;
import ra.project_5.model.dto.request.SignUpRequest;
import ra.project_5.model.dto.request.UserUpdateRequest;
import ra.project_5.model.dto.response.SignInResponse;
import ra.project_5.model.dto.response.SignUpResponse;
import ra.project_5.model.dto.response.UserResponse;
import ra.project_5.model.entity.ERoles;
import ra.project_5.model.entity.Roles;
import ra.project_5.model.entity.User;
import ra.project_5.repository.RolesRepository;
import ra.project_5.repository.UserRepository;
import ra.project_5.sercurity.jwt.JwtProvider;
import ra.project_5.sercurity.principle.CustomUserDetail;
import ra.project_5.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MapperUser mapperUser;
    @Override
    public SignUpResponse register(SignUpRequest signUpRequest) {
        Set<Roles> setRoles = new HashSet<>();
        signUpRequest.getListRoles().forEach(role -> {
            //admin, moderator, user
            switch (role) {
                case "admin":
                    setRoles.add(rolesRepository.findByName(ERoles.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Không tồn tại quyền admin")));
                    break;
                /*case "moderator":
                    setRoles.add(rolesRepository.findByName(ERoles.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Không tồn tại quyền admin")));
                    break;*/
                case "user":
                default:
                    setRoles.add(rolesRepository.findByName(ERoles.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Không tồn tại quyền admin")));
            }
        });
        User user = modelMapper.map(signUpRequest, User.class);
        user.setListRoles(setRoles);
        user.setCreated(new Date());
        //Mã hóa mật khẩu khi đăng ký
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //Khi đăng ký, set status mặc định là true
        user.setStatus(true);
        //Thực hiện thêm mới
        User userCreated = usersRepository.save(user);
        SignUpResponse signUpResponse = modelMapper.map(userCreated, SignUpResponse.class);
        //set lại quyền user trả về
        List<String> listUserRoles = new ArrayList<>();
        userCreated.getListRoles().stream().forEach(roles -> {
            listUserRoles.add(roles.getName().name());
        });
        signUpResponse.setListRoles(listUserRoles);
        return signUpResponse;
    }

    @Override
    public SignInResponse login(SignInRequest signInRequest) {
        //1. Lấy User trong db ra và so sánh(mã hóa mật khẩu Bcryt)
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getUserName(), signInRequest.getPassword()));
        } catch (Exception ex) {
            throw new RuntimeException("Username or Password incorrect");
        }
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);
        return SignInResponse.builder()
                .userName(userDetails.getUsername())
                .email(userDetails.getEmail())
                .fullName(userDetails.getFullName())
                .authorities(userDetails.getAuthorities())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public List<UserResponse> findAll() {
        List<User> listUser = usersRepository.findAll();
        List<UserResponse> listUserResponse = listUser.stream()
                .map(users -> modelMapper.map(users, UserResponse.class)).collect(Collectors.toList());
        return listUserResponse;
    }

    @Override
    public UserResponse getInForUser(long userId) {
       return mapperUser.mapperEntityToResponse(usersRepository.findById(userId).get());
    }

    @Override
    public UserResponse updateInForUser(long userId, UserUpdateRequest userUpdateRequest) {
        User user = usersRepository.findById(userId).get();
        user.setFullName(userUpdateRequest.getFullName());
        user.setPhone(userUpdateRequest.getPhone());
        user.setAddress(userUpdateRequest.getAddress());
        user.setUpdated(new Date());
        usersRepository.save(user);
        return mapperUser.mapperEntityToResponse(usersRepository.save(user));
    }

    @Override
    public Long changePassword(long userId, ChangePasswordRequest changePasswordRequest) {
        User user = usersRepository.findById(userId).get();
        // Validate password
        //Kiểm tra xem mật khẩu cũ (được cung cấp trong req.getOldPassword()) có khớp với mật khẩu hiện tại của người dùng hay không
        if (!BCrypt.checkpw(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ không chính xác"); //Nếu không khớp, ném một ngoại lệ BadRequestException.
        }
        // Check if newPassword and confirmNewPassword match
        //kiểm tra xem mật khẩu mới (getNewPassword()) và mật khẩu xác nhận (getConfirmNewPassword()) có giống nhau không
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            //Nếu chúng không giống nhau, một ngoại lệ BadRequestException sẽ được ném
            throw new BadRequestException("Mật khẩu mới và xác nhận mật khẩu mới không giống nhau");
        }

        String hash = BCrypt.hashpw(changePasswordRequest.getNewPassword(), BCrypt.gensalt(12));
        user.setPassword(hash); //BCrypt: số lượng vòng lặp được sử dụng trong quá trình hash mật khẩu
        usersRepository.save(user);
        return null;
    }

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> usersPage = usersRepository.findAll(pageable);

        List<User> usersList = usersPage.getContent();

        List<UserResponse> userResponseList = usersList.stream()
                .map(products -> mapperUser.mapperEntityToResponse(products))
                .collect(Collectors.toList());

        var totalUsers = usersPage.getTotalElements();

        return new PageImpl<>(userResponseList, pageable, totalUsers);
    }

    @Override
    public boolean findUserId(long userId) {
        Optional<User> optionalUser= usersRepository.findById(userId);
        return optionalUser.isPresent();
    }

    @Override
    public User addRoleRorUser(long userId) {
        Optional<User>userOptional=usersRepository.findById(userId); //tim kiem nguoi dung co id tuong ung trong usersRepository
        //Optional có chứa giá trị không
        if(userOptional.isPresent()){ //nghĩa là có tìm thấy người dùng với id hay không
            //neu co id, lấy đối tượng User từ Optional bằng cách s/d ph.thức get, sau đó gán cho biến user
            User user = userOptional.get();
            return user;
        }
        return null;
    }

    @Override
    public UserResponse addRole(User user) {
        return mapperUser.mapperEntityToResponse(usersRepository.save(user));
    }

    @Override
    public List<UserResponse> findByFullNameContaining(String name) {
        try {
            List<UserResponse> userResponseList = usersRepository.findByFullNameContaining(name).stream()
                    .map(user -> mapperUser.mapperEntityToResponse(user)).collect(Collectors.toList());
            return userResponseList;

        }catch (Exception e){
           throw  new CustomException("User not found");

        }
    }

    @Override
    public User findUserById(long id) {
        Optional<User> user= usersRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }else {
            throw new UserNotFoundException("User is not found");
        }
    }

    @Override
    public boolean unlockStatus(long userId) {
        User user = findUserById(userId);
        if (user.isStatus()) {
            user.setStatus(false);
        } else {
            user.setStatus(true);
        }
        usersRepository.save(user);
        return usersRepository.save(user).isStatus();
    }


    public Roles findRoleOfUser(long userId,long roleId) {
        User user= findUserById(userId);
        return user.getListRoles().stream()
                .filter(rolesEntity -> roleId==rolesEntity.getId())
                .findAny().orElse(null);
    }
}
