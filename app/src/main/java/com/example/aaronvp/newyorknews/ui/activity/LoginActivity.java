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

public class LoginActivity extends AppCompatActivity {

    TextView registerTextView;
    EditText email;
    EditText password;
    Button loginButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), ArticleListActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            if (validateRegistration()) {
                login();
            }
        });

        registerTextView = this.findViewById(R.id.textViewRegister);
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
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

    private void login() {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), ArticleListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_err_prefix)
                                        + Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}