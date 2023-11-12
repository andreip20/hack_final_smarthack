package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatButton btn_ra_register;
    private AppCompatButton btn_ra_cancel;
    private TextInputEditText tiet_username;
    private TextInputEditText tiet_parola;
    private TextInputEditText tiet_confirmareparola;

    private DatabaseHelper dbHelper;

    private void initComponents() {
        btn_ra_register = findViewById(R.id.btn_ra_register);
        btn_ra_cancel = findViewById(R.id.btn_ra_cancel);
        tiet_username = findViewById(R.id.tiet_register_user);
        tiet_parola = findViewById(R.id.tiet_register_parola);
        tiet_confirmareparola = findViewById(R.id.tiet_register_confirmareparola);
        dbHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();

        btn_ra_register.setOnClickListener(setRegisterClick());
        btn_ra_cancel.setOnClickListener(setCancelClick());
    }

    private View.OnClickListener setRegisterClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = tiet_username.getText().toString();
                String parola = tiet_parola.getText().toString();
                String confirmareParola = tiet_confirmareparola.getText().toString();

                if (validateInputs(username, parola, confirmareParola)) {
                    // Validation successful, proceed with database insertion
                    String encryptedPassword = encryptPassword(parola);
                    boolean res = dbHelper.addUser(username, encryptedPassword);

                    if (res) {
                        finish();
                    }
                    // Return to the previous activity or finish this activity as needed
                    else {
                        showToast("Something went wrong");
                    }
                }
            }
        };
    }

    private View.OnClickListener setCancelClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle cancel button click (e.g., return to the previous activity)
                finish();
            }
        };
    }

    private boolean validateInputs(String username, String parola, String confirmareParola) {
        if (username.length() < 5) {
            showToast("Username must be at least 5 characters long");
            return false;
        }

        if (parola.length() < 8) {
            showToast("Password must be at least 8 characters long");
            return false;
        }

        if (!parola.equals(confirmareParola)) {
            showToast("Passwords do not match");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            // Convert the byte to hex format
            StringBuilder hexString = new StringBuilder();
            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
