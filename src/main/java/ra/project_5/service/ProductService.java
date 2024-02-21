package ra.project_5.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project_5.model.dto.request.ProductRequestAdmin;
import ra.project_5.model.dto.response.ProductPermitResponse;
import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.dto.response.ProductStatusTrueResponse;
import ra.project_5.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<ProductResponse>searchProductByName(String name);
    ProductResponse create(ProductRequestAdmin productRequestAdmin);
   Page<ProductResponse>ProductStatusTrue(int page, int size, String nameDirection,String idDirection);
    Page<ProductStatusTrueResponse> getProductStatusTrue(int page, int size, String nameDirection, String idDirection);

    //List<ProductResponse>findNewProductsInLastTwoWeeks(Date starDate, Date endDate);
    List<ProductPermitResponse>listNewProduct();
    List<ProductResponse>findListProductByCatalogId(long catalogId);
    ProductResponse getInforProductById(long productId);
    Page<ProductResponse> findAll(Pageable pageable);
    ProductResponse updateProduct(ProductRequestAdmin productRequestAdmin,long productId);
    boolean findProductId(long productId);
    boolean isExistProductName(String productName);
    ProductResponse deleteProduct(long productId);
    List<ProductResponse> findBestSellingProducts(Pageable pageable);
    Product findById(long productId);

}
