package com.tamar.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamar.restaurant.R;
import com.tamar.restaurant.adapters.MenuAdapter;
import com.tamar.restaurant.models.Dish;
import com.tamar.restaurant.models.DishCategory;
import com.tamar.restaurant.models.Order;
import com.tamar.restaurant.models.OrderItem;
import com.tamar.restaurant.models.user.User;
import com.tamar.restaurant.network.ApiService;
import com.tamar.restaurant.network.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantMenuActivity extends AppCompatActivity {

    private Long tableId;
    private Button orderButton;
    private Long currentUserId;
    private String idToken;
    private MenuAdapter menuAdapter;
    private List<Object> displayItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private String restaurantName;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        orderButton = findViewById(R.id.placeOrderButton);
        tableId = getIntent().getLongExtra("table_id", 0);
        recyclerView = findViewById(R.id.restaurantMenuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with empty list
        menuAdapter = new MenuAdapter(displayItems);
        recyclerView.setAdapter(menuAdapter);
        TextView titleText = findViewById(R.id.restaurantMenuTitle);
        restaurantName = getIntent().getStringExtra("restaurant_name"); // You pass this from previous activity
        if (restaurantName != null) {
            titleText.setText(restaurantName + " Menu");
        }
        if (tableId != 0) {
            //  fetch menu for the restaurant
            fetchMenu();
        } else {
            Toast.makeText(this, "Invalid table ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMenu() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
        idToken = getIntent().getStringExtra("id_token");

        // Fetch dishes for the given table's restaurant
        Call<List<Dish>> dishCall = apiService.getDishesByTable("Bearer " + idToken, tableId);
        dishCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {
                if (response.isSuccessful()) {
                    List<Dish> dishes = response.body();
                    LinkedHashMap<DishCategory, List<Dish>> grouped = new LinkedHashMap<>();

                    //Create a map where keys are Category from Enum
                    for (DishCategory category : DishCategory.values()) {
                        grouped.put(category, new ArrayList<>());
                    }

                    //Add array with dishes for each category
                    for (Dish dish : dishes) {
                        grouped.get(dish.getCategory()).add(dish);
                    }
                    displayItems.clear();

                    for (DishCategory category : DishCategory.values()) {
                        List<Dish> categoryDishes = grouped.get(category);
                        if (categoryDishes != null && !categoryDishes.isEmpty()) {
                            displayItems.add(category); // category header
                            displayItems.addAll(categoryDishes); // dishes under it
                        }
                    }

                    menuAdapter.notifyDataSetChanged();

                } else if (response.code() == 404) {
                    Toast.makeText(RestaurantMenuActivity.this, "Table not found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RestaurantMenuActivity.this, "Failed to load dishes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Dish>> call, Throwable t) {
                Toast.makeText(RestaurantMenuActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        orderButton.setOnClickListener(v -> {
            Map<Long, Integer> selectedDishes = menuAdapter.getDishQuantities();

            // Filter dishes where quantity > 0
            Map<Long, Integer> filteredDishes = new HashMap<>();
            for (Map.Entry<Long, Integer> entry : selectedDishes.entrySet()) {
                if (entry.getValue() > 0) {
                    filteredDishes.put(entry.getKey(), entry.getValue());
                }
            }

            if (filteredDishes.isEmpty()) {
                Toast.makeText(this, "Please select at least one dish", Toast.LENGTH_SHORT).show();
                return;
            }

            // Fetch user's id
            Call<User> userCall = apiService.getUserInfo("Bearer " + idToken);
            userCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        currentUserId = response.body().getId();
                        boolean addingDishes = getIntent().getBooleanExtra("adding_dishes", false);
                        if (addingDishes) {
                            Long orderId = getIntent().getLongExtra("order_id", 0);
                            addDishesToOrder(orderId, filteredDishes);
                        } else {
                            //Fetch current order
                            apiService.createOrder("Bearer " + idToken, tableId, currentUserId).enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    if (response.isSuccessful()&&response.body()!=null) {
                                        Long orderId = response.body().getId();
                                        addDishesToOrder(orderId, filteredDishes);
                                    } else {
                                        Toast.makeText(RestaurantMenuActivity.this, "Order creation failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {
                                    Log.e("Order", "Order creation failed", t);
                                    Toast.makeText(RestaurantMenuActivity.this, "Order creation error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    } else {
                        Toast.makeText(RestaurantMenuActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RestaurantMenuActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
    private boolean allItemsAdded = false;

    private void addDishesToOrder(Long orderId, Map<Long, Integer> filteredDishes) {
        int totalItems = filteredDishes.values().stream().mapToInt(Integer::intValue).sum();
        AtomicInteger completedCount = new AtomicInteger(0);

        for (Map.Entry<Long, Integer> entry : filteredDishes.entrySet()) {
            Long dishId = entry.getKey();
            int quantity = entry.getValue();

            for (int i = 0; i < quantity; i++) {
                apiService.addOrderItem("Bearer " + idToken, orderId, dishId).enqueue(new Callback<OrderItem>() {
                    @Override
                    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                        if (response.isSuccessful()) {
                            Log.d("Order", "Item added: " + response.body().getDish().getName());
                        } else {
                            Log.w("Order", "Failed to add item: " + response.code());
                        }
                        checkIfAllAdded(completedCount.incrementAndGet(), totalItems,orderId);
                    }

                    @Override
                    public void onFailure(Call<OrderItem> call, Throwable t) {
                        Log.e("Order", "Failed to add item", t);
                        checkIfAllAdded(completedCount.incrementAndGet(), totalItems,orderId);
                    }
                });
            }
        }
    }

    //Will run only after all dishes are entered to order
    private void checkIfAllAdded(int current, int total, Long orderId) {

        if (current == total) {
            allItemsAdded = true;
            runOnUiThread(() -> {
                Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, OrderStatusActivity.class);
                intent.putExtra("id_token", idToken);
                intent.putExtra("order_id", orderId);
                intent.putExtra("table_id", tableId);
                intent.putExtra("restaurant_name", restaurantName);
                startActivity(intent);
                finish();
            });
        }
    }

}


