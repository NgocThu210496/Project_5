package ra.project_5.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.WishListRequest;
import ra.project_5.model.dto.response.WishListResponse;
import ra.project_5.model.entity.Product;
import ra.project_5.model.entity.User;
import ra.project_5.model.entity.WishList;
import ra.project_5.repository.ProductRepository;
import ra.project_5.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapperWishList implements MapperGeneric<WishList, WishListRequest, WishListResponse>{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository usersRepository;
    @Override
    public WishList mapperRequestToEntity(WishListRequest wishListRequest) {
        User users = usersRepository.findById(wishListRequest.getUserId()).get();
        //
        Product products = productRepository.findById(wishListRequest.getProductId()).get();
//        List<Products> productsList = new ArrayList<>();
//        productsList.add(products);

        return WishList.builder()
                .userWish(users)
                .productWish(products)
                .build();
    }

    @Override
    public WishListResponse mapperEntityToResponse(WishList wishList) {
        Product products = wishList.getProductWish();
        List<Product> productsList = new ArrayList<>();
        productsList.add(products);
        return WishListResponse.builder()
                .products(productsList)
                .build();
    }
}
