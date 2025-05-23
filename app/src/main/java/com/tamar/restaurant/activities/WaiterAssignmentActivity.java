//package com.tamar.restaurant.activities;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Spinner;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.tamar.restaurant.R;
//import com.tamar.restaurant.models.RestaurantTable;
//import
//
//com.tamar.restaurant.models.user.User;
//import com.tamar.restaurant.models.user.Waiter;
//import com.tamar.restaurant.network.ApiService;
//import com.tamar.restaurant.network.RetrofitClient;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class WaiterAssignmentActivity extends AppCompatActivity {
//
//    private LinearLayout assignmentsLayout;
//    private Spinner waiterSpinner, tableSpinner;
//    private Button assignButton;
//
//    private List<Waiter> waiters = new ArrayList<>();
//    private List<RestaurantTable> availableTables = new ArrayList<>();
//
//    private ArrayAdapter<Waiter> waiterAdapter;
//    private ArrayAdapter<RestaurantTable> tableAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_waiter_assignment);
//
//        //For connecting to the API
//        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//        String idToken = GoogleSignIn.getLastSignedInAccount(this).getIdToken();
//
//        assignmentsLayout = findViewById(R.id.assignmentsLayout);
//        waiterSpinner = findViewById(R.id.waiterSpinner);
//        tableSpinner = findViewById(R.id.tableSpinner);
//        assignButton = findViewById(R.id.assignButton);
//
//        //Get all waiters in restaurant
//        apiService.getWaitersInRestaurant("Bearer "+idToken).enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<List<Waiter>> call, Response<List<Waiter>> response) {
//                if (response.isSuccessful()) {
//                    waiters = response.body();
//                }else {
//                    Toast.makeText(WaiterAssignmentActivity.this, "Failed to load waiters", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Waiter>> call, Throwable t) {
//                Toast.makeText(WaiterAssignmentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//
//        availableTables.add(new RestaurantTable(1L, 1));
//
//        waiterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, waiters);
//        tableAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableTables);
//
//        waiterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        waiterSpinner.setAdapter(waiterAdapter);
//        tableSpinner.setAdapter(tableAdapter);
//
//        assignButton.setOnClickListener(v -> assignWaiterToTable());
//    }
//
//    private void assignWaiterToTable() {
//        User waiter = (User) waiterSpinner.getSelectedItem();
//        RestaurantTable table = (RestaurantTable) tableSpinner.getSelectedItem();
//
//        if (waiter != null && table != null) {
//            View row = getLayoutInflater().inflate(R.layout.assignment_item, null);
//            TextView info = row.findViewById(R.id.assignmentInfo);
//            Button removeBtn = row.findViewById(R.id.removeButton);
//
//            info.setText(waiter.getUserName() + " ➝ Table " + table.getTableNumber());
//
//            removeBtn.setOnClickListener(v -> assignmentsLayout.removeView(row));
//
//            assignmentsLayout.addView(row);
//            availableTables.remove(table);
//            tableAdapter.notifyDataSetChanged();
//        }
//    }
//}
