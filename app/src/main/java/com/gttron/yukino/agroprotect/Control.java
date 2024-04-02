package com.gttron.yukino.agroprotect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class Control extends AppCompatActivity {
    ThemedToggleButtonGroup th1;
    ThemedButton light,sound;
    SharedPreferences preferences1,preferences2;
    ImageButton btn1,btn2,btn3;
    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        btn1=findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Control.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        btn3=findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Control.this,Profile.class);
                finish();
                startActivity(intent);
            }
        });
        light=findViewById(R.id.light);
        sound=findViewById(R.id.sound);
        th1 = findViewById(R.id.tags);
        preferences1 = getApplicationContext().getSharedPreferences("sound", Context.MODE_PRIVATE);
        preferences2 = getApplicationContext().getSharedPreferences("light", Context.MODE_PRIVATE);
        boolean switchSound = preferences1.getBoolean("sound", false);
        boolean switchLight=preferences2.getBoolean("light",false);
        if(switchLight){
            th1.selectButton(R.id.light);
        }
        if (switchSound){
            th1.selectButton(R.id.sound);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("detected_classes");
        th1.setOnSelectListener((ThemedButton btn) -> {
            if(btn.isSelected()){
            if(btn.getId()==R.id.sound){
                preferences1 = getApplicationContext().getSharedPreferences("sound", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putBoolean("sound", true);
                editor.apply();
                updateSswitchValue(true);
            }
            if(btn.getId()==R.id.light){
                preferences2 = getApplicationContext().getSharedPreferences("light", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences2.edit();
                editor.putBoolean("light", true);
                editor.apply();
                updateLswitchValue(true);

            }

            }
            else{
                if(btn.getId()==R.id.sound){
                    preferences1 = getApplicationContext().getSharedPreferences("sound", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putBoolean("sound", false);
                    editor.apply();
                    updateSswitchValue(false);
                }
                if(btn.getId()==R.id.light){
                    preferences2 = getApplicationContext().getSharedPreferences("light", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences2.edit();
                    editor.putBoolean("light", false);
                    editor.apply();
                    updateLswitchValue(false);
                }

            }
            return kotlin.Unit.INSTANCE;
        });


    }
    private void updateSswitchValue(boolean newSwitchValue) {
        // Update the switch value in Firebase RTDB
        databaseReference.child("Sswitch").setValue(newSwitchValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    private void updateLswitchValue(boolean newSwitchValue) {
        // Update the switch value in Firebase RTDB
        databaseReference.child("lswitch").setValue(newSwitchValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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