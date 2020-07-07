package com.example.aaronvp.newyorknews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aaronvp.newyorknews.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    Button registerButton;
    TextView loginTextView;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginTextView = this.findViewById(R.id.textViewLogin);
        firstName = this.findViewById(R.id.editTextFirstName);
        lastName = this.findViewById(R.id.editTextLastName);
        email = this.findViewById(R.id.editTextEmail);
        password = this.findViewById(R.id.editTextPassword);
        registerButton = this.findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegistration()) {
                    registerUser();
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Validate Registration
     * @return boolean
     */
    private boolean validateRegistration() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailText)) {
            email.setError("Email required");
            return false;
        }

        if (TextUtils.isEmpty(passwordText)) {
            password.setError("Password required");
            return false;
        }

        if (passwordText.length() < 6) {
            password.setError("Password must be >= 6 characters");
            return false;
        }

        return true;
    }

    /**
     * Register User
     */
    private void registerUser() {
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User created successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Error! " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}