package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    public static String username;

    private AppCompatButton btn_la_login;
    private AppCompatButton btn_la_cancel;
    private TextInputEditText tiet_username;
    private TextInputEditText tiet_parola;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        btn_la_login.setOnClickListener(setLoginClick());
        btn_la_cancel.setOnClickListener(setCancelClick());
    }

    private View.OnClickListener setLoginClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = tiet_username.getText().toString();
                String enteredPassword = tiet_parola.getText().toString();

                // Fetch the hashed password from the database based on the entered username
                String encryptedPassword = encryptPassword(enteredPassword);
                boolean res = dbHelper.checkUsernameAndPassword(username, encryptedPassword);

                if (res) {
                    showToast("Login succesfully");
                    LoginActivity.username=((EditText)findViewById(R.id.tiet_login_user)).getText().toString();
                    Intent intent=new Intent(LoginActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
                else {
                    showToast("Invalid credentials");
                }
            }
        };
    }

    private View.OnClickListener setCancelClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        };
    }

    private String getStoredHashedPassword(String username) {
        // TODO: Fetch the hashed password from the database based on the username
        // Replace this with your actual database query logic
        // In a real application, you would query your database to get the hashed password associated with the username
        // For now, I'll just use a placeholder value. Replace this with your actual database query logic.

        return "";


    }

    private boolean validatePassword(String enteredPassword, String storedHashedPassword) {
        // TODO: Implement password validation by comparing the entered password with the stored hashed password
        // Use the same encryption method (SHA-256) as you used during registration
        String enteredHashedPassword = encryptPassword(enteredPassword);
        return enteredHashedPassword != null && enteredHashedPassword.equals(storedHashedPassword);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void initComponents() {
        btn_la_login = findViewById(R.id.btn_la_login);
        btn_la_cancel = findViewById(R.id.btn_la_cancel);
        tiet_username = findViewById(R.id.tiet_login_user);
        tiet_parola = findViewById(R.id.tiet_login_parola);
        dbHelper = new DatabaseHelper(this);
    }
}
