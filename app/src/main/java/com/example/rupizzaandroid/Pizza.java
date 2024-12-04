package com.example.rupizzaandroid;

import java.util.ArrayList;

/**
 * The Pizza class represents a pizza object with a specific crust, size, and toppings.
 * It provides methods to manage these properties and calculate the price based on the pizza type,
 * size, and toppings. This class serves as the base class for specific pizza types like Deluxe,
 * BBQChicken, Meatzza, and BuildYourOwn, allowing the use of polymorphism for price calculations.
 *
 * This is an abstract class, and specific pizza types should extend it and implement the price method.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public abstract class Pizza {

    // List of toppings for the pizza
    private ArrayList<Topping> toppings;

    // Crust type for the pizza
    private Crust crust;

    // Size of the pizza
    private Size size;

    /**
     * Abstract method to be implemented by subclasses to calculate the price of the pizza.
     *
     * @return The price of the pizza based on its size, crust, and toppings.
     */
    public abstract double price();

    /**
     * Sets the crust type for this pizza.
     *
     * @param crust The crust type to set.
     */
    public void setCrust(Crust crust) {
        this.crust = crust;
    }

    /**
     * Gets the crust type of this pizza.
     *
     * @return The crust type of this pizza.
     */
    public Crust getCrust() {
        return crust;
    }

    /**
     * Sets the size of this pizza.
     *
     * @param size The size to set.
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Gets the size of this pizza.
     *
     * @return The size of this pizza.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Constructor for initializing the pizza with an empty list of toppings.
     */
    public Pizza() {
        this.toppings = new ArrayList<>();
    }

    /**
     * Adds a topping to the pizza. A maximum of 7 toppings can be added for "Build Your Own" pizzas.
     *
     * @param topping The topping to add to the pizza.
     */
    public void addTopping(Topping topping) {
        if (toppings.size() < 7) {
            toppings.add(topping);
        }
    }

    /**
     * Gets the list of toppings for this pizza.
     *
     * @return The list of toppings on this pizza.
     */
    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    /**
     * Calculates the price of the pizza based on its type, size, and toppings. The price varies
     * depending on the specific pizza type (Deluxe, BBQChicken, Meatzza, or BuildYourOwn).
     *
     * @return The price of the pizza based on the size, crust, and toppings.
     */
    public double getPrice() {
        double basePrice = 0.0;
        this.setSize(size);

        switch (this.getClass().getSimpleName()) {
            case "Deluxe":
                switch (size) {
                    case SMALL:
                        basePrice = 16.99;
                        break;
                    case MEDIUM:
                        basePrice = 18.99;
                        break;
                    case LARGE:
                        basePrice = 20.99;
                        break;
                }
                break;
            case "BBQChicken":
                switch (size) {
                    case SMALL:
                        basePrice = 14.99;
                        break;
                    case MEDIUM:
                        basePrice = 16.99;
                        break;
                    case LARGE:
                        basePrice = 19.99;
                        break;
                }
                break;
            case "Meatzza":
                switch (size) {
                    case SMALL:
                        basePrice = 17.99;
                        break;
                    case MEDIUM:
                        basePrice = 19.99;
                        break;
                    case LARGE:
                        basePrice = 21.99;
                        break;
                }
                break;
            case "BuildYourOwn":
                switch (size) {
                    case SMALL:
                        basePrice = 8.99;
                        break;
                    case MEDIUM:
                        basePrice = 10.99;
                        break;
                    case LARGE:
                        basePrice = 12.99;
                        break;
                }
                basePrice += (toppings.size() * 1.69);
                break;
        }
        return basePrice;
    }

    /**
     * Removes a topping from the pizza.
     *
     * @param topping The topping to be removed.
     * @return true if the topping was successfully removed, false otherwise.
     */
    public boolean removeTopping(Topping topping) {
        if (toppings != null && toppings.contains(topping)) {
            toppings.remove(topping);
            return true;
        }
        return false; // Return false if the topping wasn't found
    }

    /**
     * Returns a string representation of the pizza, including its type, size, crust, and toppings.
     *
     * @return A string representation of the pizza.
     */
    @Override
    public String toString() {
        return "Pizza Type: " + this.getClass().getSimpleName() +
                ", Size: " + this.getSize() +
                ", Crust: " + this.getCrust() +
                ", Toppings: " + this.getToppings().toString();
    }

    public boolean removeTopping(Topping topping) {
        if (toppings != null && toppings.contains(topping)) {
            toppings.remove(topping);
            return true;
        }
        return false; // Return false if the topping wasn't found
    }

}
