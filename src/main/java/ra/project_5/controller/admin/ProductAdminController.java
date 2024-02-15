package ra.project_5.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.model.dto.request.ProductRequestAdmin;
import ra.project_5.model.dto.request.ProductRequestPermitAll;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.entity.Product;
import ra.project_5.service.CategoryService;
import ra.project_5.service.ProductService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/")
public class ProductAdminController {
    @Autowired
    private ProductService productService;
    @PostMapping("products")
    public ResponseEntity<?> AddNewProducts(@RequestBody ProductRequestAdmin productRequestAdmin) {
        BaseResponse baseResponse = new BaseResponse();

        // Kiểm tra null của productResponse
        ProductResponse productResponse = productService.create(productRequestAdmin);
        if (productResponse != null) {
            baseResponse.setStatusCode(HttpStatus.CREATED.value()); // Sử dụng mã trạng thái HTTP 201 Created
            baseResponse.setMessage("Thêm mới sản phẩm thành công!");
            baseResponse.setData(productResponse);
            return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
        } else {
            baseResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Có lỗi xảy ra khi thêm mới sản phẩm.");
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //return ResponseEntity.ok(productResponse);
    }

    @GetMapping("products")
    public ResponseEntity<Map<String, Object>> getAllProductPagSort(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable;
        if ("asc".equals(direction)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "productName"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "productName"));
        }
        Page<ProductResponse> pageProduct = productService.findAll(pageable);
        Map<String, Object> data = new HashMap<>();

        data.put("products", pageProduct.getContent());
        data.put("totalProduct", pageProduct.getTotalElements());
        data.put("totalPage", pageProduct.getTotalPages());
        return ResponseEntity.ok(data);
    }

    @GetMapping("products/{productId}")
    public ResponseEntity<?> productInfoById(@PathVariable long productId){
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

    @PutMapping("products/{productId}")
    public ResponseEntity<?>updateProduct(
            @PathVariable long productId,
            @RequestBody ProductRequestAdmin productRequestAdmin){
        BaseResponse baseResponse = new BaseResponse();

        boolean productExists = productService.findProductId(productId);
        if (productExists) {
            ProductResponse productResponse = productService.updateProduct(productRequestAdmin, productId);
            if (productResponse != null) {
                baseResponse.setStatusCode(HttpStatus.OK.value());
                baseResponse.setMessage("Đã cập nhật sản phẩm thành công!");
                baseResponse.setData(productResponse);
                return ResponseEntity.ok(baseResponse);
            } else {
                baseResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                baseResponse.setMessage("Có lỗi xảy ra khi cập nhật sản phẩm.");
                return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setMessage("Không tìm thấy sản phẩm với ID: " + productId);
            return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("products/{productId}")
    public ResponseEntity<?>deleteProduct(@PathVariable long productId){
        BaseResponse baseResponse = new BaseResponse();
        ProductResponse isDeleted = productService.deleteProduct(productId);

        if (isDeleted != null) {
            baseResponse.setStatusCode(200);
            baseResponse.setMessage("Đã xoá sản phẩm thành công");
            baseResponse.setData(isDeleted);
            return ResponseEntity.ok(baseResponse);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm với ID: " + productId);

    }
}
