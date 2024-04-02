package com.gttron.yukino.agroprotect;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    ImageButton b1;
    private FirebaseAuth mAuth;
    String email;
    String name;
    Switch biometric;
    TextView t1, t2;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        biometric=findViewById(R.id.biometric);
        biometric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences = getApplicationContext().getSharedPreferences("biometric", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("biometric", b);
                editor.apply();
                Log.d(TAG, "Switch State Saved - Key: biometric, Value: " + b);
            }
        });
        // In both activities
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("biometric", Context.MODE_PRIVATE);
        boolean switchState = preferences.getBoolean("biometric", false);
        biometric.setChecked(switchState);
        b1=findViewById(R.id.logoutbt);
        t1 = findViewById(R.id.emailtext);
        t2 = findViewById(R.id.username);
        ImageButton bt1=findViewById(R.id.btn1);
        ImageButton bt2=findViewById(R.id.btn2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Profile.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Profile.this,Control.class);
                finish();
                startActivity(intent);


            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            name = currentUser.getDisplayName();
            email = currentUser.getEmail();
        }
        t1.setText(email);
        t2.setText(name);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(Profile.this, Login.class);
                finish();
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}