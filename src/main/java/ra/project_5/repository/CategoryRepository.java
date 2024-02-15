package ra.project_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_5.model.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
   // @Query("select c from Category c where c.category_status = true ")
    List<Category>findAllByStatusIsTrue();
}
