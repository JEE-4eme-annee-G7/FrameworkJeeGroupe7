package com.esgi.framework_JEE.basket.domain;

import com.esgi.framework_JEE.user.Domain.entities.User;

import javax.persistence.*;

@Entity
@Table(name = "basket")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Double amount;

    public int getId() {
        return id;
    }

    public Basket setId(int id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Basket setUser(User user) {
        this.user = user;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Basket setAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}
