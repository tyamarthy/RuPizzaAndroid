package com.example.rupizzaandroid;

/**
 * The BBQChicken class is a subclass of the Pizza class.
 * It represents a BBQ Chicken pizza with different pricing based on the size.
 * This class overrides the `price()` method to calculate the price based on the pizza size.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public class BBQChicken extends Pizza{

    /**
     * Calculates the price of the BBQ Chicken pizza based on its size.
     * The price is determined as follows:
     * - Small: $14.99
     * - Medium: $16.99
     * - Large: $19.99
     *
     * @return the price of the BBQ Chicken pizza based on the size.
     */
    @Override
    public double price() {
        if (getSize()== (Size.SMALL)) {
            return 14.99;
        } else if (getSize() == Size.MEDIUM) {
            return 16.99;
        } else if (getSize() == Size.LARGE) {
            return 19.99;
        }
        return 0.0;
    }
}
