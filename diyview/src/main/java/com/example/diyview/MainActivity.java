package com.example.diyview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_e2eview:
                go(E2EActivity.class);
                break;
            case R.id.btn_routeview:
                go(RoutActivity.class);
                break;
            case R.id.btn_networkview:
                go(NetworkViewActivity.class);
                break;
        }
    }

    private void go(Class clazz) {
        startActivity(new Intent(MainActivity.this, clazz));
    }
}
