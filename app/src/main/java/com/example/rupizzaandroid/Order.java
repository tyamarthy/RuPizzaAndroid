package com.example.rupizzaandroid;

import java.util.ArrayList;

/**
 * The Order class represents a customer's pizza order. It maintains a list of pizzas in the order,
 * calculates the total price of the order, and provides methods to add, remove, and manage pizzas.
 * Each order has a unique order number and is tracked statically along with all orders placed.
 *
 * The class also includes functionality to clear the order, place the order, and retrieve the list
 * of all orders.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public class Order {

    // Static counter for generating unique order numbers
    private static int orderCounter = 1;

    private static int nextNum = 1;
    // Unique order number for each order
    private int orderNum;

    private boolean orderPlaced = false;

    // List of pizzas in the order
    private ArrayList<Pizza> pizzas = new ArrayList<>();

    // List of all orders placed
    private static ArrayList<Order> allOrders = new ArrayList<>();

    /**
     * Constructor for creating a new order with a specified order number.
     *
     * @param number The custom order number.
     */
    public Order(int number) {
        this.pizzas = new ArrayList<>();
    }

    /**
     * Default constructor for creating a new order with an auto-generated order number.
     * The order number is incremented each time an order is placed.
     */
    public Order() {
        this.orderNum = orderCounter++;
        this.pizzas = new ArrayList<>();
    }

    /**
     * Gets the order number for this order.
     *
     * @return The unique order number.
     */
    public int getOrderNum() {
        return orderNum;
    }

    /**
     * Gets the list of pizzas in this order.
     *
     * @return A list of pizzas in the order.
     */
    public ArrayList<Pizza>getPizzas(){
        return pizzas;
    }

    /**
     * Adds a pizza to the order.
     *
     * @param pizza The pizza to be added to the order.
     */
    public void addAPizza(Pizza pizza){
        if(pizza!=null){
            pizzas.add(pizza);
        }
    }

    /**
     * Removes a pizza from the order by its index in the pizza list.
     *
     * @param index The index of the pizza to remove.
     */
    public void removePizza(int index) {
        if (index >= 0 && index < pizzas.size()) {
            pizzas.remove(index);
        }
    }

    /**
     * Clears the pizzas from the order, effectively emptying the order.
     * This method does not affect the order number or other order details.
     */
    public void clearOrderDisplay() {
        pizzas.clear();
    }

    /**
     * Adds the order to the static list of all orders placed.
     * This method ensures that only one instance of the order is added.
     */
    //public void placeOrder() {
     //   if (!allOrders.contains(this)) {
      //    allOrders.add(this);  // Add this order to the list of all orders
      //  }
      // orderCounter++;
    //}
    public void placeOrder() {
        if (!orderPlaced) {
            orderPlaced = true;
            allOrders.add(this);
        }
    }

    public boolean orderPlaced() {
        return orderPlaced;
    }

    /**
     * Gets the list of all orders placed.
     *
     * @return A list of all orders.
     */
    public static ArrayList<Order>getAllOrders(){
        return allOrders;
    }
}
