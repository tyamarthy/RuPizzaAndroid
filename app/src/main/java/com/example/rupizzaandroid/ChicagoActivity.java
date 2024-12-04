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

public class ChicagoActivity extends AppCompatActivity {

    private Spinner pizzaTypeSpinner;
    private RecyclerView availableToppingsRecyclerView;
    private RecyclerView selectedToppingsRecyclerView;
    private TextView crustTextView;
    private TextView priceTextView;
    private RadioButton smallRadioButton;
    private RadioButton mediumRadioButton;
    private RadioButton largeRadioButton;
    private ImageView pizzaImageView;
    private Button backButton;
    private Button orderButton;
    private Button addToppingButton;
    private Button removeToppingButton;

    private Pizza currPizza;
    private ChicagoPizza chicagoPizzaFactory = new ChicagoPizza();
    private Order currentOrder;

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
        setContentView(R.layout.chicago_activity);

        initializeViews();
        setupSpinner();
        setupRadioButtons();
        setupRecyclerViews();
        setupButtons();
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

    private void setupSpinner() {
        String[] pizzaTypes = {"Select Pizza Type...", "Deluxe", "BBQ Chicken", "Meatzza", "Build your own"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pizzaTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pizzaTypeSpinner.setAdapter(adapter);

        pizzaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPizzaType = (String) parent.getItemAtPosition(position);
                updatePizzaSelection(selectedPizzaType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRadioButtons() {
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

    private void setupRecyclerViews() {
        availableToppingsAdapter = new ToppingAdapter(availableToppings, topping -> {
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                if (selectedToppingsAdapter.getItemCount() > 6) {
                    showAlert("Toppings Limit Reached", "You are only allowed to select up to 7 toppings.");
                    return;
                }
                selectedToppingsAdapter.addTopping(topping);
                availableToppingsAdapter.removeTopping(topping);
                currPizza.addTopping(topping);
                updatePrice();
            }
        });

        selectedToppingsAdapter = new ToppingAdapter(new ArrayList<>(), topping -> {
            if (currPizza != null && currPizza.getClass().getSimpleName().equals("BuildYourOwn")) {
                availableToppingsAdapter.addTopping(topping);
                selectedToppingsAdapter.removeTopping(topping);
                currPizza.getToppings().remove(topping);
                updatePrice();
            }
        });

        availableToppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        availableToppingsRecyclerView.setAdapter(availableToppingsAdapter);
    }

    private void setupButtons() {
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        orderButton.setOnClickListener(v -> addPizzaToOrder());
    }

    private void updatePizzaSelection(String pizzaType) {
        availableToppingsAdapter.clearToppings();
        availableToppingsAdapter.addAllToppings(availableToppings);
        selectedToppingsAdapter.clearToppings();

        switch (pizzaType) {
            case "Deluxe":
                currPizza = chicagoPizzaFactory.createDeluxe();
                break;
            case "BBQ Chicken":
                currPizza = chicagoPizzaFactory.createBBQChicken();
                break;
            case "Meatzza":
                currPizza = chicagoPizzaFactory.createMeatzza();
                break;
            case "Build your own":
                currPizza = chicagoPizzaFactory.createBuildYourOwn();
                break;
            default:
                currPizza = null;
                return;
        }

        crustTextView.setText("Crust: " + currPizza.getCrust());

        if (!pizzaType.equals("Build your own")) {
            selectedToppingsAdapter.addAllToppings(currPizza.getToppings());
        }

        smallRadioButton.setChecked(false);
        mediumRadioButton.setChecked(false);
        largeRadioButton.setChecked(false);
        updatePrice();
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

    private void addPizzaToOrder() {
        String selectedPizza = (String) pizzaTypeSpinner.getSelectedItem();

        if ("Select Pizza Type...".equals(selectedPizza)) {
            showAlert("Cannot Add to Order", "Please select your pizza type to proceed");
            return;
        }

        if (!(smallRadioButton.isChecked() || mediumRadioButton.isChecked() || largeRadioButton.isChecked())) {
            showAlert("Cannot Add to Order", "You must select a size for the pizza to proceed");
            return;
        }

        if (currPizza != null) {
            if (currentOrder == null) {
                currentOrder = new Order(); // Initialize order if not already done
            }
            currentOrder.addAPizza(currPizza);
            showSuccess("Pizza Added to Order!", "Pizza added successfully!\nTotal pizzas in order: " + currentOrder.getPizzas().size());
            resetFields();
        } else {
            showAlert("Error Adding to Order", "Unable to add this pizza to your order");
        }
    }

    private void resetFields() {
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

    public void setOrder(Order order) {
        this.currentOrder = order;
    }
}