package ra.project_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_5.model.entity.Product;
import ra.project_5.model.entity.WishList;

import java.util.List;
@Repository
public interface WishListRepository extends JpaRepository<WishList,Long> {
    @Query("select p from Product p where p.productId in (select w.productWish.productId from WishList w )")
    List<Product> getAllWishLists();
    @Query("select w from WishList w where w.productWish.productId = :productId and w.userWish.id = :userId")
    WishList findByProduct(long productId, long userId);


    @Query("select p from Product p where p.productId in (select w.productWish.productId from WishList w where w.userWish.id = :userId)")
    List<Product> findAllByUserWishId(long userId);
}
