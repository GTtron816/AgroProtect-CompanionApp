package com.gttron.yukino.agroprotect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    private WebView webView;
    String devip;
    Button b1;
    ImageButton btn1,btn2,btn3;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupDialog();
            }
        });
        btn2=findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Control.class);
                finish();
                startActivity(intent);
            }
        });
        btn3=findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Profile.class);
                finish();
                startActivity(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("detected_classes");

        Bundle extra = getIntent().getExtras();
        try{
        devip= extra.getString("devIP");
            }
        catch (Exception e){
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("devip", Context.MODE_PRIVATE);
            devip = preferences.getString("devip", null);
        }

       setStream(devip);
    }

    private void setStream(String devip) {
        webView = findViewById(R.id.webView);
        webView.reload();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.loadUrl("http://"+devip+":5000/");
    }

    private void showPopupDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.refresh_stream, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userInput = editText.getText().toString();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("devip", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("devip", userInput);
                editor.apply();
                setStream(userInput);
                alertDialog.dismiss();
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
