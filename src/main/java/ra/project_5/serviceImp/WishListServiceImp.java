package ra.project_5.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project_5.mapper.MapperGeneric;
import ra.project_5.mapper.MapperWishList;
import ra.project_5.mapper.ProductMapper;
import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.dto.response.WishListResponse;
import ra.project_5.model.entity.Product;
import ra.project_5.model.entity.User;
import ra.project_5.model.entity.WishList;
import ra.project_5.repository.UserRepository;
import ra.project_5.repository.WishListRepository;
import ra.project_5.service.WishListService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishListServiceImp implements WishListService {
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MapperWishList mapperWishList;
    @Override
    public List<ProductResponse> getAllWishLists() {
        List<Product> productsList = wishListRepository.getAllWishLists();

        List<ProductResponse> productResponseList = productsList.stream()
                .map(products -> productMapper.mapperEntityToResponse(products))
                .collect(Collectors.toList());
        return productResponseList;
    }

    @Override
    public WishListResponse addNew(long userId, Product product) {
        User user = userRepository.findById(userId).get();
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        WishList wishList = new WishList();
        wishList.setUserWish(user);
        wishList.setProductWish(product);
        return mapperWishList.mapperEntityToResponse(wishListRepository.save(wishList));
    }

    @Override
    public boolean isExist(long productId, long userId) {
        WishList wishList =wishListRepository.findByProduct(productId,userId);
        return wishList !=null;
    }

    @Override
    public List<ProductResponse> getAllWishList(long userId) {
        List<Product> wishListList = wishListRepository.findAllByUserWishId(userId);
        List<ProductResponse> productResponseList = wishListList.stream()
                .map(products -> productMapper.mapperEntityToResponse(products))
                .collect(Collectors.toList());
        return productResponseList;
    }

    @Override
    public boolean deleteWishList(long wishListId) {
        Optional<WishList> wishListOptional = wishListRepository.findById(wishListId);
        if(wishListOptional.isPresent()){
            wishListRepository.deleteById(wishListId);
            return true;
        }
        return false;
    }
}
