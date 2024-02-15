package ra.project_5.service;

import ra.project_5.model.dto.request.ShoppingCartRequest;
import ra.project_5.model.dto.response.ShoppingCartResponse;
import ra.project_5.model.entity.ShoppingCard;

import java.util.List;

public interface ShoppingCartService {
    boolean isExist(long userId, ShoppingCartRequest shoppingCartRequest);
    boolean checkQuantity(long productId,int quantity);
    ShoppingCartResponse addToCart(long userId, ShoppingCartRequest shoppingCartRequest);
    ShoppingCard findByUser_IdAndProduct_ProductId(long userId, long productId);
    List<ShoppingCartResponse>listProductInCart(long userId);
}
