package com.tamar.restaurant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tamar.restaurant.R;
//import com.tamar.restaurant.models.user.User;
import com.tamar.restaurant.models.user.User;
import com.tamar.restaurant.network.ApiService;
import com.tamar.restaurant.network.RetrofitClient;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private String idToken;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))  // from google-services.json
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.loginGoogleButton).setOnClickListener(v -> signInWithGoogle());
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && data != null) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    idToken = account.getIdToken();
                    authenticateWithBackend(idToken);
                }
            } catch (ApiException e) {
                Log.e("LoginActivity", "Google sign in failed", e);
                Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void authenticateWithBackend(String idToken) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = apiService.loginWithGoogle("Bearer " + idToken);


        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call,
                                   @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String role = String.valueOf(response.body().getRole());
                    userId = response.body().getId();
                    navigateByRole(role);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Navigate to user's home page based on their role
    private void navigateByRole(String role) {
        Intent intent;
        switch (role) {
            case "MANAGER":
                intent = new Intent(this, ManagerHomeActivity.class);
                break;
            case "WAITER":
                intent = new Intent(this, WaiterTablesActivity.class);
                break;
            case "KITCHEN":
                intent = new Intent(this, KitchenOrdersActivity.class);
                break;
            default:
                intent = new Intent(this, QRScannerActivity.class);
        }
        intent.putExtra("id_token", idToken);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }
}
