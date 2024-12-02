package com.example.rupizzaandroid;

/**
 * The Crust enum represents the different types of crusts available for pizzas.
 * It categorizes crust types into New York style and Chicago style, with specific
 * crust options such as Brooklyn, Thin, Hand Tossed, Deep Dish, Pan, and Stuffed.
 * Each crust type is associated with its respective pizza style (e.g., New York or Chicago).
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public enum Crust {
    // New York Style Crusts
    BROOKLYN("NYPizza", "Brooklyn"),
    THIN("NYPizza", "Thin"),
    HAND_TOSSED("NYPizza", "Hand Tossed"),

    // Chicago Style Crusts
    DEEP_DISH("ChicagoPizza", "Deep Dish"),
    PAN("ChicagoPizza", "Pan"),
    STUFFED("ChicagoPizza", "Stuffed");

    private final String style;
    private final String crustType;

    /**
     * Constructor for the Crust enum.
     * Initializes the style and crust type for each enum constant.
     *
     * @param style The pizza style associated with this crust (e.g., NY or Chicago).
     * @param crustType The specific type of crust (e.g., Brooklyn, Thin, Deep Dish).
     */
    Crust(String style, String crustType) {
        this.style = style;
        this.crustType = crustType;
    }

    /**
     * Returns a string representation of this crust type and its associated pizza style.
     * The format will be "{crustType} ({style} style)".
     *
     * @return A string representation of the crust type and style.
     */
    @Override
    public String toString() {
        return this.crustType + " (" + this.style + " style)";
    }
}
