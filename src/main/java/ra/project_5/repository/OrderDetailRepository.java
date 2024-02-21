package ra.project_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_5.model.entity.OrderDetail;
import ra.project_5.model.entity.Orders;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    // chỉ cần findAllBy gì đấy là nó trả về List<OrderDetail> mà vì mình đang gọi repository của orderdetail để làm việc
    //done.ok thay
    List<OrderDetail> findAllByOrdersEntity(Orders orders);
}
