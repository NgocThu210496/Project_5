package ra.project_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ra.project_5.model.entity.Product;
import ra.project_5.model.entity.ShoppingCard;
import ra.project_5.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCard,Integer> {
    ShoppingCard findByProductAndUser(Product product, User user);
    ShoppingCard findByUserIdAndCartId(long userId, int cartItemId);
    ShoppingCard findByUser_IdAndProduct_ProductId(long userId, long productId);
    List<ShoppingCard>findByUser_Id(long userId);
    @Transactional
    void deleteByUser_IdAndCartId(long userId, int cartId);
    @Transactional
    void deleteAllByUserId(long userId);

}
