package com.tamar.restaurant.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.tamar.restaurant.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.tamar.restaurant.models.Restaurant;
import com.tamar.restaurant.network.ApiService;
import com.tamar.restaurant.network.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRScannerActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ProcessCameraProvider cameraProvider;
    private BarcodeScanner scanner;
    private String restaurantName;
    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idToken =  getIntent().getStringExtra("id_token");

        setContentView(R.layout.activity_qr_scanner);

        previewView = findViewById(R.id.previewView);
        scanner = BarcodeScanning.getClient();
        //For scanning table's QR code
        startCamera();
        //For entering table's id number
        EditText manualTableIdInput = findViewById(R.id.manualTableIdInput);
        Button tableButton = findViewById(R.id.testButton);

        tableButton.setOnClickListener(v -> {

            String tableIdStr = manualTableIdInput.getText().toString().trim();
            if (!tableIdStr.isEmpty()) {
                Long tableId = Long.parseLong(tableIdStr);
                Intent intent = new Intent(QRScannerActivity.this, RestaurantMenuActivity.class);
                //TODO add more option to client (ask waiter for water, ordering for him etc)
//                Intent intent = new Intent(QRScannerActivity.this, TableOptionsActivity.class);
                intent.putExtra("table_id", tableId);
                intent.putExtra("id_token", idToken);
                //For getting restaurant's name and menu
                getRestaurantAndRedirect(tableId);
                intent.putExtra("restaurant_name", restaurantName);
                //Go to restaurant's menu
                startActivity(intent);
                finish();


            } else {
                Toast.makeText(this, "Please enter a table ID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraUseCases() {
        ImageAnalysis analysis = new ImageAnalysis.Builder().build();
        analysis.setAnalyzer(ContextCompat.getMainExecutor(this), imageProxy -> {
            @SuppressLint("UnsafeOptInUsageError")
            InputImage image = InputImage.fromMediaImage(Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());

            scanner.process(image)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            String rawValue = barcode.getRawValue();
                            if (rawValue != null) {
                                Log.d("QR", "Scanned: " + rawValue);
                                imageProxy.close();

                                try {
                                    Long tableId = Long.parseLong(rawValue); //QR has a table ID
                                    getRestaurantAndRedirect(tableId);
                                } catch (NumberFormatException e) {
                                    Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
                                }

                                return;
                            }
                        }
                        imageProxy.close();
                    })
                    .addOnFailureListener(e -> imageProxy.close());

        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, analysis);

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
    }

    private void getRestaurantAndRedirect(Long tableId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getRestaurantFromTableId("Bearer " + idToken, tableId).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call,
                                   @NonNull Response<Restaurant> response) {
                if (response.isSuccessful() && response.body() != null) {

                    restaurantName = response.body().getName();
                } else if (response.code() == 404) {
                    Toast.makeText(QRScannerActivity.this, "Table not found. Please enter a valid table ID.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QRScannerActivity.this, "failed retrieving restaurant name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Restaurant> call, Throwable t) {
                Toast.makeText(QRScannerActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String qrResult = data.getStringExtra("SCAN_RESULT"); // this depends on your scanner

            if (qrResult != null && qrResult.contains("table_id=")) {
                Uri uri = Uri.parse(qrResult);
                String tableId = uri.getQueryParameter("table_id");

                // Start MenuActivity and pass the table_id
                Intent intent = new Intent(QRScannerActivity.this, RestaurantMenuActivity.class);
                //                Intent intent = new Intent(QRScannerActivity.this, TableOptionsActivity.class);

                intent.putExtra("table_id", Long.parseLong(tableId));
                intent.putExtra("restaurant_name", restaurantName);
                //Go to restaurant's menu
                startActivity(intent);
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid QR code", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
