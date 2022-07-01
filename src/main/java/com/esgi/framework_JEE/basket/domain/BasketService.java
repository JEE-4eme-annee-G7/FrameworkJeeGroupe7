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
        basket.setUser(null);
        basket.setAmount(null);
        basketRepository.delete(basket);
    }

    public Basket addProducts(Basket basket, List<Integer> productIdList){
        var products = new ArrayList<Product>();

        productIdList.forEach(id -> products.add(productQuery.getProduct(id)));
        products.forEach(product -> {
            product.setBasket(basket);
            productCommand.saveProduct(product);
        });

        return getById(basket.getId());
    }


}
