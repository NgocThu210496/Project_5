package ra.project_5.serviceImp;

import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.project_5.advice.exception.CustomException;
import ra.project_5.advice.exception.UserNotFoundException;
import ra.project_5.mapper.ShoppingMapper;
import ra.project_5.model.dto.request.OrderRequest;
import ra.project_5.model.dto.request.ShoppingCartRequest;
import ra.project_5.model.dto.request.ShoppingCartUpdateQuantityRequest;
import ra.project_5.model.dto.response.ShoppingCartResponse;
import ra.project_5.model.entity.*;
import ra.project_5.repository.*;
import ra.project_5.service.ProductService;
import ra.project_5.service.ShoppingCartService;
import ra.project_5.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
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
        shoppingCard1.setQuantity(shoppingCartRequest.getQuantity());
        shoppingCard1.setProduct(product);
        return shoppingMapper.mapperEntityToResponse(shoppingCartRepository.save(shoppingCard1));
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

    @Override
    public boolean deleteByCartId(long userId, int cartId) {
        if(userService.findUserById(userId)==null){
            throw new UserNotFoundException("User not Exist");
        }
        if(shoppingCartRepository.findByUserIdAndCartId(userId,cartId)==null){
            throw new CustomException("Cart in UserEntity not Exist");
        }
        try {
            shoppingCartRepository.deleteByUser_IdAndCartId(userId,cartId);
            return true;
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public void deleteAllProductInCart(long userId) {
        /*try {
            List<ShoppingCartResponse> shoppingCardList = listProductInCart(userId);
            if(shoppingCardList.size()>1){
                throw new CustomException("User not exist");
            }else {
                shoppingCartRepository.deleteAllByUserId(userId);
                return true;
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }*/
        shoppingCartRepository.deleteAllByUserId(userId);

    }

    @Override
    public boolean checkOut(long userId, OrderRequest orderRequest) {
        User user = userRepository.findById(userId).orElseThrow(()->new CustomException("user id not found"));
        Address address = addressRepository.findById(orderRequest.getAddressId()).orElseThrow(()->new CustomException("address id not found"));
        // chuyen doi tu list shopping cart id sang list doi tuong shopping cart de tinh tong tien
        List<ShoppingCard> shoppingCardList = new ArrayList<>();
        for (Integer shoppingCartId:orderRequest.getListCartId()) {
            ShoppingCard shoppingCard = shoppingCartRepository.findById(shoppingCartId).orElseThrow(()->new CustomException("shopping cart id not fond"));
            shoppingCardList.add(shoppingCard);
        }
        double sum = 0;
        for (ShoppingCard shoppingCard:shoppingCardList) {
            sum = sum + (shoppingCard.getQuantity() * shoppingCard.getProduct().getUnitPrice().doubleValue());
        }
        // buoc 1: khoi tao doi tuong order va luu nhung thong tin can thiet
            Orders orders = Orders.builder()
                    .orderAt(LocalDateTime.now())
                    .price(BigDecimal.valueOf(sum))
                    .status(EStatus.WAITING)
                    .note(orderRequest.getNote())
                    .receiveName(address.getReceiveName())
                    .receiveAddress(address.getFullAddress())
                    .receivePhone(address.getPhone())
                    .created(new Date())
                    .received(LocalDate.now().plusDays(4))
                    .userOder(user)
                    .build();
            Orders newOrder = orderRepository.save(orders);
        // buoc 2: chuyen doi shopping cart sang order detail


            // chuyen doi list shopping cart sang list order detail
            for (ShoppingCard shoppingCard:shoppingCardList) {
                OrderDetail orderDetail = OrderDetail.builder()
                        .ordersEntity(newOrder)
                        .productEntity(shoppingCard.getProduct())
                        .name(shoppingCard.getProduct().getProductName())
                        .price(shoppingCard.getProduct().getUnitPrice())
                        .quantity(shoppingCard.getQuantity())
                        .build();
                orderDetailRepository.save(orderDetail);
            }
        // buoc 3: xoa nhung shopping cart da check out thong qua list shopping cart id
        for (Integer shoppingCartId:orderRequest.getListCartId()){
            shoppingCartRepository.deleteById(shoppingCartId);
        }
        // buoc 4: tru so luong product da mua thong qua list doi tuong shopping cart vi trong no co doi tuong product tru di
        for (ShoppingCard shoppingCard:shoppingCardList){
            Product product = shoppingCard.getProduct();
            product.setQuantity(product.getQuantity() - shoppingCard.getQuantity());
            productRepository.save(product);
        }

        /*Address address = addressRepository.findByUserAddr_Id(userId).orElseThrow();
        List<ShoppingCard>shoppingCardList = shoppingCartRepository.findByUser_Id(userId);
        Orders orders= new Orders();
        orders.setUserOder(User.builder().id(userId).build());
        orders.setReceiveName(address.getReceiveName());
        orders.setReceiveAddress(address.getFullAddress());
        orders.setReceivePhone(address.getPhone());
        Orders ordersSave = orderRepository.save(orders);
        List<OrderDetail>orderDetailList= new ArrayList<>();
        for (ShoppingCard shoppingCart:shoppingCardList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrdersEntity(Orders.builder().orderId(ordersSave.getOrderId()).build());
            orderDetail.setProductEntity(Product.builder().productId(shoppingCart.getProduct().getProductId()).build());
            orderDetail.setName(shoppingCart.getProduct().getProductName());
            orderDetail.setQuantity(shoppingCart.getQuantity());
            orderDetail.setPrice(shoppingCart.getProduct().getUnitPrice());
            orderDetailList.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetailList);
        shoppingCartRepository.deleteAll(shoppingCardList);
        return true;*/
        return true;
    }

    public ShoppingCard shoppingCard(long userId){
        ShoppingCard shoppingCard = new ShoppingCard();
        User user = userService.findUserById(userId);
        shoppingCard.setUser(user);
        return shoppingCard;
    }
}

