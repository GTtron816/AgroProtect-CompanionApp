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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        Button regButton=findViewById(R.id.reg);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });




    }

    private void registerUser() {
        EditText em=findViewById(R.id.usremail);
        EditText pw=findViewById(R.id.regpw);
        EditText username=findViewById(R.id.username);
        String email=em.getText().toString();
        String name=username.getText().toString();
        String password=pw.getText().toString();
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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            user.updateProfile(profileUpdates);
                            Toast.makeText(Register.this, "User Created.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Register.this,MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
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
