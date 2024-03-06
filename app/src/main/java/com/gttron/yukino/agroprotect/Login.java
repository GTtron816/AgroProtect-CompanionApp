package com.gttron.yukino.agroprotect;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText,devIpEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.loginemail);
        passwordEditText = findViewById(R.id.loginpw);
        devIpEditText=findViewById(R.id.devip);
        Button b1 = findViewById(R.id.loginbt);
        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validateLogin()) {
                    LoginUser();
                }
            }
        });
        TextView regt = findViewById(R.id.noreg);
        regt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Login.this, Register.class);
//                startActivity(intent);
            }
        });

    }

    private boolean validateLogin() {
        boolean isValid = true;
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String ip= devIpEditText.getText().toString();
        // Check if fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            isValid = false;
            if (email.isEmpty()) {
                emailEditText.setError("Email is required.");
            }
            if (ip.isEmpty()) {
                devIpEditText.setError("Device IP/ URL required.");
            }
            if (password.isEmpty()) {
                passwordEditText.setError("Password is required.");
            }
        }


        return isValid;
    }




    private void LoginUser() {
        EditText em = findViewById(R.id.loginemail);
        EditText pw = findViewById(R.id.loginpw);
        String email = em.getText().toString();
        String password = pw.getText().toString();
        String ip= devIpEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            em.setError("Email is required.");
            em.requestFocus();
            return;
        }

        if (!isValidEmail(email)) {
            em.setError("Invalid email address.");
            em.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pw.setError("Password is required.");
            pw.requestFocus();
            return;
        }

        if (!isValidPassword(password)) {
            pw.setError("Password must have at least 8 characters, including special characters, digits, and both uppercase and lowercase letters.");
            pw.requestFocus();
            return;
        }
        if(TextUtils.isEmpty((ip))){
            pw.setError("Device IP/URL required.");
            pw.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private boolean isValidPassword(String password) {
        // Password must have at least 8 characters with a combination of special characters, digits, and both uppercase and lowercase letters.
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        return passwordPattern.matcher(password).matches();
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
