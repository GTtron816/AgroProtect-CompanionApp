package com.gttron.yukino.agroprotect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    String devip;
    Switch snd,lht;
    ImageButton btn1,btn2,btn3;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snd=findViewById(R.id.snd);
        lht=findViewById(R.id.lht);
        btn2=findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Control.class);
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

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.loadUrl("http://"+devip+":5000/");
    }

}
