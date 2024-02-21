package ra.project_5.repository;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.project_5.model.dto.request.OrderRequest;
import ra.project_5.model.entity.EStatus;
import ra.project_5.model.entity.Orders;
import ra.project_5.model.entity.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders>findByUserOder_Id(long userId);
    @Query("select o from Orders o where (:status IS NULL OR o.status =:status)")
    List<Orders>findByUserOderStatus(@Param("status")EStatus status);

    List<Orders> findByUserOder_IdAndStatus(long userId, EStatus eStatus);
    Orders findByOrderIdAndUserOder_Id(long userId,long orderId);
    @Query("SELECT sum(o.price) FROM Orders o WHERE (o.status in (:confirm,:del,:success)) and (o.created between :from and :to)")
    BigDecimal getByCreate_at(Date from, Date to, EStatus confirm, EStatus del, EStatus success);
}
