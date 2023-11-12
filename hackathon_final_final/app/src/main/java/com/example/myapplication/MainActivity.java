package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btn_login;
    private AppCompatButton btn_register;

    private ActivityResultLauncher<Intent> launcherLogin;
    private ActivityResultLauncher<Intent> launcherRegister;

    private ActivityResultCallback<Boolean> callbackForLogin = new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isSuccessful) {
            handleLoginResult(isSuccessful);
        }
    };

    private ActivityResultCallback<Boolean> callbackForRegister = new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isSuccessful) {
            handleRegisterResult(isSuccessful);
        }
    };

    private void initComponents() {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        launcherLogin = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleActivityResult(result, callbackForLogin)
        );

        launcherRegister = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> handleActivityResult(result, callbackForRegister)
        );
    }

    private void handleActivityResult(ActivityResult result, ActivityResultCallback<Boolean> callback) {
        if (result.getResultCode() == RESULT_OK) {
            callback.onActivityResult(true);
        } else {
            callback.onActivityResult(false);
        }
    }

    private void handleLoginResult(boolean isSuccessful) {

        if (isSuccessful) {
            // Do something when login is successful

        } else {
            // Do something when login is not successful
        }
    }

    private void handleRegisterResult(boolean isSuccessful) {

        if (isSuccessful) {
            // Do something when registration is successful
        } else {
            // Do something when registration is not successful
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        btn_login.setOnClickListener(setLoginClick());
        btn_register.setOnClickListener(setRegisterClick());

    }

    private View.OnClickListener setLoginClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                launcherLogin.launch(loginIntent);
            }
        };
    }

    private View.OnClickListener setRegisterClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                launcherRegister.launch(registerIntent);
            }
        };
    }
}
