package com.example.diyview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.diyview.customview.e2egplotview.E2EGplotView;
import com.example.diyview.customview.e2egplotview.E2ENode;
import com.example.diyview.customview.e2egplotview.OnE2eNodeItemClicked;

import java.util.ArrayList;
import java.util.List;

public class E2EActivity extends AppCompatActivity {

    E2EGplotView e2EGplotView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e2_e);
        initView();
    }

    private void initView() {
        e2EGplotView=findViewById(R.id.e2eview);

        // 设置数据
        List<E2ENode> leftNodes=new ArrayList<>();
        List<E2ENode> rightNodes=new ArrayList<>();
        List<E2ENode> bottomNodes=new ArrayList<>();

        //==========Bottom=================
        E2ENode e2ENode1=new E2ENode(E2ENode.TYPE_BOTTOM,"云端1");
        E2ENode e2ENode21=new E2ENode(E2ENode.TYPE_BOTTOM,"云端21");
        E2ENode e2ENode22=new E2ENode(E2ENode.TYPE_BOTTOM,"云端22",R.drawable.back5);
        E2ENode e2ENode31=new E2ENode(E2ENode.TYPE_BOTTOM,"云端31",R.drawable.back5);
        E2ENode e2ENode32=new E2ENode(E2ENode.TYPE_BOTTOM,"云端32",R.drawable.back5);
        E2ENode e2ENode33=new E2ENode(E2ENode.TYPE_BOTTOM,"云端33");
        E2ENode e2ENode34=new E2ENode(E2ENode.TYPE_BOTTOM,"云端34");

        E2ENode e2ENode35=new E2ENode(E2ENode.TYPE_BOTTOM,"云端35");
        E2ENode e2ENode36=new E2ENode(E2ENode.TYPE_BOTTOM,"云端36");

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
        E2ENode leftNode10=new E2ENode(E2ENode.TYPE_LEFT,"云端10");
        E2ENode leftNode11=new E2ENode(E2ENode.TYPE_LEFT,"云端11");
        E2ENode leftNode21=new E2ENode(E2ENode.TYPE_LEFT,"云端21");
        E2ENode leftNode22=new E2ENode(E2ENode.TYPE_LEFT,"云端21");
        E2ENode leftNode31=new E2ENode(E2ENode.TYPE_LEFT,"云端31");
        E2ENode leftNode32=new E2ENode(E2ENode.TYPE_LEFT,"云端32");
        E2ENode leftNode33=new E2ENode(E2ENode.TYPE_LEFT,"云端33");
        E2ENode leftNode34=new E2ENode(E2ENode.TYPE_LEFT,"云端34");

        leftNode21.addChild(leftNode31);
        leftNode22.addChild(leftNode32);
        leftNode10.addChild(leftNode21);
        leftNode10.addChild(leftNode22);
        leftNodes.add(leftNode10);
        leftNodes.add(leftNode11);
        //==========Left-end=================

        //==========Right=================
        E2ENode rightNode10=new E2ENode(E2ENode.TYPE_RIGHT,"云端10");
        E2ENode rightNode11=new E2ENode(E2ENode.TYPE_RIGHT,"云端11");
        E2ENode rightNode21=new E2ENode(E2ENode.TYPE_RIGHT,"云端21");
        E2ENode rightNode22=new E2ENode(E2ENode.TYPE_RIGHT,"云端22");
        E2ENode rightNode31=new E2ENode(E2ENode.TYPE_RIGHT,"云端31");
        E2ENode rightNode32=new E2ENode(E2ENode.TYPE_RIGHT,"云端32");
        E2ENode rightNode33=new E2ENode(E2ENode.TYPE_RIGHT,"云端33");
        E2ENode rightNode34=new E2ENode(E2ENode.TYPE_RIGHT,"云端34");

        rightNode21.addChild(rightNode31);
        rightNode22.addChild(rightNode32);
        rightNode10.addChild(rightNode21);
        rightNode10.addChild(rightNode22);
        rightNodes.add(rightNode10);
        rightNodes.add(rightNode11);
        //==========Right-end=================

        //==========Main======================
        E2ENode mainNode=new E2ENode(E2ENode.TYPE_BOTTOM,"云端拓扑图",R.drawable.back2);
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
                Toast.makeText(E2EActivity.this, "点击："+e2ENode.getText()+",坐标：（"+e2ENode.getX()+","+e2ENode.getY()+"）", Toast.LENGTH_SHORT).show();
                Log.e("TAG","点击："+e2ENode.getText()+",坐标：（"+e2ENode.getX()+","+e2ENode.getY()+"）,");
            }
        });
    }
}
