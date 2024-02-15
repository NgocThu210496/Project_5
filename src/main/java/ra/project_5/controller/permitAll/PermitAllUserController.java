package ra.project_5.controller.permitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.request.SignInRequest;
import ra.project_5.model.dto.request.SignUpRequest;
import ra.project_5.model.dto.response.SignInResponse;
import ra.project_5.model.dto.response.SignUpResponse;
import ra.project_5.model.dto.response.UserResponse;
import ra.project_5.model.entity.Category;
import ra.project_5.service.CategoryService;
import ra.project_5.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/")
public class PermitAllUserController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @PostMapping("user")
    public ResponseEntity<SignUpResponse> register(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse signUpResponse = userService.register(signUpRequest);
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }

    @PostMapping("user/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
        return new ResponseEntity<>(userService.login(signInRequest), HttpStatus.OK);
    }
/*    @GetMapping("/admin/user")
    public ResponseEntity<List<UserResponse>> findALl(){
        return new ResponseEntity<>(userService.findAll(),HttpStatus.OK);
    }*/

}
