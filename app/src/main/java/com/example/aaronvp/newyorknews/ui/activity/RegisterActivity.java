package com.example.aaronvp.newyorknews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aaronvp.newyorknews.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

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

        registerButton.setOnClickListener(v -> {
            if (validateRegistration()) {
                registerUser();
            }
        });

        loginTextView.setOnClickListener(v -> onBackPressed());
    }

    /**
     * Validate Registration
     *
     * @return boolean
     */
    private boolean validateRegistration() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailText)) {
            email.setError(getString(R.string.err_email_req));
            return false;
        }

        if (TextUtils.isEmpty(passwordText)) {
            password.setError(getString(R.string.err_password_req));
            return false;
        }

        if (passwordText.length() < 6) {
            password.setError(getString(R.string.err_password_length));
            return false;
        }

        return true;
    }

    /**
     * Register User
     */
    private void registerUser() {
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.reg_success), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ArticleListActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_err_prefix) +
                                        Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

}