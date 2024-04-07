package com.gttron.yukino.agroprotect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BiometricActivity extends AppCompatActivity {

    private static final String TAG = "BiometricAuth";
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);
        preferences = getApplicationContext().getSharedPreferences("biometric", Context.MODE_PRIVATE);
        boolean switchState = preferences.getBoolean("biometric", false);
        if (switchState){
            if (BiometricManager.from(this).canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
                Log.e(TAG, "Biometric authentication not available on this device");
                return;
            }


            executor = Executors.newSingleThreadExecutor();
            biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.e(TAG, "Authentication error: " + errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Log.i(TAG, "Authentication succeeded");
                    Intent intent = new Intent(BiometricActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Log.w(TAG, "Authentication failed");
                    Toast.makeText(BiometricActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Place your finger on the sensor")
                    .setNegativeButtonText("Cancel")
                    .build();
            biometricPrompt.authenticate(promptInfo);
        }
        else{
            Intent intent=new Intent(BiometricActivity.this,MainActivity.class);
            finish();
            startActivity(intent);
        }

    }

}
