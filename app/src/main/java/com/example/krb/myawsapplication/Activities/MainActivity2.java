package com.example.krb.myawsapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krb.myawsapplication.R;
import com.example.krb.myawsapplication.Supporting.Crypto;

public class MainActivity2 extends AppCompatActivity {

    protected TextView textViewDataset;

    protected Button buttonCloud;
    protected Button buttonVersion;

    protected Activity that;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        that = this;
        context = getApplicationContext();

        initSetup();
    }

    protected void initSetup() {
        setupButtons();
        setupTextViews();
    }

    protected void setupButtons() {
        setupButtonCloud();
        setupButtonVersion();
    }

    protected void setupTextViews() {
        setupTextViewDataset();
    }

    protected void setupTextViewDataset() {
        textViewDataset = (TextView) findViewById(R.id.textview_dataset);
    }

    protected void setupButtonCloud() {
        buttonCloud = (Button) findViewById(R.id.button_cloud);
        buttonCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentApp = new Intent(MainActivity2.this, CloudActivity.class);
                MainActivity2.this.startActivity(intentApp);
            }

        });
    }

    protected void setupButtonVersion() {
        buttonVersion = (Button) findViewById(R.id.button_version);
        buttonVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crypto crypto = new Crypto();
                Toast.makeText(that.getApplicationContext(), crypto.getAndroidVersion(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
