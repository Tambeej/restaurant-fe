package com.tamar.restaurant.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.tamar.restaurant.R;
import com.tamar.restaurant.models.Restaurant;
import com.tamar.restaurant.models.WaiterRestaurant;
import com.tamar.restaurant.network.ApiService;
import com.tamar.restaurant.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerHomeActivity extends AppCompatActivity {

    private EditText waiterEmailEditText;
    private Button assignWaiterButton;
    private Long restaurantId;
    private ApiService apiService;
    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

        waiterEmailEditText = findViewById(R.id.waiterEmailEditText);
        assignWaiterButton = findViewById(R.id.assignWaiterButton);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        idToken = GoogleSignIn.getLastSignedInAccount(this).getIdToken();
        if (idToken != null) {
            idToken = "Bearer " + idToken;
        }

        // Get my restaurant
        apiService.getMyRestaurant(idToken).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                if (response.isSuccessful() && response.body() != null) {
                    restaurantId = response.body().getId();
                } else {
                    Toast.makeText(ManagerHomeActivity.this, "Failed to get restaurant", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Toast.makeText(ManagerHomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        assignWaiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = waiterEmailEditText.getText().toString().trim();
                if (!email.isEmpty() && restaurantId != null) {
                    assignWaiter(email);
                } else {
                    Toast.makeText(ManagerHomeActivity.this, "Enter email and ensure restaurant is loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void assignWaiter(String email) {
        apiService.assignWaiterByEmail(idToken, restaurantId, email).enqueue(new Callback<WaiterRestaurant>() {
            @Override
            public void onResponse(Call<WaiterRestaurant> call, Response<WaiterRestaurant> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManagerHomeActivity.this, "Waiter assigned successfully", Toast.LENGTH_SHORT).show();
                    waiterEmailEditText.setText("");
                } else {
                    Toast.makeText(ManagerHomeActivity.this, "Failed to assign waiter", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WaiterRestaurant> call, Throwable t) {
                Toast.makeText(ManagerHomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}