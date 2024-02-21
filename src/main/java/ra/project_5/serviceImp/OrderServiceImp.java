package ra.project_5.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.mapper.MapperOrder;
import ra.project_5.mapper.MapperOrderDetail;
import ra.project_5.model.dto.response.OrderDetailResponse;
import ra.project_5.model.dto.response.OrderHistoryResponse;
import ra.project_5.model.dto.response.OrderResponse;
import ra.project_5.model.entity.EStatus;
import ra.project_5.model.entity.OrderDetail;
import ra.project_5.model.entity.Orders;
import ra.project_5.repository.OrderDetailRepository;
import ra.project_5.repository.OrderRepository;
import ra.project_5.service.OrderService;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MapperOrder mapperOrder;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private MapperOrderDetail mapperOrderDetail;
    @Override
    public List<OrderHistoryResponse> findOrderByUserId(long userId) {
        try {
            List<Orders>ordersList=orderRepository.findByUserOder_Id(userId);
            return ordersList.stream().map(orders -> mapperOrder
                            .EntityToOrderHistory(orders))
                    .collect(Collectors.toList());

        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public List<OrderHistoryResponse> findOrderByUserIdAndStatus(long userId,String status) {
        try {
            List<Orders> ordersList  = orderRepository.findByUserOder_IdAndStatus(userId,EStatus.valueOf(status));
            return ordersList.stream()
                    .map(orders -> mapperOrder.EntityToOrderHistory(orders))
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw  new CustomException("Status not Exist");
        }


    }

    @Override
    public OrderHistoryResponse cancelOrder(long userId, long orderId) {
        /*Orders orders = orderRepository.findByOrderIdAndUserOder_Id(userId,orderId);
        if(orders==null){
            throw new CustomException("Order Not Exist");
        }
        if(orders.getStatus().equals(EStatus.WAITING)){
            orders.getStatus().equals(EStatus.CANCEL);

            return mapperOrder.EntityToOrderHistory(orderRepository.save(orders));
        }else {
            throw new CustomException("Oder can't Cancel By Success");
        }*/

        Orders orders = findByOrderIdAndUserOder_Id(orderId,userId);
        if (orders==null){
            throw new CustomException("Order Not Exist");
        }
        if (orders.getStatus().equals(EStatus.WAITING)){
            orders.setStatus(EStatus.CANCEL);
            return mapperOrder.EntityToOrderHistory(orderRepository.save(orders));
        }else {
            throw new CustomException("Oder can't Cancel By Success");
        }
    }

    @Override
    public Orders findByOrderIdAndUserOder_Id(long orderId, long userId) {
        return orderRepository.findByOrderIdAndUserOder_Id(orderId,userId);
    }

    @Override
    public List<OrderResponse> finAll() {
        List<Orders> ordersList = orderRepository.findAll();
        List<OrderResponse> orderResponseList = ordersList.stream()
                .map(orders -> mapperOrder.mapperEntityToResponse(orders))
                .collect(Collectors.toList());
        return orderResponseList;
    }

    @Override
    public List<OrderResponse> findByOrderStatus(String status) {
        try {
            List<Orders> ordersList  = orderRepository.findByUserOderStatus(EStatus.valueOf(status));
            List<OrderResponse> orderResponseList = ordersList.stream()
                    .map(orders -> mapperOrder.mapperEntityToResponse(orders))
                    .collect(Collectors.toList());
            return orderResponseList;
        }catch (Exception e){
            throw  new CustomException("Status not Exist");
        }
    }

    @Override
    public OrderHistoryResponse updateStatusOrder(long orderId, String status) {
        try {
            Orders orders = findById(orderId);
            EStatus eStatus = stringToEnumStatusValue(status);
            orders.setStatus(eStatus);
            return mapperOrder.EntityToOrderHistory(orderRepository.save(orders));
        } catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public Orders findById(long oderId) {
        Optional<Orders> orders = orderRepository.findById(oderId);
        if (orders.isPresent()){
            return orders.get();
        }else {
            throw new CustomException("Order not Exist");
        }
    }

    @Override
    public List<OrderDetailResponse> getDetailOrderByOrderId(long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(()->new CustomException("id order not found")); // get luôn thì trường hợp không có nó sẽ bào lỗi
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrdersEntity(orders);
        List<OrderDetailResponse> odr = orderDetailList.stream()
                .map(detail -> mapperOrderDetail.mapperEntityToResponse(detail))
                .collect(Collectors.toList());
        return odr;
    }

    @Override
    public BigDecimal dashBoardSaleByTime(Date from, Date to) {
        return orderRepository.getByCreate_at(from,to,EStatus.CONFIRM,EStatus.DELIVERY,EStatus.SUCCESS);
    }


    public EStatus stringToEnumStatusValue(String orderStatus){
        if (orderStatus.equalsIgnoreCase("WAITING")){
            return EStatus.WAITING;
        }
        if (orderStatus.equalsIgnoreCase("CONFIRM")){
            return EStatus.CONFIRM;
        }
        if (orderStatus.equalsIgnoreCase("DELIVERY")){
            return EStatus.DELIVERY;
        }
        if (orderStatus.equalsIgnoreCase("SUCCESS")){
            return EStatus.SUCCESS;
        }
        if (orderStatus.equalsIgnoreCase("CANCEL")){
            return EStatus.CANCEL;
        }
        if (orderStatus.equalsIgnoreCase("DENIED")){
            return EStatus.DENIED;
        }else {
            throw  new CustomException("Status not Exist");
        }

    }
}
