package ra.project_5.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ra.project_5.model.dto.request.ShoppingCartRequest;
import ra.project_5.model.dto.response.ShoppingCartResponse;
import ra.project_5.model.entity.Product;
import ra.project_5.model.entity.ShoppingCard;
import ra.project_5.repository.ProductRepository;

@Component
public class ShoppingMapper implements MapperGeneric<ShoppingCard, ShoppingCartRequest, ShoppingCartResponse>{
    @Autowired
    private ProductRepository productRepository;
    @Override
    public ShoppingCard mapperRequestToEntity(ShoppingCartRequest shoppingCartRequest) {
        return null;
    }

    @Override
    public ShoppingCartResponse mapperEntityToResponse(ShoppingCard shoppingCard) {
        Product products = productRepository.findById(shoppingCard.getProduct().getProductId()).get();
        return ShoppingCartResponse.builder()
                .shoppingCartId(shoppingCard.getCartId())
                .product_name(shoppingCard.getProduct().getProductName())
                .sku(products.getSku())
                .description(products.getDescription())
                .unit_price(products.getUnitPrice())
                .image(products.getImage())
                .orderQuantity(shoppingCard.getQuantity())
                .build();
    }
}
