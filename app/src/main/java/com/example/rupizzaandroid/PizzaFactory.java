package com.example.rupizzaandroid;

/**
 * The PizzaFactory interface defines methods for creating different types of pizzas.
 * Concrete factory classes, such as `NYPizza` and `ChicagoPizza`, implement this interface
 * to create specific pizzas with appropriate crusts and toppings.
 *
 * Methods include creating Deluxe, Meatzza, BBQChicken, and BuildYourOwn pizzas.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public interface PizzaFactory {

    /**
     * Creates a Deluxe pizza.
     *
     * @return A new Deluxe pizza with the correct crust and toppings for the pizza style.
     */
    Pizza createDeluxe();

    /**
     * Creates a Meatzza pizza.
     *
     * @return A new Meatzza pizza with the correct crust and toppings for the pizza style.
     */
    Pizza createMeatzza();

    /**
     * Creates a BBQ Chicken pizza.
     *
     * @return A new BBQ Chicken pizza with the correct crust and toppings for the pizza style.
     */
    Pizza createBBQChicken();

    /**
     * Creates a Build Your Own pizza.
     *
     * @return A new Build Your Own pizza with the correct crust for the pizza style.
     *         Toppings can be added by the user up to the allowed limit.
     */
    Pizza createBuildYourOwn();
}
