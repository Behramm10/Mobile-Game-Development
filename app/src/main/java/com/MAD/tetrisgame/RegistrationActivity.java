package com.MAD.tetrisgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText regEmail, regPassword;
    private Button registerButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        regEmail = findViewById(R.id.regEmailEditText);
        regPassword = findViewById(R.id.regPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmail.getText().toString().trim();
                String password = regPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    regPassword.setError("Password is required.");
                    return;
                }
                if (password.length() < 6) {
                    regPassword.setError("Password must be >= 6 characters.");
                    return;
                }

                // Register the user in Firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), GameManager.class));
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}