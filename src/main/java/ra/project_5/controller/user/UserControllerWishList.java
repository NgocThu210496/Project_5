package ra.project_5.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.entity.Product;
import ra.project_5.repository.ProductRepository;
import ra.project_5.sercurity.principle.CustomUserDetail;
import ra.project_5.service.WishListService;

@RestController
@RequestMapping("/api/v1/user/")
public class UserControllerWishList {
    @Autowired
    private WishListService wishListService;
    @Autowired
    private ProductRepository productRepository;


    @PostMapping("wish-list")
    public ResponseEntity<?> addProductInWishList(@RequestParam long productId) {
        long userId = returnUserIdAuthentication();
        boolean isExist = wishListService.isExist(productId, userId);

        if (!isExist) {
            // Nếu sản phẩm không tồn tại trong danh sách yêu thích, thêm mới
            Product product = productRepository.findById(productId).orElse(null);

            if (product != null) {
                wishListService.addNew(userId, product);

                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setStatusCode(HttpStatus.OK.value());
                baseResponse.setMessage("Thêm mới sản phẩm vào danh sách yêu thích thành công");
                baseResponse.setData(true);

                return ResponseEntity.ok(baseResponse);
            } else {
                // Nếu productId không tồn tại, trả về thông báo lỗi
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
                baseResponse.setMessage("Không tồn tại sản phẩm với ID: " + productId);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
            }
        } else {
            // Nếu sản phẩm đã tồn tại trong danh sách yêu thích, trả về thông báo đã tồn tại
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setMessage("Sản phẩm đã tồn tại trong danh sách yêu thích");
            baseResponse.setData(true);

            return ResponseEntity.ok(baseResponse);
        }
    }

    @GetMapping("wish-list")
    public ResponseEntity<?> getAllWishList(){
        long userId = returnUserIdAuthentication();
        return ResponseEntity.ok(wishListService.getAllWishList(userId));
    }

    @DeleteMapping("wish-list/{wishListId}")
    public ResponseEntity<?> deleteWishList(@PathVariable long wishListId){
        long userId = returnUserIdAuthentication();
        // Gọi phương thức xóa WishList và lấy kết quả
        boolean result = wishListService.deleteWishList(wishListId);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(HttpStatus.OK.value());
        // Kiểm tra kết quả và thiết lập thông điệp phù hợp
        if (result) {
            baseResponse.setMessage("Xoá thành công!");
        } else {
            baseResponse.setMessage("Không tìm thấy hoặc không thể xoá WishList với ID: " + wishListId);
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        }
        baseResponse.setData(result);
        // Trả về ResponseEntity với baseResponse
        return ResponseEntity.ok(baseResponse);


    }

    public static long returnUserIdAuthentication(){
        // Lấy thông tin xác thực từ SecurityContextHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Ép kiểu principal về CustomUserDetail để có thể truy cập thông tin người dùng
        CustomUserDetail customUserDetail = (CustomUserDetail) principal;
        // Trả về ID của người dùng
        return customUserDetail.getId();
        /*return ((CustomUserDetail) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getId();*/
    }
}
