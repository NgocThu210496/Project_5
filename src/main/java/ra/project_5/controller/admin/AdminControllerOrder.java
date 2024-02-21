package ra.project_5.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.OrderDetailResponse;
import ra.project_5.model.dto.response.OrderHistoryResponse;
import ra.project_5.model.dto.response.OrderResponse;
import ra.project_5.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminControllerOrder {
    @Autowired
    private OrderService orderService;
    @GetMapping("orders")
    public ResponseEntity<?> getListAllOrders() {
        BaseResponse baseResponse = new BaseResponse();
        List<OrderResponse> orderResponses = orderService.finAll();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh sách tất cả đơn hàng");
        baseResponse.setData(orderResponses);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("orders/{orderStatus}")
    public ResponseEntity<?> getListOrderStatus(@PathVariable String orderStatus) {
        BaseResponse baseResponse = new BaseResponse();
        List<OrderResponse> orderResponses = orderService.findByOrderStatus(orderStatus);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh sách đơn hàng theo trạng thái " + orderStatus);
        baseResponse.setData(orderResponses);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("orders/orderId/{orderId}")
    public ResponseEntity<?> detailOrders(@PathVariable long orderId) {
        BaseResponse baseResponse = new BaseResponse();
        List<OrderDetailResponse> orderDetailResponse = orderService.getDetailOrderByOrderId(orderId);
        baseResponse.setData(orderDetailResponse);
        baseResponse.setMessage("Chi tiết đơn hàng: " + orderId);
        baseResponse.setStatusCode(200);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);

       // return ResponseEntity.ok(orderService.getDetailOrderByOrderId(orderId));
    }

    @PutMapping("orders/{orderId}/status")
    public ResponseEntity<?> updateStatusOrders(
            @PathVariable long orderId,
            @RequestParam String orderStatus
    ){
        BaseResponse baseResponse = new BaseResponse();
        OrderHistoryResponse orderResponses = orderService.updateStatusOrder(orderId,orderStatus);
        baseResponse.setData(orderResponses);
        baseResponse.setMessage("Cập nhập trạng thái đơn hàng thành công!");
        baseResponse.setStatusCode(200);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}

