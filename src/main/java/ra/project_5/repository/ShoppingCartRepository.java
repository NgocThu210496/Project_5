package ra.project_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_5.model.entity.Product;
import ra.project_5.model.entity.ShoppingCard;
import ra.project_5.model.entity.User;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCard,Integer> {
    ShoppingCard findByProductAndUser(Product product, User user);
    ShoppingCard findByUser_IdAndProduct_ProductId(long userId, long productId);
    List<ShoppingCard>findByUser_Id(long userId);
}
