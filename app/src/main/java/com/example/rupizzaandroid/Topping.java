package com.example.rupizzaandroid;

/**
 * Enum representing the available pizza toppings.
 * Each topping corresponds to a different ingredient that can be added to a pizza.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public enum Topping {
    SAUSAGE,
    PEPPERONI,
    GREEN_PEPPER,
    ONION,
    MUSHROOM,
    BBQ_CHICKEN,
    CHEDDAR,
    PROVOLONE,
    BEEF,
    HAM,
    OLIVES,
    PINEAPPLES,
    JALAPENOS;


    private boolean isSelected = false;

    // Method to get the selection status
    public boolean isSelected() {
        return isSelected;
    }

    // Method to set the selection status
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }

}
