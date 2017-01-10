package com.easy.android.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.easy.android.log.XLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View btn = findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        XLog.d("MainActivity", "create a new repository on the command line");
        XLog.d("MainActivity", "create a new repository on the command line");
        XLog.d("MainActivity", "create a new repository on the command line");
        XLog.d("MainActivity", "create a new repository on the command line");
        XLog.d("MainActivity", "create a new repository on the command line");
    }
}
