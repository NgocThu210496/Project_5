package ra.project_5.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_5.model.entity.Category;
import ra.project_5.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p where p.productName like %:name% or p.description like %:name%")
    List<Product>searchProductsByProductName(String name);
    Page<Product>findAllByStatusIsTrue(Pageable pageable);
    @Query("select p from Product p ORDER BY p.created desc limit 5")
    List<Product>findTop5ByStatusIsTrueOrderByCreatedDesc11();
    List<Product> findByCategoryIs(Category category);
    boolean existsByProductName(String productName);

   @Query("SELECT od.productEntity FROM OrderDetail od GROUP BY od.productEntity.productId ORDER BY SUM(od.quantity) DESC")
    List<Product> findBestSellingProducts(Pageable pageable);

    @Query("select p from Product p where p.productId = :productId and p.quantity >= :stockQuantity")
    Product checkQuantity(long productId,int stockQuantity);

    @Query("SELECT od.productEntity,SUM(od.quantity) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.ordersEntity o " +
            "WHERE MONTH(o.created) = MONTH(CURRENT_DATE) AND YEAR(o.created) = YEAR(CURRENT_DATE) and o.status = 'SUCCESS'" +
            "GROUP BY od.productEntity.productId " +
            "ORDER BY totalQuantity DESC limit 3")
    List<Object[]> findBestSellingProducts();

    @Query("SELECT c.categoryName, SUM(od.price) as totalQuantity " +
            "FROM OrderDetail od " +
            "JOIN od.productEntity p " +
            "JOIN p.category c " +
            "WHERE od.ordersEntity.status = 'SUCCESS'" +
            "GROUP BY c.categoryName " +
            "ORDER BY totalQuantity DESC")
    List<Object[]>findSaleCategory();



}
