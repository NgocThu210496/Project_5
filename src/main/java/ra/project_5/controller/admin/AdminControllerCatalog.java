package ra.project_5.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.request.CatalogRequest;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.CatalogResponse;
import ra.project_5.service.CategoryService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminControllerCatalog {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("categories")
    public ResponseEntity<?> getAllCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "asc") String direction)
    {
        Pageable pageable;
        if ("asc".equals(direction)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "categoryName"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "categoryName"));
        }
        Page<CatalogResponse> pageCatalog = categoryService.findAll(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("totalCatalog", pageCatalog.getTotalElements());
        data.put("totalPage", pageCatalog.getTotalPages());
        data.put("catalog", pageCatalog.getContent());

        return ResponseEntity.ok(data);
    }

    @GetMapping("categories/{categoryId}")
    public ResponseEntity<?>getInForCategoryId(@PathVariable long categoryId){
        BaseResponse baseResponse = new BaseResponse();
        CatalogResponse catalogResponse = categoryService.getInForCategoryId(categoryId);
        if (catalogResponse != null) {
            baseResponse.setStatusCode(HttpStatus.OK.value());
            baseResponse.setMessage("Thông tin danh mục " + categoryId);
            baseResponse.setData(catalogResponse);
            return ResponseEntity.ok(baseResponse);
        } else {
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setMessage("Không tồn tại danh mục với ID: " + categoryId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
    }

    @PostMapping("categories")
    public ResponseEntity<?>createCategory(@RequestBody CatalogRequest catalogRequest){
        BaseResponse baseResponse = new BaseResponse();
        CatalogResponse catalogResponse = categoryService.create(catalogRequest);
        if (catalogResponse != null) {
            baseResponse.setStatusCode(HttpStatus.CREATED.value());
            baseResponse.setMessage("Thêm mới danh mục thành công!");
            baseResponse.setData(catalogResponse);
            return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
        } else {
            baseResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Có lỗi xảy ra khi thêm mới danh mục.");
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("categories/{categoryId}")
    public ResponseEntity<?>updateCategory(@RequestBody CatalogRequest catalogRequest, @PathVariable long categoryId){
        BaseResponse baseResponse = new BaseResponse();

        boolean categoryExists = categoryService.findCategoryId(categoryId);

        if (categoryExists) {
            CatalogResponse catalogResponse = categoryService.update(catalogRequest, categoryId);

            if (catalogResponse != null) {
                baseResponse.setStatusCode(HttpStatus.OK.value());
                baseResponse.setMessage("Cập nhật danh mục thành công!");
                baseResponse.setData(catalogResponse);
                return ResponseEntity.ok(baseResponse);
            } else {
                baseResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                baseResponse.setMessage("Có lỗi xảy ra khi cập nhật danh mục!");
                return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            baseResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            baseResponse.setMessage("Không tìm thấy danh mục với ID: " + categoryId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponse);
        }
    }

    @DeleteMapping("categories/{categoryId}")
    public ResponseEntity<?>DeleteCategory(@PathVariable long categoryId){
        BaseResponse baseResponse = new BaseResponse();
        boolean isSuccess = categoryService.delete(categoryId);
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Xoá danh mục(Đổi trạng thái)");
        baseResponse.setData(isSuccess);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
