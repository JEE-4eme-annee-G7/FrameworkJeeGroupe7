package com.esgi.framework_JEE.basket.domain;

import com.esgi.framework_JEE.basket.infrastructure.repository.BasketRepository;
import com.esgi.framework_JEE.product.domain.entities.Product;
import com.esgi.framework_JEE.product.web.command.ProductCommand;
import com.esgi.framework_JEE.product.web.query.ProductQuery;
import com.esgi.framework_JEE.user.Domain.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductQuery productQuery;
    private final ProductCommand productCommand;

    public BasketService(BasketRepository basketRepository, ProductQuery productQuery, ProductCommand productCommand) {
        this.basketRepository = basketRepository;
        this.productQuery = productQuery;
        this.productCommand = productCommand;
    }

    public Basket createEmpty(){
        var basket = new Basket();
        basketRepository.save(basket);

        return basket;
    }

    public Basket generateWithUser(User user){
        var basket = new Basket()
                .setUser(user)
                .setAmount(0.0);

        basketRepository.save(basket);
        return basket;
    }

    public Basket getById(int id){
        return basketRepository.getBasketById(id);
    }

    public Basket getByUserId(int id){
        return basketRepository.findBasketByUser_Id(id);
    }

    public List<Basket> getAll(){
        return basketRepository.findAll();
    }

    public void delete(int id){
        var basket = getById(id);
        basket.setUser(null);
        basket.setAmount(null);
        basketRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUser(int user_id){
        var basket = getByUserId(user_id);

        var products = productQuery.getProductsByBasketId(basket.getId());
        products.forEach(product -> {
            product.setBasket(null);
            productCommand.saveProduct(product);
        });

        basket.setUser(null);
        basketRepository.save(basket);
        basketRepository.delete(basket);
    }

    public Basket addProducts(Basket basket, List<Integer> productIdList){
        var products = new ArrayList<Product>();

        AtomicReference<Double> basketAmount = new AtomicReference<>(basket.getAmount());

        //TODO : Vérifier que le produit n'est pas déjà dans le panier

        productIdList.forEach(id -> products.add(productQuery.getProduct(id)));
        products.forEach(product -> {
            product.setBasket(basket);
            productCommand.saveProduct(product);
            basketAmount.updateAndGet(amount -> amount + product.getPrice());
        });


        basket.setAmount(basketAmount.get());
        basketRepository.save(basket);

        return getById(basket.getId());
    }

    public List<Product> getProductFromBasketId(int basket_id){
        return productQuery.getProductsByBasketId(basket_id);
    }

    public void removeProductFromBasket(Basket basket, Product product){
        product.setBasket(null);
        productCommand.saveProduct(product);

        basket.setAmount(basket.getAmount() - product.getPrice());
        basketRepository.save(basket);
    }




}
