package ra.project_5.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.mapper.ShoppingMapper;
import ra.project_5.model.dto.request.ShoppingCartRequest;
import ra.project_5.model.dto.request.ShoppingCartUpdateQuantityRequest;
import ra.project_5.model.dto.response.ShoppingCartResponse;
import ra.project_5.model.entity.Product;
import ra.project_5.model.entity.ShoppingCard;
import ra.project_5.model.entity.User;
import ra.project_5.repository.ProductRepository;
import ra.project_5.repository.ShoppingCartRepository;
import ra.project_5.repository.UserRepository;
import ra.project_5.service.ProductService;
import ra.project_5.service.ShoppingCartService;
import ra.project_5.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ShoppingMapper shoppingMapper;
    @Autowired
    private UserService userService;
    @Override
    public boolean isExist(long userId, ShoppingCartRequest shoppingCartRequest) {
        Product product= productRepository.findById(shoppingCartRequest.getProductId()).get();
        User user = userRepository.findById(userId).get();
        ShoppingCard shoppingCard = shoppingCartRepository.findByProductAndUser(product,user);
        return shoppingCard !=null;
    }

    @Override
    public boolean checkQuantity(long productId, int quantity) {
        Product products = productRepository.checkQuantity(productId, quantity);
        return products != null;
    }

    @Override
    public ShoppingCartResponse addToCart(long userId, ShoppingCartRequest shoppingCartRequest) {

        ShoppingCard shoppingCard = findByUser_IdAndProduct_ProductId(userId, shoppingCartRequest.getProductId());
        if (shoppingCard!=null){
            shoppingCard.setQuantity(shoppingCard.getQuantity()+ shoppingCartRequest.getQuantity());
            shoppingCartRepository.save(shoppingCard);
            return shoppingMapper.mapperEntityToResponse(shoppingCard);
        }
        ShoppingCard shoppingCard1 = shoppingCard(userId);
        Product product = productService.findById(shoppingCartRequest.getProductId());
        shoppingCard.setQuantity(shoppingCartRequest.getQuantity());
        shoppingCard.setProduct(product);
        return shoppingMapper.mapperEntityToResponse(shoppingCartRepository.save(shoppingCard));
    }

    @Override
    public ShoppingCard findByUser_IdAndProduct_ProductId(long userId, long productId) {
        if (userService.findUserById(userId)==null){
            throw new CustomException("User not Exist");
        }
        if (productService.findById(productId)==null){
            throw new CustomException("Product not Exist");
        }
        return shoppingCartRepository.findByUser_IdAndProduct_ProductId(userId,productId);
    }

    @Override
    public List<ShoppingCartResponse> listProductInCart(long userId) {
        userService.findUserById(userId);
        List<ShoppingCard> shoppingCardList = shoppingCartRepository.findByUser_Id(userId);
        return shoppingCardList.stream()
                .map(shoppingCard -> shoppingMapper.mapperEntityToResponse(shoppingCard))
                .collect(Collectors.toList());
    }

    @Override
    public ShoppingCartResponse update(long userId, int cartItemId, ShoppingCartUpdateQuantityRequest cartRequest) {
        try {
            ShoppingCard shoppingCard = shoppingCartRepository.findByUserIdAndCartId(userId,cartItemId);
            shoppingCard.setQuantity(cartRequest.getQuantity());
            shoppingCartRepository.save(shoppingCard);
            return shoppingMapper.mapperEntityToResponse(shoppingCard);
        } catch (Exception e){
            throw  new CustomException(e.getMessage());
        }
    }

    public ShoppingCard shoppingCard(long userId){
        ShoppingCard shoppingCard = new ShoppingCard();
        User user = userService.findUserById(userId);
        shoppingCard.setUser(user);
        return shoppingCard;
    }
}

