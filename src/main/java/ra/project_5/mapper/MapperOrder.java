package ra.project_5.mapper;

import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.OrderRequest;
import ra.project_5.model.dto.response.OrderHistoryResponse;
import ra.project_5.model.dto.response.OrderResponse;
import ra.project_5.model.entity.Orders;

import java.time.LocalDateTime;

@Component
public class MapperOrder implements MapperGeneric<Orders, OrderRequest, OrderResponse>{
    @Override
    public Orders mapperRequestToEntity(OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderResponse mapperEntityToResponse(Orders orders) {
        return OrderResponse.builder().orderId(orders.getOrderId())
                .orderStatus(orders.getStatus())
                .message(orders.getNote())
                .totalAmount(orders.getPrice())
                .build();
    }
    public OrderHistoryResponse EntityToOrderHistory(Orders orders){
        return OrderHistoryResponse.builder()
                .orderId(orders.getOrderId())
                .serialNumber(orders.getSerialNumber())
                .price(orders.getPrice())
                .status(orders.getStatus())
                .orderAt(orders.getOrderAt())
                .updateDay(LocalDateTime.now())
                .build();
    }
}
