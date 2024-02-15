package ra.project_5.mapper;

import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.CatalogRequest;
import ra.project_5.model.dto.response.CatalogResponse;
import ra.project_5.model.entity.Category;
@Component
public class MapperCatalog implements MapperGeneric<Category, CatalogRequest, CatalogResponse>{
    @Override
    public Category mapperRequestToEntity(CatalogRequest catalogRequest) {
        return Category.builder().categoryName(catalogRequest.getCategoryName())
                .description(catalogRequest.getDescription())
                .status(catalogRequest.isStatus())
                .build();
    }

    @Override
    public CatalogResponse mapperEntityToResponse(Category category) {
        return CatalogResponse.builder()
                .catalogId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .category_status(category.isStatus())
                .build();
    }
}
