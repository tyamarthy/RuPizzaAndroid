package com.example.rupizzaandroid;

/**
 * The Meatzza class represents a Meatzza pizza, which is a specific type of pizza with a
 * set pricing structure based on size.
 * It extends the abstract class Pizza and implements the method to calculate the price
 * for a Meatzza pizza based on its size.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public class Meatzza extends Pizza{

    /**
     * Calculates the price of the Meatzza pizza based on its size.
     *
     * @return the price of the Meatzza pizza based on its size (Small, Medium, or Large).
     *         - Small: $17.99
     *         - Medium: $19.99
     *         - Large: $21.99
     */
    @Override
    public double price() {
        if (getSize() == (Size.SMALL)) {
            return 17.99;
        } else if (getSize() == Size.MEDIUM) {
            return 19.99;
        } else if (getSize() == Size.LARGE) {
            return 21.99;
        }
        return 0.0;
    }
}
