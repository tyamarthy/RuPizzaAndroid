package com.example.rupizzaandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewYorkActivity extends AppCompatActivity {

    private Spinner pizzaTypeSpinner;
    private RecyclerView availableToppingsRecyclerView;
    private TextView crustTextView;
    private TextView priceTextView;
    private RadioButton smallRadioButton;
    private RadioButton mediumRadioButton;
    private RadioButton largeRadioButton;
    private ImageView pizzaImageView;
    private Button backButton;
    private Button orderButton;

    private static final int MAX_TOPPINGS_LIMIT = 7;

    private Pizza currPizza;
    private NYPizza newYorkPizzaFactory = new NYPizza();

    private OrderManager sharedOrder;

    private List<Topping> availableToppings = List.of(
            Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER,
            Topping.ONION, Topping.MUSHROOM, Topping.BBQ_CHICKEN,
            Topping.CHEDDAR, Topping.PROVOLONE, Topping.BEEF, Topping.HAM,
            Topping.OLIVES, Topping.PINEAPPLES, Topping.JALAPENOS
    );

    private ToppingAdapter availableToppingsAdapter;
    private ToppingAdapter selectedToppingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ny_activity);
        sharedOrder=OrderManager.getInstance();

        initializeViews();
        spinnerSetUp();
        radioButtonSetUp();
        recyclerViewSetUp();
        buttonSetUp();
    }

    private void initializeViews() {
        pizzaTypeSpinner = findViewById(R.id.spinnerPizzaSpecialty);
        availableToppingsRecyclerView = findViewById(R.id.recyclerViewToppings);
        crustTextView = findViewById(R.id.crustTextView);
        priceTextView = findViewById(R.id.priceTextView);
        smallRadioButton = findViewById(R.id.radioSmall);
        mediumRadioButton = findViewById(R.id.radioMedium);
        largeRadioButton = findViewById(R.id.radioLarge);
        pizzaImageView = findViewById(R.id.pizzaImageView);
        backButton = findViewById(R.id.backButton);
        orderButton = findViewById(R.id.orderButton);
    }

    private void spinnerSetUp() {
        String[] pizzaTypes = {"Select Pizza Type...", "Deluxe", "BBQ Chicken", "Meatzza", "Build your own"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pizzaTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaTypeSpinner.setAdapter(adapter);

        pizzaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPizzaType = (String) parent.getItemAtPosition(position);
                choosingAPizza(selectedPizzaType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void radioButtonSetUp() {
        smallRadioButton.setOnClickListener(v -> {
            mediumRadioButton.setChecked(false);
            largeRadioButton.setChecked(false);
            updatePrice();
        });

        mediumRadioButton.setOnClickListener(v -> {
            smallRadioButton.setChecked(false);
            largeRadioButton.setChecked(false);
            updatePrice();
        });

        largeRadioButton.setOnClickListener(v -> {
            smallRadioButton.setChecked(false);
            mediumRadioButton.setChecked(false);
            updatePrice();
        });
    }

    private void recyclerViewSetUp() {
        availableToppingsAdapter = new ToppingAdapter(availableToppings, topping -> {
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                if (topping.isSelected()) {
                    topping.setSelected(false);
                    selectedToppingsAdapter.removeTopping(topping);
                    currPizza.getToppings().remove(topping);

                    if (currPizza.getToppings().isEmpty()) {
                        pizzaImageView.setImageResource(R.drawable.buildyourown);
                    }
                } else {
                    if (selectedToppingsAdapter.getItemCount() >= MAX_TOPPINGS_LIMIT) {
                        showAlert("Maximum Toppings Reached", "The toppings you can choose are limited to a maximum of " + MAX_TOPPINGS_LIMIT);
                        return;
                    }
                    topping.setSelected(true);
                    selectedToppingsAdapter.addTopping(topping);
                    currPizza.addTopping(topping);
                }

                buildYourOwnImages();
                updatePrice();
                availableToppingsAdapter.notifyDataSetChanged(); // Refresh the UI
            } else {
                showAlert("Cannot choose toppings", "You can only choose your own toppings for Build Your Own pizzas.");
            }
        }, imageResId -> {
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                pizzaImageView.setImageResource(imageResId);
            }
        });

        selectedToppingsAdapter = new ToppingAdapter(new ArrayList<>(), topping -> {
        }, imageResId -> {
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                pizzaImageView.setImageResource(imageResId);
            }
        });

        availableToppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableToppingsRecyclerView.setAdapter(availableToppingsAdapter);
    }

    private void buildYourOwnImages() {
        if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
            List<Topping> selectedToppings = currPizza.getToppings();

            if (selectedToppings.isEmpty()) {
                pizzaImageView.setImageResource(R.drawable.buildyourown);
            } else {
                Topping lastTopping = selectedToppings.get(selectedToppings.size() - 1);
                int toppingImageResource = toppingsImages(lastTopping);
                pizzaImageView.setImageResource(toppingImageResource);
            }
        }
    }

    private int toppingsImages(Topping topping) {
        switch (topping) {
            case SAUSAGE: return R.drawable.sausage;
            case PEPPERONI: return R.drawable.pepperoni;
            case GREEN_PEPPER: return R.drawable.greenpepper;
            case ONION: return R.drawable.onion;
            case MUSHROOM: return R.drawable.mushroom;
            case BBQ_CHICKEN: return R.drawable.bbqchicken;
            case CHEDDAR: return R.drawable.cheddar;
            case PROVOLONE: return R.drawable.provolone;
            case BEEF: return R.drawable.beef;
            case HAM: return R.drawable.ham;
            case OLIVES: return R.drawable.olives;
            case PINEAPPLES: return R.drawable.pineapple;
            case JALAPENOS: return R.drawable.jalapenos;
            default: return R.drawable.buildyourown; // Fallback default image
        }
    }

    private void buttonSetUp() {
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        orderButton.setOnClickListener(v -> addingAPizza());
    }

    private void choosingAPizza(String pizzaType) {
        for (Topping topping : availableToppings) {
            topping.setSelected(false);
            topping.setEditable(false);
        }
        availableToppingsAdapter.notifyDataSetChanged();

        selectedToppingsAdapter.clearToppings();

        switch (pizzaType) {
            case "Deluxe":
                currPizza = newYorkPizzaFactory.createDeluxe();
                preselectedToppings(List.of(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER,
                        Topping.ONION, Topping.MUSHROOM
                ));
                pizzaImageView.setImageResource(R.drawable.deluxe); // Deluxe image
                break;

            case "BBQ Chicken":
                currPizza = newYorkPizzaFactory.createBBQChicken();
                preselectedToppings(List.of(
                        Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER,
                        Topping.PROVOLONE, Topping.CHEDDAR
                ));
                pizzaImageView.setImageResource(R.drawable.bbqchicken); // BBQ Chicken image
                break;

            case "Meatzza":
                currPizza = newYorkPizzaFactory.createMeatzza();
                preselectedToppings(List.of(
                        Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF,
                        Topping.HAM
                ));
                pizzaImageView.setImageResource(R.drawable.meatzza); // Meatzza image
                break;

            case "Build your own":
                currPizza = newYorkPizzaFactory.createBuildYourOwn();
                for (Topping topping : availableToppings) {
                    topping.setEditable(true); // Make all toppings editable
                }
                pizzaImageView.setImageResource(R.drawable.buildyourown); // Default image for Build Your Own
                break;

            default:
                currPizza = null;
                pizzaImageView.setImageResource(R.drawable.pinkpizza); // Default fallback image
                break;
        }

        crustTextView.setText("Crust: " + (currPizza != null ? currPizza.getCrust() : ""));

        smallRadioButton.setChecked(false);
        mediumRadioButton.setChecked(false);
        largeRadioButton.setChecked(false);
        updatePrice();
    }

    private void preselectedToppings(List<Topping> specialtyToppings) {
        for (Topping topping : availableToppings) {
            if (specialtyToppings.contains(topping)) {
                topping.setSelected(true);
                topping.setEditable(false);
                selectedToppingsAdapter.addTopping(topping);
            } else {
                topping.setSelected(false);
            }
        }
        availableToppingsAdapter.notifyDataSetChanged();
    }

    private void updatePrice() {
        double price = 0.0;
        Size selectedSize = null;

        if (smallRadioButton.isChecked()) {
            selectedSize = Size.SMALL;
        } else if (mediumRadioButton.isChecked()) {
            selectedSize = Size.MEDIUM;
        } else if (largeRadioButton.isChecked()) {
            selectedSize = Size.LARGE;
        }

        if (selectedSize != null && currPizza != null) {
            currPizza.setSize(selectedSize);
            price = currPizza.price();
        }

        priceTextView.setText(String.format("$%.2f", price));
    }

    private void addingAPizza() {
        String selectedPizza = (String) pizzaTypeSpinner.getSelectedItem();

        if ("Select Pizza Type...".equals(selectedPizza)) {
            showAlert("Error: Cannot Add Pizza to Order", "You need to select a pizza type before you can add a pizza to the order.");
            return;
        }

        if (!(smallRadioButton.isChecked() || mediumRadioButton.isChecked() || largeRadioButton.isChecked())) {
            showAlert("Error: Cannot Cannot Add Pizza to Order", "You need to select a pizza size before you can add a pizza to the order.");
            return;
        }

        if (currPizza != null) {
            sharedOrder.addPizza(currPizza);
            showSuccess("Pizza Added to Order!", "Pizza added successfully!\nTotal pizzas in order: " + sharedOrder.getTotalPizzasInOrder());
            defaultReset();
        } else {
            showAlert("Error Adding to Order", "This pizza is not added to your order.");
        }
    }

    private void defaultReset() {
        pizzaTypeSpinner.setSelection(0);
        availableToppingsAdapter.clearToppings();
        availableToppingsAdapter.addAllToppings(availableToppings);
        selectedToppingsAdapter.clearToppings();
        crustTextView.setText("Crust: ");
        priceTextView.setText("$0.00");
        smallRadioButton.setChecked(false);
        mediumRadioButton.setChecked(false);
        largeRadioButton.setChecked(false);
        pizzaImageView.setImageResource(R.drawable.pinkpizza);
        currPizza = null;
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void showSuccess(String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_light);
    }
}
