package com.esgi.framework_JEE.basket.infrastructure.web;



import com.esgi.framework_JEE.basket.paiment.PaymentRequest;
import com.esgi.framework_JEE.basket.paiment.mapper.BuyerMapper;
import com.esgi.framework_JEE.basket.paiment.mapper.PaymentMapper;
import com.esgi.framework_JEE.basket.infrastructure.web.response.BasketResponse;
import com.esgi.framework_JEE.basket.domain.Basket;
import com.esgi.framework_JEE.basket.domain.BasketService;
import com.esgi.framework_JEE.invoice.domain.InvoiceService;
import com.esgi.framework_JEE.product.web.query.ProductQuery;
import com.esgi.framework_JEE.user.query.UserQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/basket")
public class BasketController {

    private final BasketService basketService;
    private final UserQuery userQuery;
    private final BuyerMapper buyerMapper;
    private final PaymentMapper paymentMapper;
    private final ProductQuery productQuery;
    private final InvoiceService invoiceService;

    public BasketController(BasketService basketService, UserQuery userQuery, BuyerMapper buyerMapper, PaymentMapper paymentMapper, ProductQuery productQuery, InvoiceService invoiceService) {
        this.basketService = basketService;
        this.userQuery = userQuery;
        this.buyerMapper = buyerMapper;
        this.paymentMapper = paymentMapper;
        this.productQuery = productQuery;
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<?> create(){
        var basketCreated = basketService.createEmpty();

        return ResponseEntity.created(
                linkTo(
                        methodOn(BasketController.class).getById(basketCreated.getId())
                ).toUri()
        ).build();
    }

    @PostMapping("/generate/{user_id}")
    public ResponseEntity<?> generateBasket(@PathVariable int user_id){
        var user = userQuery.getById(user_id);
        if(user == null){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

        var basket = basketService.getByUserId(user_id);
        if(basket != null){
            return new ResponseEntity<>(user.getFirstname() + " have already a basket", HttpStatus.FORBIDDEN);
        }

        var basketCreated = basketService.generateWithUser(user);


        return ResponseEntity.created(
                linkTo(
                        methodOn(BasketController.class).getById(basketCreated.getId())
                ).toUri()
        ).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        var basket = basketService.getById(id);
        if(basket == null){
            return new ResponseEntity<>(" Basket not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(toResponse(basket), HttpStatus.FOUND);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getByUserId(@PathVariable int id){
        var user = userQuery.getById(id);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        var basket = basketService.getByUserId(user.getId());
        if(basket == null){
            return new ResponseEntity<>("Basket not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(toResponse(basket), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<BasketResponse>> getAll(){
        var basketResponses = basketService.getAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(basketResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id){

        var basketToDelete = basketService.getById(id);
        if(basketToDelete == null){
            return new ResponseEntity<>(" Basket not found", HttpStatus.NOT_FOUND);
        }

        basketService.delete(id);

        return new ResponseEntity<>("Basket deleted", HttpStatus.OK);
    }


    @DeleteMapping("/userId/{user_id}")
    public ResponseEntity<String> deleteByUserId(@PathVariable int user_id){
        var user = userQuery.getById(user_id);
        if(user == null){
            return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);
        }

        var userBasket = basketService.getByUserId(user.getId());
        if (userBasket == null){
            return new ResponseEntity<>(user.getFirstname() + "don't have any basket", HttpStatus.NOT_FOUND);
        }

        basketService.deleteByUser(user.getId());

        return new ResponseEntity<>("Basket deleted", HttpStatus.OK);
    }


    @PutMapping("/{user_id}")
    public ResponseEntity<?> addProduct(@PathVariable int user_id, @RequestBody List<Integer> productIdList){
        var user = userQuery.getById(user_id);
        if(user == null) return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);

        var basket = basketService.getByUserId(user.getId());
        if(basket == null) return new ResponseEntity<>(user.getFirstname() + "don't have any basket", HttpStatus.NOT_FOUND);

        for (Integer productId: productIdList) {
            if (productQuery.getProduct(productId) == null) {
                return new ResponseEntity<>("Product " + productId + "not found", HttpStatus.BAD_REQUEST);
            }
        }

        var basketUpdated = basketService.addProducts(basket, productIdList);

        return new ResponseEntity<>(toResponse(basketUpdated), HttpStatus.OK);
    }


    @GetMapping("/{basket_id}/products")
    public ResponseEntity<?> getProductInBasket(@PathVariable int basket_id){
        var basket = basketService.getById(basket_id);
        if(basket == null) return new ResponseEntity<>(" Basket not found", HttpStatus.NOT_FOUND);

        var basketResponse = basketService.getProductFromBasketId(basket.getId());

        return new ResponseEntity<>(basketResponse, HttpStatus.FOUND);
    }


    @PatchMapping("/{basket_id}/product/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int basket_id, @PathVariable int product_id){
        var basket = basketService.getById(basket_id);
        if(basket == null) return new ResponseEntity<>(" Basket not found", HttpStatus.NOT_FOUND);

        var product = productQuery.getProduct(product_id);
        if(product == null) return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);

        if(product.getBasket() == null ||product.getBasket().getId() != basket.getId()) {
            return new ResponseEntity<>("Product is not in this basket", HttpStatus.FORBIDDEN);
        }

        basketService.removeProductFromBasket(basket, product);

        return new ResponseEntity<>("Product remove from basket", HttpStatus.OK);
    }


    @PostMapping("/payment")
    public ResponseEntity<?> paid(@RequestBody PaymentRequest paymentRequest){

        //Retrouver les infos du user a partir de paymentRequest
        var user = userQuery.getById(paymentRequest.getUser_id());
        if(user == null) return new ResponseEntity<>(" User not found", HttpStatus.NOT_FOUND);

        var payment = paymentMapper.toPayment(paymentRequest);
        //Convertir le user en buyer
        payment.setBuyer(buyerMapper.userToBuyer(user, paymentRequest.getCreditCard()));
        //Aller chercher le basket du user
        var userBasket = basketService.getByUserId(user.getId());
        //Mettre ?? jour le montant
        payment.setAmount(userBasket.getAmount());

        try{
            URI uri = URI.create("http://localhost:8090/payment");
            var restTemplate = new RestTemplate();

            ResponseEntity<?> paymentResult = restTemplate.postForEntity(uri, payment, String.class);

            if(paymentResult.getStatusCodeValue() == 202){
                invoiceService.generateWithUser(user, userBasket);
                basketService.deleteByUser(user.getId());
            }
            return new ResponseEntity<>(paymentResult.getBody(), paymentResult.getStatusCode());

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Payment Refused ! Please check your information and retry with the same checkout_id", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private BasketResponse toResponse(Basket basket){
        return new BasketResponse()
                .setUserId(
                        basket.getUser() == null ? 0 : basket.getUser().getId()
                )
                .setAmount(basket.getAmount());
    }
}
