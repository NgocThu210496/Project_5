package ra.project_5.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.request.ShoppingCartRequest;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.ShoppingCartResponse;
import ra.project_5.service.ShoppingCartService;
import ra.project_5.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/")
public class UserControllerShoppingCart {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @PostMapping("shopping-cart/{userId}/add")
    public ResponseEntity<?>addNewProductInCart(
            @PathVariable long userId,
            @RequestBody ShoppingCartRequest shoppingCartRequest
            ){

        BaseResponse baseResponse = new BaseResponse();
        ShoppingCartResponse cartResponse = shoppingCartService.addToCart(userId,shoppingCartRequest);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Thêm sản phẩm vào giỏ hàng");
        baseResponse.setData(cartResponse);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("shopping-cart/{userId}")
    public ResponseEntity<?> findListProductByUserId(@PathVariable long userId){
// Kiểm tra xem userId có tồn tại hay không
        boolean userExists = userService.findUserId(userId);
        if (!userExists) {
            // Nếu userId không tồn tại, trả về ResponseEntity với mã trạng thái NOT FOUND và thông báo lỗi
            BaseResponse errorResponse = new BaseResponse();
            errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            errorResponse.setMessage("Không tìm thấy người dùng với ID: " + userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        // Nếu userId tồn tại, tiếp tục lấy danh sách sản phẩm trong giỏ hàng
        BaseResponse successResponse = new BaseResponse();
        List<ShoppingCartResponse> responseList = shoppingCartService.listProductInCart(userId);
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setMessage("Danh sách sản phẩm trong giỏ hàng");
        successResponse.setData(responseList);
        // Trả về ResponseEntity với danh sách sản phẩm hoặc mã trạng thái NOT FOUND nếu có lỗi khác
        return ResponseEntity.ok(successResponse);
    }
}
