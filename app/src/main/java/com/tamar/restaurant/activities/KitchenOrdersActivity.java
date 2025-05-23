package com.tamar.restaurant.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamar.restaurant.R;
import com.tamar.restaurant.adapters.KitchenDishAdapter;
import com.tamar.restaurant.models.OrderItem;
import com.tamar.restaurant.models.OrderStatus;
import com.tamar.restaurant.models.Restaurant;
import com.tamar.restaurant.network.ApiService;
import com.tamar.restaurant.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KitchenOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KitchenDishAdapter adapter;
    private List<OrderItem> items = new ArrayList<>();
    private ApiService apiService;
    private String idToken;
    private Long kitchen_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        idToken = getIntent().getStringExtra("id_token");
        apiService = RetrofitClient.getClient().create(ApiService.class);

        recyclerView = findViewById(R.id.kitchenRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new KitchenDishAdapter(items, (item, newStatus) -> {
            updateItemStatus(item, newStatus);
        });
        recyclerView.setAdapter(adapter);

        fetchKitchenItems();
    }

    private void fetchKitchenItems() {
        kitchen_id = getIntent().getLongExtra("user_id", 0);
        apiService.getRestaurantFromKitchenId("Bearer " + idToken,kitchen_id).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Long restaurantId = response.body().getId();

                    apiService.getItemsByStatusAndRestaurant("Bearer " + idToken,restaurantId, List.of(OrderStatus.ORDERED, OrderStatus.COOKING)).enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                items.clear();
                                for (OrderItem item : response.body()) {
                                    items.add(item);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                            Toast.makeText(KitchenOrdersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(KitchenOrdersActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Toast.makeText(KitchenOrdersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateItemStatus(OrderItem item, OrderStatus newStatus) {
        apiService.updateOrderItemStatus("Bearer " + idToken, item.getId(), newStatus)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                        if (response.isSuccessful()) {
                            // Refresh list
                             fetchKitchenItems();
                        } else {
                            Toast.makeText(KitchenOrdersActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderItem> call, Throwable t) {
                        Toast.makeText(KitchenOrdersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


