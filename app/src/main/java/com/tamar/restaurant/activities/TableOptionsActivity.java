package com.tamar.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tamar.restaurant.R;
import com.tamar.restaurant.models.CallType;
import com.tamar.restaurant.models.WaiterCall;
import com.tamar.restaurant.network.ApiService;
import com.tamar.restaurant.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableOptionsActivity extends AppCompatActivity {

    private Long tableId;
    private String idToken;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_options);

        tableId = getIntent().getLongExtra("table_id", 0);
        idToken = getIntent().getStringExtra("id_token");

        apiService = RetrofitClient.getClient().create(ApiService.class);

        Button callWaiterBtn = findViewById(R.id.callWaiterButton);
        Button waterBtn = findViewById(R.id.requestWaterButton);
        Button billBtn = findViewById(R.id.requestBillButton);
        Button orderBtn = findViewById(R.id.orderYourselfButton);

        callWaiterBtn.setOnClickListener(v -> sendAssistanceRequest(CallType.HELP));
        waterBtn.setOnClickListener(v -> sendAssistanceRequest(CallType.WATER));
        billBtn.setOnClickListener(v -> sendAssistanceRequest(CallType.BILL));


        orderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(TableOptionsActivity.this, RestaurantMenuActivity.class);
            intent.putExtra("table_id", tableId);
            intent.putExtra("restaurant_name", "Restaurant");
            intent.putExtra("id_token", idToken);
            startActivity(intent);
        });
    }

    private void sendAssistanceRequest(CallType type) {
        apiService.createCall("Bearer " + idToken, tableId, type).enqueue(new Callback<WaiterCall>() {
            @Override
            public void onResponse(Call<WaiterCall> call, Response<WaiterCall> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TableOptionsActivity.this, "Request sent: " + type, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableOptionsActivity.this, "Failed to send request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WaiterCall> call, Throwable t) {
                Toast.makeText(TableOptionsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
