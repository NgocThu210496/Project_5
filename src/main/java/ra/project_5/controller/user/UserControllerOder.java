package ra.project_5.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.OrderHistoryResponse;
import ra.project_5.service.OrderService;
import ra.project_5.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/")
public class UserControllerOder {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @GetMapping("account/{userId}/history")
    public ResponseEntity<?> getListHistoryOrder(@PathVariable long userId) {
        BaseResponse baseResponse = new BaseResponse();
        List<OrderHistoryResponse> orderResponses = orderService.findOrderByUserId(userId);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Lịch sử mua hàng");
        baseResponse.setData(orderResponses);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("{userId}/history")
    public ResponseEntity<?> findOrderByUserId(
            @PathVariable long userId,
            @RequestParam String status

    ){
        BaseResponse baseResponse = new BaseResponse();
        List<OrderHistoryResponse> orderResponses = orderService.findOrderByUserIdAndStatus(userId,status);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh sách đơn hàng: "+status);
        baseResponse.setData(orderResponses);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping("account/{userId}/history/{orderId}/cancel")
    public ResponseEntity<?>cancelOrder(
            @PathVariable long userId,
            @PathVariable long orderId
    ){
        BaseResponse baseResponse = new BaseResponse();
        OrderHistoryResponse orderResponses = orderService.cancelOrder(userId,orderId);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Chuyển đổi trạng thái thành công!");
        baseResponse.setData(orderResponses);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
