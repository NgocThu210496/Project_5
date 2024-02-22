package ra.project_5.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_5.model.dto.request.SaleRequest;
import ra.project_5.model.dto.response.BaseResponse;
import ra.project_5.model.dto.response.DashboardSaleByCatalogResponse;
import ra.project_5.model.dto.response.SaleResponse;
import ra.project_5.repository.ProductRepository;
import ra.project_5.service.OrderService;
import ra.project_5.service.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminControllerDashboard {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping ("dash-board/sales")
    public ResponseEntity<?> dashBoardSaleByTime(@RequestBody SaleRequest saleRequest) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Doanh thu bán hàng");
        baseResponse.setData(orderService.dashBoardSaleByTime(saleRequest.getFrom(),saleRequest.getTo()));
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
       // return ResponseEntity.ok(orderService.dashBoardSaleByTime(saleRequest.getFrom(),saleRequest.getTo()));
    }

    @GetMapping("dash-board/sales/best-seller-products")
    public ResponseEntity<?> bestSellerProductsByMonth() {
        BaseResponse baseResponse = new BaseResponse();
        List<Object[]>objects  = productRepository.findBestSellingProducts();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Danh sách những sản phẩm bán chạy trong tháng 2");
        baseResponse.setData(objects);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);

       // return ResponseEntity.ok(productRepository.findBestSellingProducts());
    }

    @GetMapping("dash-board/sales/catalog")
    public ResponseEntity<?> dashBoardSaleByCatalog() {
        List<Object[]> objects =productRepository.findSaleCategory();
        List<DashboardSaleByCatalogResponse> responseList = new ArrayList<>();

        for (Object[] obj : objects) {
            String categoryName = (String) obj[0];
            BigDecimal totalQuantity = (BigDecimal) obj[1];

            DashboardSaleByCatalogResponse response = new DashboardSaleByCatalogResponse(categoryName, totalQuantity);
            responseList.add(response);
        }
        return ResponseEntity.ok(responseList);
    }

}
