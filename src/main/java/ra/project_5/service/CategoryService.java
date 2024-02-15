package ra.project_5.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project_5.model.dto.request.CatalogRequest;
import ra.project_5.model.dto.response.CatalogResponse;
import ra.project_5.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllByStatusIsTrue();
    Category findById(long categoryId);
    Page<CatalogResponse> findAll(Pageable pageable);
    CatalogResponse getInForCategoryId(long categoryId);
    CatalogResponse create(CatalogRequest catalogRequest);
    boolean findCategoryId(long categoryId);
    CatalogResponse update(CatalogRequest catalogRequest, long catalogId);
    boolean delete(long categoryId);
}
