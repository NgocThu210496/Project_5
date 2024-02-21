package ra.project_5.mapper;

import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.OrderDetailRequest;
import ra.project_5.model.dto.response.OrderDetailResponse;
import ra.project_5.model.entity.OrderDetail;
@Component
public class MapperOrderDetail implements MapperGeneric<OrderDetail, OrderDetailRequest, OrderDetailResponse>{
    @Override
    public OrderDetail mapperRequestToEntity(OrderDetailRequest orderDetailRequest) {
        return null;
    }

    @Override
    public OrderDetailResponse mapperEntityToResponse(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .productId(orderDetail.getProductEntity().getProductId())
                .productName(orderDetail.getName())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
