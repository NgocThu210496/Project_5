package ra.project_5.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.mapper.ProductMapper;
import ra.project_5.model.dto.request.ProductRequestAdmin;
import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.dto.response.ProductStatusTrueResponse;
import ra.project_5.model.entity.Category;
import ra.project_5.model.entity.Product;
import ra.project_5.repository.CategoryRepository;
import ra.project_5.repository.ProductRepository;
import ra.project_5.service.CategoryService;
import ra.project_5.service.ProductService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Override
    public List<ProductResponse> searchProductByName(String name) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        List<Product> productsList = productRepository.searchProductsByProductName(name);

        if (productsList != null && !productsList.isEmpty()) {
            productResponseList = productsList.stream()
                    .map(products -> productMapper.mapperEntityToResponse(products))
                    .collect(Collectors.toList());
        }

        return productResponseList;
    }

    @Override
    public ProductResponse create(ProductRequestAdmin productRequestAdmin) {
        return productMapper.mapperEntityToResponse(productRepository.save(productMapper
                .mapperRequestToEntity(productRequestAdmin)));
    }

    @Override
    public Page<ProductResponse> ProductStatusTrue(int page, int size, String nameDirection, String idDirection) {
        return null;
    }
    public Sort.Order sortProduct(String nameDirection, String nameSort){
        Sort.Order order;
        if (nameDirection.equalsIgnoreCase("asc")){
            order = new Sort.Order(Sort.Direction.ASC,nameSort);
        } else {
            order = new Sort.Order(Sort.Direction.DESC,nameSort);
        }
        return  order;
    }
    public Pageable pageableSort (int page, int size, String nameDirection, String idDirection){
        List<Sort.Order> oderList = new ArrayList<>();
        Sort.Order orderId = sortProduct(idDirection,"productId");
        oderList.add(orderId);
        Sort.Order orderName = sortProduct(nameDirection, "productName");
        oderList.add(orderName);
        Pageable pageable = PageRequest.of(page,size,Sort.by(oderList));
        return  pageable;
    }
    @Override
    public Page<ProductStatusTrueResponse> getProductStatusTrue(int page, int size, String nameDirection, String idDirection) {
        try {
           // Pageable pageable = pageableSort(page,size,nameDirection,idDirection);
            Pageable pageable;
            if(idDirection.equals("ASC")){
                pageable= PageRequest.of(page,size,Sort.by(nameDirection).ascending());
            }else {
                pageable= PageRequest.of(page,size,Sort.by(nameDirection).descending());
            }
            Page<Product> list = productRepository.findAllByStatusIsTrue(pageable);

            List<ProductStatusTrueResponse>productResponses = list.getContent()
                    .stream()
                    .map(productEntity ->
                            productMapper.EntityToResponseStatusTrue(productEntity))
                    .collect(Collectors.toList());
            Page<ProductStatusTrueResponse> listProduct = new PageImpl<>(productResponses,pageable, list.getTotalElements());
            return listProduct;
        } catch (Exception e){
            throw new CustomException("Error find All Product");
        }
    }

    @Override
    public List<ProductResponse> listNewProduct() {
        try {
            List<Product> list = productRepository.findTop5ByStatusIsTrueOrderByCreatedDesc11();
            return list.stream().map(product ->productMapper.EntityToResponsePermit(product)).collect(Collectors.toList());
        } catch (Exception e){
            throw new ClassCastException(e.getMessage());
        }
    }

    @Override
    public List<ProductResponse> findListProductByCatalogId(long catalogId) {
        Category category = categoryRepository.findById(catalogId).get();
        List<Product>  productList = productRepository.findByCategoryIs(category);
        List<ProductResponse> productResponseList= productList.stream()
                .map(product -> productMapper.mapperEntityToResponse(product)).collect(Collectors.toList());
        return productResponseList;
    }

    @Override
    public ProductResponse getInforProductById(long productId) {
        Optional<Product> productOp= productRepository.findById(productId);
        return productOp.map(product -> productMapper.mapperEntityToResponse(product)).orElse(null);
    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> productEntityPage = productRepository.findAll(pageable);

        List<Product> productsList = productEntityPage.getContent();

        List<ProductResponse> productResponseList = productsList.stream()
                .map(products -> productMapper.mapperEntityToResponse(products))
                .collect(Collectors.toList());

        var totalProduct = productEntityPage.getTotalElements();

        return new PageImpl<>(productResponseList, pageable, totalProduct);
    }

    @Override
    public ProductResponse updateProduct( ProductRequestAdmin productRequestAdmin,long productId) {
        Optional<Product> productsOptional = productRepository.findById(productId);
        Category category = categoryService.findById(productRequestAdmin.getCategoryId());
        if (productsOptional.isPresent()) {
            Product product = productsOptional.get();
            product.setProductName(productRequestAdmin.getProductName());
            product.setUpdated(new Date());
            product.setDescription(productRequestAdmin.getDescription());
            product.setImage(productRequestAdmin.getImage());
            product.setCategory(category);
            return productMapper.mapperEntityToResponse(productRepository.save(product));
        } else {
            return null;
        }
    }

    @Override
    public boolean findProductId(long productId) {
        Optional<Product> productsOptional = productRepository.findById(productId);
        return productsOptional.isPresent();
    }


    @Override
    public boolean isExistProductName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    @Override
    public ProductResponse deleteProduct(long productId) {
        Optional<Product>productOptional = productRepository.findById(productId);
        if(productOptional.isPresent()){
            productRepository.deleteById(productId);
            return productMapper.mapperEntityToResponse(productOptional.get());
        }else {
            return null;
        }

    }

    @Override
    public List<ProductResponse> findBestSellingProducts(Pageable pageable) {
        List<Product> productsList = productRepository.findBestSellingProducts(pageable);
        List<ProductResponse> productResponseList = productsList.stream()
                .map(products -> productMapper.mapperEntityToResponse(products))
                .collect(Collectors.toList());
        return productResponseList;
    }

    @Override
    public Product findById(long productId) {
        try {
            return productRepository.findById(productId).get();
        } catch (Exception e){
            throw  new CustomException("Product not Exist");
        }
    }

    /*@Override
    public List<ProductResponse> findNewProductsInLastTwoWeeks(Date starDate, Date endDate) {
        List<Product> productList = productRepository.findNewProductsInLastTwoWeeks(starDate,endDate);
        List<ProductResponse> productResponseList = productList.stream()
                .map(product -> productMapper.mapperEntityToResponse(product)).collect(Collectors.toList());
        return productResponseList;
    }*/

}
