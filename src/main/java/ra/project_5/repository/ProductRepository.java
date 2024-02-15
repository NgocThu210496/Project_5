package ra.project_5.repository;

import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.entity.Category;
import ra.project_5.model.entity.Product;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p where p.productName like %:name% or p.description like %:name%")
    List<Product>searchProductsByProductName(String name);
    Page<Product>findAllByStatusIsTrue(Pageable pageable);

    //Danh sach sp moi: trong 2 tuan
   /* @Query("select p from Product p where p.created >= :startDate AND p.created <=: endDate")
    List<Product>findNewProductsInLastTwoWeeks(@Param("startDate") Date starDate,@Param("endDate") Date endDate);
*/
    @Query("select p from Product p ORDER BY p.created desc limit 2")
    List<Product>findTop5ByStatusIsTrueOrderByCreatedDesc11();
    List<Product> findByCategoryIs(Category category);
    boolean existsByProductName(String productName);

   @Query("SELECT od.productEntity FROM OrderDetail od GROUP BY od.productEntity ORDER BY SUM(od.quantity) DESC")
    List<Product> findBestSellingProducts(Pageable pageable);

    @Query("select p from Product p where p.productId = :productId and p.quantity >= :stockQuantity")
    Product checkQuantity(long productId,int stockQuantity);

}
