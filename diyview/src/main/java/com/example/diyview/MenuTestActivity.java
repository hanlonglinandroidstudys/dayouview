package com.example.diyview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.diyview.customview.PopMenu.PopMenuView;

public class MenuTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_test);
        initView();
    }

    private void initView() {
        PopMenuView btn_menu=findViewById(R.id.btn_menu);
        Button btn1=new Button(this);
        btn1.setText("按钮1");
        btn1.setBackgroundResource(R.drawable.back3);
        Button btn2=new Button(this);
        btn2.setText("按钮2");
        btn2.setBackgroundResource(R.drawable.back3);
        Button btn3=new Button(this);
        btn3.setText("按钮3");
        btn3.setBackgroundResource(R.drawable.back3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuTestActivity.this, "按下按钮1", Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuTestActivity.this, "按下按钮2", Toast.LENGTH_SHORT).show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuTestActivity.this, "按下按钮3", Toast.LENGTH_SHORT).show();
            }
        });

        btn_menu.addItem(btn1);
        btn_menu.addItem(btn2);
        btn_menu.addItem(btn3);
    }
}
