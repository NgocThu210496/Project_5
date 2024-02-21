package ra.project_5.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.request.OrderRequest;
import ra.project_5.model.dto.request.ShoppingCartRequest;
import ra.project_5.model.dto.request.ShoppingCartUpdateQuantityRequest;
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
        ShoppingCartResponse cartResponse = shoppingCartService.addToCart(userId, shoppingCartRequest);
        BaseResponse successResponse = new BaseResponse();
        successResponse.setStatusCode(HttpStatus.OK.value());
        successResponse.setMessage("Thêm sản phẩm vào giỏ hàng thành công");
        successResponse.setData(cartResponse);

        return ResponseEntity.ok(successResponse);
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

    @PutMapping("shopping-cart/{userId}/update/{cartItemId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable long userId,
            @PathVariable int cartItemId,
            @RequestBody ShoppingCartUpdateQuantityRequest cartRequest){
        BaseResponse baseResponse = new BaseResponse();
        ShoppingCartResponse response = shoppingCartService.update(userId,cartItemId,cartRequest);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Thay đổi số lượng sản phẩm");
        baseResponse.setData(response);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);

    }

    @DeleteMapping("shopping-cart/{userId}/delete/{shoppingCartId}")
    public ResponseEntity<?> deleteByUserIdAndCartId(
            @PathVariable long userId,
            @PathVariable int shoppingCartId) {
        BaseResponse baseResponse = new BaseResponse();
        boolean isSuccess = shoppingCartService.deleteByCartId(userId,shoppingCartId);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Xoá thành công 1 sản phẩm trong giỏ hàng");
        baseResponse.setData(isSuccess);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping("shopping-cart/{userId}/clear")
    public ResponseEntity<?>deleteAllProductInCart(@PathVariable long userId){
        /*BaseResponse baseResponse = new BaseResponse();
        boolean isSuccess = shoppingCartService.deleteAllProductInCart(userId);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Đã xoá tất cả sản phẩm trong giỏ hàng");
        baseResponse.setData(isSuccess);
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);*/
        shoppingCartService.deleteAllProductInCart(userId);
        return ResponseEntity.ok("Đã xoá tất cả sản phẩm trong giỏ hàng của userId " + userId);
    }

    @PostMapping("shopping-cart/{userId}/check-out")
    public ResponseEntity<?>checkOut(@PathVariable long userId, @RequestBody OrderRequest orderRequest){
        shoppingCartService.checkOut(userId,orderRequest);
        return ResponseEntity.ok("Order success !");
    }
}
