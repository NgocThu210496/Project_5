package ra.project_5.controller.permitAll;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.ProductPermitResponse;
import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.dto.response.ProductStatusTrueResponse;
import ra.project_5.repository.ProductRepository;
import ra.project_5.service.ProductService;
import ra.project_5.service.WishListService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/public/")
public class PermitAllProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private WishListService wishListService;

    @GetMapping("products/search")
    public ResponseEntity<?> search(
            @RequestParam(defaultValue = "") String productName) {
        List<ProductResponse> productResponseList = productService.searchProductByName(productName);
        return ResponseEntity.ok(productResponseList);
    }

    /*@GetMapping("products/new-products")
    public ResponseEntity<?>newProduct(){
        Date endDate = new Date();
        LocalDateTime startDateLocalDateTime = LocalDateTime.now().minusDays(1); //test 1 ngay
        Date startDate = java.sql.Timestamp.valueOf(startDateLocalDateTime);
        return ResponseEntity.ok(productService.findNewProductsInLastTwoWeeks(startDate,endDate));
    }*/
    @GetMapping("products/{productId}")
    public ResponseEntity<?> productInfoById(@PathVariable long productId) {
        BaseResponse baseResponse = new BaseResponse();
        ProductResponse productResponse = productService.getInforProductById(productId);
        if (productResponse != null) {
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setMessage("Thông tin sản phẩm theo ID " + productId);
            baseResponse.setData(productResponse);
            return ResponseEntity.ok(baseResponse);
        } else {
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setMessage("Không tồn tại sản phẩm với ID: " + productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
    }

    @GetMapping("/products/new-products")
    public ResponseEntity<?> newProducts() {
        List<ProductPermitResponse> productPermitResponses = productService.listNewProduct();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage("Danh sách 5 sản phẩm  mới nhất.");
        baseResponse.setStatusCode(200);
        baseResponse.setData(productPermitResponses);

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("products/catalogs/{catalogId}")
    public ResponseEntity<?> listProductByCatalogId(@PathVariable long catalogId) {
        BaseResponse baseResponse = new BaseResponse();
        List<ProductResponse> responseList = productService.findListProductByCatalogId(catalogId);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh Sách Sản phẩm theo Danh mục " + catalogId);
        baseResponse.setData(responseList);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("products")
    public ResponseEntity<?> findAllProductStatusTrue(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "productId") String nameDirection,
            @RequestParam(defaultValue = "ASC") String idDirection
    ) {
        BaseResponse baseResponse = new BaseResponse();
        Page<ProductStatusTrueResponse> productResponses = productService.getProductStatusTrue(page, size, nameDirection, idDirection);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("totalPage: " + productResponses.getTotalPages() + " totalProduct: " + productResponses.getTotalElements());
        baseResponse.setData(productResponses.getContent());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("featured-products")
    public ResponseEntity<?> featuredProducts() {
        BaseResponse baseResponse = new BaseResponse();
        // Lấy danh sách sản phẩm nổi bật từ wishListService
        List<ProductResponse> productResponseList = wishListService.getAllWishLists();
        baseResponse.setMessage("Danh sách sản phẩm nổi bật");
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setData(productResponseList);

        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("products/best-seller-products")
    public ResponseEntity<?> bestSellerProducts() {
       /* BaseResponse baseResponse = new BaseResponse();
        Pageable pageable = PageRequest.of(0, 5);
        List<ProductResponse> productResponseList = productService.findBestSellingProducts(pageable);
        baseResponse.setMessage("Danh sách sản phẩm bán chạy:");
        baseResponse.setStatusCode(HttpStatus.OK.value());
        baseResponse.setData(productResponseList);*/

        Pageable pageable = PageRequest.of(0,5);
        return ResponseEntity.ok(productService.findBestSellingProducts(pageable));
    }


}
