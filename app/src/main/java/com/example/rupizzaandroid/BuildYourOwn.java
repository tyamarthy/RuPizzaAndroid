package com.example.rupizzaandroid;

import java.util.ArrayList;
import java.util.List;

/**
 * The BuildYourOwn class is a subclass of the Pizza class.
 * It represents a customizable pizza where customers can add toppings.
 * The price of the pizza is calculated based on the size and the number of toppings selected.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public class BuildYourOwn extends Pizza{

    // List to hold the toppings added to the pizza
    private List<Topping> toppings;

    // Constant for the cost of each topping
    private static final double TOPPING_COST = 1.69;

    /**
     * Constructs a BuildYourOwn pizza object with an empty list of toppings.
     */
    public BuildYourOwn() {
        toppings = new ArrayList<>();
    }

    /**
     * Adds a topping to the pizza, provided the maximum number of toppings (7) hasn't been reached.
     *
     * @param topping the topping to be added
     */
    public void addTopping(Topping topping) {
        if (toppings.size() < 7) {
            toppings.add(topping);
        }
    }

    /**
     * Calculates the price of the BuildYourOwn pizza based on its size and the number of toppings.
     * The price is determined as follows:
     * - Small: $8.99
     * - Medium: $10.99
     * - Large: $12.99
     * Plus an additional $1.69 for each topping added (up to a maximum of 7 toppings).
     *
     * @return the total price of the BuildYourOwn pizza
     */
    @Override
    public double price() {
        double startPrice = 0.0;
        if (getSize()== (Size.SMALL)) {
            startPrice = 8.99;
        } else if (getSize() == Size.MEDIUM) {
            startPrice = 10.99;
        } else if (getSize() == Size.LARGE) {
            startPrice = 12.99;
        }
        double toppingsCost = toppings.size() * TOPPING_COST;
        return startPrice + toppingsCost;
    }

    /**
     * Returns the list of toppings added to the BuildYourOwn pizza.
     *
     * @return an ArrayList containing the toppings added to the pizza
     */
    public ArrayList getToppings() {
        return (ArrayList) toppings;
    }
}
