package ra.project_5.controller.permitAll;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.project_5.model.entity.Category;
import ra.project_5.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/")
public class PermitAllCatalogController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("catalog")
    public ResponseEntity<?>findCategoryStatusIsTrue(){
        // return new ResponseEntity<>(categoryService.findAllByStatusIsTrue(), HttpStatus.OK);
        List<Category> categoryList = categoryService.findAllByStatusIsTrue();
        return ResponseEntity.ok(categoryList);
    }
}
