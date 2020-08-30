package com.example.aaronvp.newyorknews.ui.activity;

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegistration()) {
                    login();
                }
            }
        });

        registerTextView = this.findViewById(R.id.textViewRegister);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
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

    private void login() {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()) {
                              Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                              startActivity(new Intent(getApplicationContext(), ArticleListActivity.class));
                              finish();
                          } else {
                              Toast.makeText(getApplicationContext(), "Error! " + task.getException().getMessage(),
                                      Toast.LENGTH_SHORT).show();
                          }
                    }
                });
    }

}