package ra.project_5.service;

import org.hibernate.query.Order;
import ra.project_5.model.dto.response.OrderDetailResponse;
import ra.project_5.model.dto.response.OrderHistoryResponse;
import ra.project_5.model.dto.response.OrderResponse;
import ra.project_5.model.entity.Orders;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {
    List<OrderHistoryResponse>findOrderByUserId(long userId);
    List<OrderHistoryResponse>findOrderByUserIdAndStatus(long userId,String status);
    OrderHistoryResponse cancelOrder(long userId, long orderId);
    Orders findByOrderIdAndUserOder_Id(long orderId, long userId);
    List<OrderResponse>finAll();
    List<OrderResponse> findByOrderStatus(String status);
    OrderHistoryResponse updateStatusOrder(long orderId,String status);
    Orders findById(long oderId);
    List<OrderDetailResponse> getDetailOrderByOrderId(long orderId);
    BigDecimal dashBoardSaleByTime(Date from,Date to);
}
