package com.example.rupizzaandroid;

public class Deluxe extends Pizza{

    /**
     * Calculates the price of the Deluxe pizza based on its size.
     *
     * @return the price of the Deluxe pizza based on its size (Small, Medium, or Large).
     *         - Small: $16.99
     *         - Medium: $18.99
     *         - Large: $20.99
     */
    @Override
    public double price() {
        if (getSize() == (Size.SMALL)) {
            return 16.99;
        } else if (getSize() == Size.MEDIUM) {
            return 18.99;
        } else if (getSize() == Size.LARGE) {
            return 20.99;
        }
        return 0.0;
    }
}