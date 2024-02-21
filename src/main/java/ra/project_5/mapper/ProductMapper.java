package ra.project_5.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.ProductRequestAdmin;
import ra.project_5.model.dto.request.ProductRequestPermitAll;
import ra.project_5.model.dto.response.ProductPermitResponse;
import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.dto.response.ProductStatusTrueResponse;
import ra.project_5.model.entity.Product;
import ra.project_5.repository.CategoryRepository;
import ra.project_5.repository.ProductRepository;
import ra.project_5.service.CategoryService;

import java.util.Date;

@Component
public class ProductMapper implements MapperGeneric<Product, ProductRequestPermitAll, ProductResponse> {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoriesRepository;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product mapperRequestToEntity(ProductRequestPermitAll productRequestPermitAll) {
     //   Category category =categoryService.findEntityById(productRequest.getCategoryId());
        return  Product.builder()
                .productName(productRequestPermitAll.getProductName())
                .category(categoriesRepository.findById(productRequestPermitAll.getCategoryId()).get())
                .description(productRequestPermitAll.getDescription())
                .unitPrice(productRequestPermitAll.getUnitPrice())
                .quantity(productRequestPermitAll.getQuantity())
                .image(productRequestPermitAll.getImage())
                .created(new Date())
                .updated(new Date())
                .status(true)
                .build();
    }

    @Override
    public ProductResponse mapperEntityToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .categoryName(product.getCategory().getCategoryId())
               // .sku(product.getSku())
                .image(product.getImage())
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .quantity(product.getQuantity())
                .status(product.isStatus())
                .created(product.getCreated())
                .updated(product.getUpdated())
                .build();

    }

    public Product mapperRequestToEntity(ProductRequestAdmin productRequestAdmin) {
        //   Category category =categoryService.findEntityById(productRequest.getCategoryId());
        return  Product.builder()
                .productName(productRequestAdmin.getProductName())
                .category(categoriesRepository.findById(productRequestAdmin.getCategoryId()).get())
                .description(productRequestAdmin.getDescription())
               // .sku(productRequestAdmin.get)
                .unitPrice(productRequestAdmin.getUnitPrice())
                .quantity(productRequestAdmin.getQuantity())
                .image(productRequestAdmin.getImage())
                .created(new Date())
                .updated(new Date())
                .status(true)
                .build();
    }
    public ProductResponse EntityToResponsePermit(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .categoryName(product.getCategory().getCategoryId())
               // .sku(product.getSku())
                .productName(product.getProductName())
                .image(product.getImage())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .quantity(product.getQuantity())
                .status(product.isStatus())
                .created(product.getCreated())
                .build();
    }
    public ProductStatusTrueResponse EntityToResponseStatusTrue(Product product) {
        return ProductStatusTrueResponse.builder()
                .productId(product.getProductId())
                .sku(product.getSku())
                .productName(product.getProductName())
                .description(product.getDescription())
                .status(product.isStatus())
                .build();
    }

    public ProductPermitResponse EntityToResponsePermit1(Product productEntity) {
        return ProductPermitResponse.builder()
                .productId(productEntity.getProductId())
                .categoryName(productEntity.getCategory().getCategoryName())
                .sku(productEntity.getSku())
                .productName(productEntity.getProductName())
                .description(productEntity.getDescription())
                .unitPrice(productEntity.getUnitPrice())
                .quantity(productEntity.getQuantity())
                .status(productEntity.isStatus())
                .image(productEntity.getImage())
                .build();
    }
}
