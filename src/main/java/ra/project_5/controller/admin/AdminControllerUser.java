package ra.project_5.controller.admin;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.UserResponse;
import ra.project_5.model.entity.ERoles;
import ra.project_5.model.entity.Roles;
import ra.project_5.model.entity.User;
import ra.project_5.repository.UserRepository;
import ra.project_5.service.RolesService;
import ra.project_5.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminControllerUser {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RolesService rolesService;

    @GetMapping("users")
    public ResponseEntity<Map<String, Object>> getAllUsersPagSort(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable;
        BaseResponse baseResponse = new BaseResponse();
        if ("asc".equals(direction)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "email"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "email"));
        }
        Page<UserResponse> pageUser = userService.findAll(pageable);
        Map<String, Object> data = new HashMap<>();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh sách người dùng");
        baseResponse.setData(pageUser.getContent());
        data.put("dataUsers", baseResponse);
      //  data.put("users", pageUser.getContent());
        data.put("totalUsers", pageUser.getTotalElements());
        data.put("totalPage", pageUser.getTotalPages());

        return ResponseEntity.ok(data);
    }

    @PostMapping("users/{userId}/role")
    public ResponseEntity<?>addRoleForUser(@PathVariable long userId){
        boolean result= userService.findUserId(userId);// kiem tra co nguoi dung nao co userId tuong ung khong
        //tạo đối tượng HashSet để lưu trữ các vai trò của role
        Set<Roles> rolesSet = new HashSet<>(); //Set: ngăn chặn việc thêm các phần tử trùng lặp
        if(result){ //k.tra co ton tai userId khong
            //Nếu không tìm thấy, ném ra một ngoại lệ với thông báo lỗi.
            Roles adminRole= rolesService.findByName(ERoles.ROLE_ADMIN)
                    .orElseThrow(()-> new RuntimeException("Error: Role is not found"));
            Roles userRole = rolesService.findByName(ERoles.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found"));
            //Thêm vai trò đã tìm thấy vào danh sách listRoles
            rolesSet.add(adminRole);
            rolesSet.add(userRole);
            //Gọi phương thức addRoleForUser để thêm vai trò cho người dùng
            // với userId và sau đó cập nhật danh sách các vai trò của người dùng
            User user = userService.addRoleRorUser(userId);
            user.setListRoles(rolesSet);
            //Lưu thay đổi và trả về kết quả
            UserResponse userResponse = userService.addRole(user);
            return ResponseEntity.ok(userResponse);

        }else {
            return ResponseEntity.ok("Not found userId");
        }
    }
    @DeleteMapping("users/{userId}/role")
    public ResponseEntity<?>deleteRoleOfUser(@PathVariable long userId){
        boolean result= userService.findUserId(userId);
        Set<Roles> rolesSet = new HashSet<>();
        if(result){
            Roles userRole = rolesService.findByName(ERoles.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found"));
            rolesSet.add(userRole);
            User user = userService.addRoleRorUser(userId);
            user.setListRoles(rolesSet);
            UserResponse userResponse = userService.addRole(user);
            return  ResponseEntity.ok(userResponse);
        }else {
            return  ResponseEntity.ok("Not found userId");
        }
    }

    @GetMapping("roles")
    public ResponseEntity<?>getListRole(){
        List<Roles> rolesList = rolesService.getListRole();
       // return ResponseEntity.ok(rolesList);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh sách quyền");
        baseResponse.setData(rolesList);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("users/search")
    public ResponseEntity<?>searchUserByName(
            @RequestParam String userName){
        List<UserResponse> userResponseList = userService.findByFullNameContaining(userName);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(userResponseList);
        baseResponse.setStatusCode(200);
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }
}
