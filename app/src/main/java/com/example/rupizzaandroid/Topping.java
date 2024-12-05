package com.example.rupizzaandroid;

/**
 * Enum representing the available pizza toppings.
 * Each topping corresponds to a different ingredient that can be added to a pizza.
 *
 * @author Raashi Maheshwari
 * @author Tanvi Yamarthy
 */
public enum Topping {
    SAUSAGE(R.drawable.sausage),
    PEPPERONI(R.drawable.pepperoni),
    GREEN_PEPPER(R.drawable.greenpepper),
    ONION(R.drawable.onion),
    MUSHROOM(R.drawable.mushroom),
    BBQ_CHICKEN(R.drawable.bbqchicken),
    CHEDDAR(R.drawable.cheddar),
    PROVOLONE(R.drawable.provolone),
    BEEF(R.drawable.beef),
    HAM(R.drawable.ham),
    OLIVES(R.drawable.olives),
    PINEAPPLES(R.drawable.pineapple),
    JALAPENOS(R.drawable.jalapenos);

    private final int imageResId;
    private boolean isSelected = false;
    private boolean isEditable;

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

    Topping(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
    }
}
