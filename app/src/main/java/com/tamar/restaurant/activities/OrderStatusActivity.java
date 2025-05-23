package com.tamar.restaurant.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tamar.restaurant.R;
import com.tamar.restaurant.adapters.OrderItemStatusAdapter;
import com.tamar.restaurant.models.OrderItem;
import com.tamar.restaurant.network.ApiService;
import com.tamar.restaurant.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusActivity extends AppCompatActivity {

    private RecyclerView statusRecyclerView;
    private Long orderId;
    private Long tableId;
    private String restaurantName;
    private String idToken;
    private ApiService apiService;
    private List<OrderItem> items;
    ;
    private OrderItemStatusAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        statusRecyclerView = findViewById(R.id.orderStatusRecyclerView);
        Button addMoreButton = findViewById(R.id.addMoreDishesButton);

        //Getting data from previous page
        orderId = getIntent().getLongExtra("order_id", 0);
        tableId = getIntent().getLongExtra("table_id", 0);
        restaurantName = getIntent().getStringExtra("restaurant_name");
        idToken = getIntent().getStringExtra("id_token");

        apiService = RetrofitClient.getClient().create(ApiService.class);

        //Creating a list of order items to show in the recycler view
        statusRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderItemStatusAdapter(items);
        statusRecyclerView.setAdapter(adapter);

        fetchOrderItems();

        //When clicking going back to menu activity but still having the same order_id for not creating a new order
        addMoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderStatusActivity.this, RestaurantMenuActivity.class);
            intent.putExtra("table_id", tableId);
            intent.putExtra("restaurant_name", restaurantName);
            intent.putExtra("id_token", idToken);
            intent.putExtra("adding_dishes", true);
            intent.putExtra("order_id", orderId);
            startActivity(intent);
        });
    }

    private void fetchOrderItems() {
        apiService.getOrderItemsByOrder("Bearer " + idToken, orderId)
                .enqueue(new Callback<>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            items = new ArrayList<>(response.body());
                            adapter.setOrderItems(items);
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(OrderStatusActivity.this, "Failed to load order items", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                        Toast.makeText(OrderStatusActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
