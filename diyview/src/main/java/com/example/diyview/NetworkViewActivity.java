package com.example.diyview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.diyview.customview.networkspace.NetNode;
import com.example.diyview.customview.networkspace.NetworkSpaceView;

import java.util.ArrayList;
import java.util.List;

public class NetworkViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_view);
        initView();
    }

    private void initView() {
        NetworkSpaceView networkSpaceView=findViewById(R.id.networkView);

        //=========OCDN========================================
        List<NetNode> ocdnList=new ArrayList<>();
        ocdnList.add(new NetNode(this,NetNode.TYPE_OCDN,"大有中诚网站"));
        ocdnList.add(new NetNode(this,NetNode.TYPE_OCDN,"大有中诚App"));
        ocdnList.add(new NetNode(this,NetNode.TYPE_OCDN,"大有中诚公众号"));
        //=================================================

        //=========ICDN========================================
        List<NetNode> icdnList=new ArrayList<>();
        icdnList.add(new NetNode(this,NetNode.TYPE_ICDN,"销售管理系统"));
        icdnList.add(new NetNode(this,NetNode.TYPE_ICDN,"仓库管理系统"));
        //=================================================

        //=========ECDN========================================
        List<NetNode> ecdnList=new ArrayList<>();
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"公司前台/门票"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"会议室"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"董事长办公室"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"财务室"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"人力资源行政部"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"项目管理部"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"运维部"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"应用研发部"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"产品部"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"总工办公室"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"副总经理办公室"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"演示教室"));
        ecdnList.add(new NetNode(this,NetNode.TYPE_ECDN,"库房"));
        //=================================================

        networkSpaceView.setOcdnList(ocdnList);
        networkSpaceView.setIcdnList(icdnList);
        networkSpaceView.setEcdnList(ecdnList);
        networkSpaceView.show();
    }
}
