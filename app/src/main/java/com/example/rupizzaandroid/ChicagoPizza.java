package com.example.rupizzaandroid;

public class ChicagoPizza extends Pizza implements  PizzaFactory{

    /**
     * Creates a Deluxe pizza with a Deep Dish crust and a selection of toppings.
     * The toppings include sausage, pepperoni, green pepper, onion, and mushrooms.
     *
     * @return a new Deluxe pizza with Deep Dish crust and predefined toppings.
     */
    @Override
    public Pizza createDeluxe() {
        Pizza pizza = new Deluxe();
        pizza.setCrust(Crust.DEEP_DISH);
        pizza.addTopping(Topping.SAUSAGE);
        pizza.addTopping(Topping.PEPPERONI);
        pizza.addTopping(Topping.GREEN_PEPPER);
        pizza.addTopping(Topping.ONION);
        pizza.addTopping(Topping.MUSHROOM);
        return pizza;
    }

    /**
     * Creates a Meatzza pizza with a Stuffed crust and a selection of meat toppings.
     * The toppings include sausage, pepperoni, beef, and ham.
     *
     * @return a new Meatzza pizza with Stuffed crust and predefined meat toppings.
     */
    @Override
    public Pizza createMeatzza() {
        Pizza pizza = new Meatzza();
        pizza.setCrust(Crust.STUFFED);
        pizza.addTopping(Topping.SAUSAGE);
        pizza.addTopping(Topping.PEPPERONI);
        pizza.addTopping(Topping.BEEF);
        pizza.addTopping(Topping.HAM);
        return pizza;
    }

    /**
     * Creates a BBQ Chicken pizza with a Pan crust and a selection of BBQ chicken toppings.
     * The toppings include BBQ chicken, green pepper, provolone, and cheddar.
     *
     * @return a new BBQ Chicken pizza with Pan crust and predefined BBQ chicken toppings.
     */
    @Override
    public Pizza createBBQChicken() {
        Pizza pizza = new BBQChicken();
        pizza.setCrust(Crust.PAN);
        pizza.addTopping(Topping.BBQ_CHICKEN);
        pizza.addTopping(Topping.GREEN_PEPPER);
        pizza.addTopping(Topping.PROVOLONE);
        pizza.addTopping(Topping.CHEDDAR);
        return pizza;
    }

    /**
     * Creates a Build Your Own pizza with a Pan crust. The toppings are not specified and can be added later.
     *
     * @return a new Build Your Own pizza with Pan crust and no toppings initially.
     */
    @Override
    public Pizza createBuildYourOwn() {
        Pizza pizza = new BuildYourOwn();
        pizza.setCrust(Crust.PAN);
        return pizza;
    }

    /**
     * This method is not implemented in this class, as it doesn't apply to the pizza factory.
     *
     * @return 0.0 as this method does not apply to the ChicagoPizza factory.
     */
    @Override
    public double price() {
        return 0;
    }
}


