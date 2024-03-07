package com.gttron.yukino.agroprotect;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private WebView webView;
    String devip;
    Switch snd,lht;
    SharedPreferences preferences1,preferences2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snd=findViewById(R.id.snd);
        lht=findViewById(R.id.lht);
        Bundle extra = getIntent().getExtras();
        try{
        devip= extra.getString("devIP");
            }
        catch (Exception e){
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("devip", Context.MODE_PRIVATE);
            devip = preferences.getString("devip", null);

        }
        snd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences1 = getApplicationContext().getSharedPreferences("sound", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putBoolean("sound", b);
                editor.apply();
                Log.d(TAG, "Switch State Saved - Key: biometric, Value: " + b);
            }
        });
        lht.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences2 = getApplicationContext().getSharedPreferences("light", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences2.edit();
                editor.putBoolean("light", b);
                editor.apply();
                Log.d(TAG, "Switch State Saved - Key: biometric, Value: " + b);
            }
        });
        SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("sound", Context.MODE_PRIVATE);
        SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("light", Context.MODE_PRIVATE);
        boolean switchSound = preferences1.getBoolean("sound", false);
        boolean switchLight=preferences2.getBoolean("light",false);
        snd.setChecked(switchSound);
        lht.setChecked(switchLight);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.loadUrl("http://"+devip+":5000/");
    }
}
