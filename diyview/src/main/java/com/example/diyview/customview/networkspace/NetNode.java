package com.example.diyview.customview.networkspace;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 韩龙林
 * @date 2019/6/21 15:11
 */
public class NetNode {

    public final static int TYPE_OCDN=1;
    public final static int TYPE_ECDN=2;
    public final static int TYPE_ICDN=3;

    private TextView tv;
    private int type=-1;

    private NetNode parent=null;
    private List<NetNode> childs=new ArrayList<>();
    /**
     * 连接线的点 连接到父节点的线
     */
    List<PointF> linePoints = new ArrayList<>();

    public NetNode(Context context,int type, String text){
        this.tv=new TextView(context);
        this.tv.setTextColor(Color.DKGRAY);
        this.tv.setTextSize(10);
        this.tv.setPadding(20, 8, 20, 8);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.tv.setGravity(Gravity.CENTER);
        this.tv.setBackgroundResource(R.drawable.shape_round_rect);
        this.tv.setLayoutParams(params);
        this.tv.setText(text);
        this.type=type;
    }

    public TextView tv() {
        return tv;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setParent(NetNode parent) {
        this.parent = parent;
    }

    public NetNode getParent() {
        return parent;
    }

    public void setChilds(List<NetNode> childs) {
        this.childs = childs;
        for (int i = 0; i <childs.size() ; i++) {
            childs.get(i).setParent(this);
        }
    }

    public List<NetNode> getChilds() {
        return childs;
    }

    public void addLinePoint(PointF p){
        linePoints.add(p);
    }
    public void clearLinePoints(){
        linePoints.clear();
    }
}
