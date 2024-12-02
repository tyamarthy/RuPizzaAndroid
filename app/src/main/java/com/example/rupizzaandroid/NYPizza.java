package com.example.rupizzaandroid;

/**
 * The NYPizza class represents a New York-style pizza factory. It implements the PizzaFactory
 * interface and provides methods to create specific types of pizzas, including Deluxe, Meatzza,
 * BBQ Chicken, and Build Your Own pizzas. Each pizza is customized with a specific crust and
 * set of toppings.
 *
 * This class provides methods to create each pizza type with predefined crusts and toppings,
 * in line with the New York pizza style.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public class NYPizza extends Pizza implements PizzaFactory{

    /**
     * Creates a Deluxe pizza with Brooklyn-style crust and specific toppings.
     *
     * @return a Deluxe pizza with Brooklyn-style crust and toppings: Sausage, Pepperoni,
     *         Green Pepper, Onion, and Mushroom.
     */
    @Override
    public Pizza createDeluxe() {
        Pizza pizza = new Deluxe();
        pizza.setCrust(Crust.BROOKLYN);
        pizza.addTopping(Topping.SAUSAGE);
        pizza.addTopping(Topping.PEPPERONI);
        pizza.addTopping(Topping.GREEN_PEPPER);
        pizza.addTopping(Topping.ONION);
        pizza.addTopping(Topping.MUSHROOM);
        return pizza;
    }

    /**
     * Creates a Meatzza pizza with Hand-Tossed crust and specific toppings.
     *
     * @return a Meatzza pizza with Hand-Tossed crust and toppings: Sausage, Pepperoni, Beef,
     *         and Ham.
     */
    @Override
    public Pizza createMeatzza() {
        Pizza pizza = new Meatzza();
        pizza.setCrust(Crust.HAND_TOSSED);
        pizza.addTopping(Topping.SAUSAGE);
        pizza.addTopping(Topping.PEPPERONI);
        pizza.addTopping(Topping.BEEF);
        pizza.addTopping(Topping.HAM);
        return pizza;
    }

    /**
     * Creates a BBQ Chicken pizza with Thin crust and specific toppings.
     *
     * @return a BBQ Chicken pizza with Thin crust and toppings: BBQ Chicken, Green Pepper,
     *         Provolone, and Cheddar.
     */
    @Override
    public Pizza createBBQChicken() {
        Pizza pizza = new BBQChicken();
        pizza.setCrust(Crust.THIN);
        pizza.addTopping(Topping.BBQ_CHICKEN);
        pizza.addTopping(Topping.GREEN_PEPPER);
        pizza.addTopping(Topping.PROVOLONE);
        pizza.addTopping(Topping.CHEDDAR);
        return pizza;
    }

    /**
     * Creates a Build Your Own pizza with Hand-Tossed crust. The pizza starts without toppings,
     * but staff can add up to 7 toppings as needed.
     *
     * @return a Build Your Own pizza with Hand-Tossed crust, initially without toppings.
     */
    @Override
    public Pizza createBuildYourOwn() {
        Pizza pizza = new BuildYourOwn();
        pizza.setCrust(Crust.HAND_TOSSED);
        return pizza;
    }

    /**
     * A placeholder price method that does not return a price for the NYPizza class.
     * This method is required by the PizzaFactory interface but is not implemented here.
     *
     * @return 0.0 as this method does not provide a price.
     */
    @Override
    public double price() {
        return 0;
    }
}

