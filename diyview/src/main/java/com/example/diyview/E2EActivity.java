package com.example.diyview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.diyview.customview.e2eglotview2.E2ETvGplotView;
import com.example.diyview.customview.e2eglotview2.OnTvNodeItemClickListener;
import com.example.diyview.customview.e2eglotview2.TvNode;
import com.example.diyview.customview.e2egplotview.E2EGplotView;
import com.example.diyview.customview.e2egplotview.E2ENode;
import com.example.diyview.customview.e2egplotview.OnE2eNodeItemClicked;

import java.util.ArrayList;
import java.util.List;

public class E2EActivity extends AppCompatActivity {

    E2EGplotView e2EGplotView;
    E2ETvGplotView e2ETvGplotView;

    Button btn_start;
    Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e2_e);
        initView();
        initView2();
    }

    private void initView() {
        e2EGplotView = findViewById(R.id.e2eview);

        // 设置数据
        List<E2ENode> leftNodes = new ArrayList<>();
        List<E2ENode> rightNodes = new ArrayList<>();
        List<E2ENode> bottomNodes = new ArrayList<>();

        //==========Bottom=================
        E2ENode e2ENode1 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端1");
        E2ENode e2ENode21 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端21");
        E2ENode e2ENode22 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端22", R.drawable.back5);
        E2ENode e2ENode31 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端31", R.drawable.back5);
        E2ENode e2ENode32 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端32", R.drawable.back5);
        E2ENode e2ENode33 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端33");
        E2ENode e2ENode34 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端34");

        E2ENode e2ENode35 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端35");
        E2ENode e2ENode36 = new E2ENode(E2ENode.TYPE_BOTTOM, "云端36");

        e2ENode21.addChild(e2ENode31);
        e2ENode21.addChild(e2ENode32);
        e2ENode21.addChild(e2ENode33);
        e2ENode21.addChild(e2ENode34);

        e2ENode22.addChild(e2ENode35);
        e2ENode22.addChild(e2ENode36);


        e2ENode1.addChild(e2ENode21);
        e2ENode1.addChild(e2ENode22);
        bottomNodes.add(e2ENode1);
//        bottomNodes.add(e2ENode36);
        //==========Bottom--end=================

        //==========Left=================
        E2ENode leftNode10 = new E2ENode(E2ENode.TYPE_LEFT, "云端10");
        E2ENode leftNode11 = new E2ENode(E2ENode.TYPE_LEFT, "云端11");
        E2ENode leftNode21 = new E2ENode(E2ENode.TYPE_LEFT, "云端21");
        E2ENode leftNode22 = new E2ENode(E2ENode.TYPE_LEFT, "云端21");
        E2ENode leftNode31 = new E2ENode(E2ENode.TYPE_LEFT, "云端31");
        E2ENode leftNode32 = new E2ENode(E2ENode.TYPE_LEFT, "云端32");
        E2ENode leftNode33 = new E2ENode(E2ENode.TYPE_LEFT, "云端33");
        E2ENode leftNode34 = new E2ENode(E2ENode.TYPE_LEFT, "云端34");

        leftNode21.addChild(leftNode31);
        leftNode22.addChild(leftNode32);
        leftNode10.addChild(leftNode21);
        leftNode10.addChild(leftNode22);
        leftNodes.add(leftNode10);
        leftNodes.add(leftNode11);
        //==========Left-end=================

        //==========Right=================
        E2ENode rightNode10 = new E2ENode(E2ENode.TYPE_RIGHT, "云端10");
        E2ENode rightNode11 = new E2ENode(E2ENode.TYPE_RIGHT, "云端11");
        E2ENode rightNode21 = new E2ENode(E2ENode.TYPE_RIGHT, "云端21");
        E2ENode rightNode22 = new E2ENode(E2ENode.TYPE_RIGHT, "云端22");
        E2ENode rightNode31 = new E2ENode(E2ENode.TYPE_RIGHT, "云端31");
        E2ENode rightNode32 = new E2ENode(E2ENode.TYPE_RIGHT, "云端32");
        E2ENode rightNode33 = new E2ENode(E2ENode.TYPE_RIGHT, "云端33");
        E2ENode rightNode34 = new E2ENode(E2ENode.TYPE_RIGHT, "云端34");

        rightNode21.addChild(rightNode31);
        rightNode22.addChild(rightNode32);
        rightNode10.addChild(rightNode21);
        rightNode10.addChild(rightNode22);
        rightNodes.add(rightNode10);
        rightNodes.add(rightNode11);
        //==========Right-end=================

        //==========Main======================
        E2ENode mainNode = new E2ENode(E2ENode.TYPE_MAIN, "云端拓扑图", R.drawable.back2);
        //==========Main-end======================

        e2EGplotView
                .setBottomNodes(bottomNodes)
                .setLeftNodes(leftNodes)
                .setRightNodes(rightNodes)
                .setMainNode(mainNode)
                .setHorizontalLineLength(60)
                .setVerticalLineLength(30)
                .show();

        e2EGplotView.setOnE2eNodeItemClicked(new OnE2eNodeItemClicked() {
            @Override
            public void onItemClicked(E2ENode e2ENode) {
                Toast.makeText(E2EActivity.this, "点击：" + e2ENode.getText() + ",坐标：（" + e2ENode.getX() + "," + e2ENode.getY() + "）", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "点击：" + e2ENode.getText() + ",坐标：（" + e2ENode.getX() + "," + e2ENode.getY() + "）,");
            }
        });
    }

    private void initView2() {

        List<TvNode> bottomNodes = new ArrayList<>();
        List<TvNode> leftNodes = new ArrayList<>();
        List<TvNode> rightNodes = new ArrayList<>();
        //=====================================bottom===================================
        TvNode bottomTvNode11 = TvNode.createNode(this, TvNode.TYPE_BOTTOM, "bottom11");
        TvNode bottomTvNode12 = TvNode.createNode(this, TvNode.TYPE_BOTTOM, "bottom12");
        TvNode bottomTvNode13 = TvNode.createNode(this, TvNode.TYPE_BOTTOM, "bottom13");
        TvNode bottomTvNode21 = TvNode.createNode(this, TvNode.TYPE_BOTTOM, "bottom21");
        TvNode bottomTvNode22 = TvNode.createNode(this, TvNode.TYPE_BOTTOM, "bottom22");
        TvNode bottomTvNode31 = TvNode.createNode(this, TvNode.TYPE_BOTTOM, "bottom31");
        TvNode bottomTvNode32 = TvNode.createNode(this, TvNode.TYPE_BOTTOM, "bottom32");
        bottomTvNode32.setStatus(TvNode.STATUS_CLOSE);
        bottomTvNode21.addChild(bottomTvNode31);
        bottomTvNode21.addChild(bottomTvNode32);
        bottomTvNode11.addChild(bottomTvNode21);
        bottomTvNode11.addChild(bottomTvNode22);
        bottomNodes.add(bottomTvNode11);
        //=====================================bottom-end===================================
        //=====================================left===================================
        TvNode leftTvNode11 = TvNode.createNode(this, TvNode.TYPE_LEFT, "left11");
        TvNode leftTvNode12 = TvNode.createNode(this, TvNode.TYPE_LEFT, "left12");
        leftTvNode12.setStatus(TvNode.STATUS_CLOSE);
        TvNode leftTvNode13 = TvNode.createNode(this, TvNode.TYPE_LEFT, "left13");
        TvNode leftTvNode21 = TvNode.createNode(this, TvNode.TYPE_LEFT, "left21");
        TvNode leftTvNode22 = TvNode.createNode(this, TvNode.TYPE_LEFT, "left22");
        TvNode leftTvNode31 = TvNode.createNode(this, TvNode.TYPE_LEFT, "left31");
        TvNode leftTvNode32 = TvNode.createNode(this, TvNode.TYPE_LEFT, "left32");
        leftTvNode11.addChild(leftTvNode21);
        leftTvNode11.addChild(leftTvNode22);
        leftTvNode22.addChild(leftTvNode31);
//        leftTvNode22.addChild(leftTvNode32);
        leftNodes.add(leftTvNode11);
        leftNodes.add(leftTvNode12);
//        leftNodes.add(leftTvNode13);
        //=====================================left-end===================================

        //=====================================right===================================
        TvNode rightTvNode11 = TvNode.createNode(this, TvNode.TYPE_RIGHT, "right11");
        TvNode rightTvNode12 = TvNode.createNode(this, TvNode.TYPE_RIGHT, "right12");
        TvNode rightTvNode13 = TvNode.createNode(this, TvNode.TYPE_RIGHT, "right13");
        TvNode rightTvNode21 = TvNode.createNode(this, TvNode.TYPE_RIGHT, "right21");
        TvNode rightTvNode22 = TvNode.createNode(this, TvNode.TYPE_RIGHT, "right22");
        TvNode rightTvNode31 = TvNode.createNode(this, TvNode.TYPE_RIGHT, "right22");
        TvNode rightTvNode32 = TvNode.createNode(this, TvNode.TYPE_RIGHT, "right22");
        rightTvNode21.tv().setTextColor(Color.RED);
        rightTvNode21.setStatus(TvNode.STATUS_CLOSE);
        rightTvNode21.addChild(rightTvNode31);
        rightTvNode11.addChild(rightTvNode21);
        rightTvNode11.addChild(rightTvNode22);
        rightNodes.add(rightTvNode11);
        rightNodes.add(rightTvNode12);
//        rightNodes.add(rightTvNode13);
        //=====================================right-end===================================
        //=====================================main===================================
        TvNode mainNode = TvNode.createNode(this, TvNode.TYPE_MAIN, "云端拓扑图");
//        mainNode.tv().setBackgroundResource(R.drawable.back2);
        mainNode.tv().setTextSize(20);
        //=====================================main-end===================================

        e2ETvGplotView = findViewById(R.id.e2eview2);

        e2ETvGplotView
                .setMainNode(mainNode)
                .setBottomNodes(bottomNodes)
                .setLeftNodes(leftNodes)
                .setRightNodes(rightNodes)
                .setHorizontalLineLength(50)
                .setVerticalLineLength(30)
                .show();

        e2ETvGplotView.setOnE2eNodeItemClicked(new OnTvNodeItemClickListener() {
            @Override
            public void onItemClicked(TvNode tvNode) {
                Toast.makeText(E2EActivity.this, "点击：" + tvNode.tv().getText(), Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2ETvGplotView.startAllAnim();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2ETvGplotView.cancelAllAnim();
            }
        });
    }
}
