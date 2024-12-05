package com.example.rupizzaandroid;

import java.util.ArrayList;

public class OrderManager {
    private static OrderManager instance;
    private Order currentOrder;
    private OrderManager() {
        currentOrder = new Order();
    }
    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addPizza(Pizza pizza) {
        currentOrder.addAPizza(pizza);
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public int getTotalPizzasInOrder() {
        return currentOrder.getPizzas().size();
    }

    public void clearOrder() {
        if (currentOrder.orderPlaced()) {
            currentOrder = new Order();
        } else {
            currentOrder.getPizzas().clear();
        }
    }
    }
