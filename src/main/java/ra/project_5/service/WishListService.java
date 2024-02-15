package ra.project_5.service;

import ra.project_5.model.dto.response.ProductResponse;
import ra.project_5.model.dto.response.WishListResponse;
import ra.project_5.model.entity.Product;

import java.util.List;

public interface WishListService {
    List<ProductResponse> getAllWishLists();
    WishListResponse addNew(long userId, Product product);
    boolean isExist (long productId,long userId);
    List<ProductResponse>getAllWishList(long userId);
    boolean deleteWishList(long wishListId);
}
