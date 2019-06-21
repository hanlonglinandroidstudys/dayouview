package com.example.diyview.customview.networkspace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 韩龙林
 * @date 2019/6/21 15:00
 */
public class NetworkSpaceView extends RelativeLayout {
    private Context mContext;
    private int W = 0;
    private int H = 0;
    private int ocdnHorizontalLineLength1 = 100;
    private int ocdnHorizontalLineLength2 = 50;
    private int icdnHorizontalLineLength1 = 100;
    private int icdnHorizontalLineLength2 = 50;
    private int ecdnHorizontalLineLength1 = 100;
    private int ecdnHorizontalLineLength2 = 50;


    private int containerPadding = 40;

    List<NetNode> ocdnList = new ArrayList<>();
    List<NetNode> icdnList = new ArrayList<>();
    List<NetNode> ecdnList = new ArrayList<>();

    private NetNode ocdnNode;
    private NetNode icdnNode;
    private NetNode ecdnNode;
    private NetNode platformNode;

    Paint ocdnLinePaint;
    Paint ocdnBackPaint;
    Paint icdnLinePaint;
    Paint icdnBackPaint;
    Paint ecdnLinePaint;


    public NetworkSpaceView(Context context) {
        super(context);
        init(context);
    }

    public NetworkSpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        ocdnNode = new NetNode(mContext, NetNode.TYPE_OCDN, "OCDN");
        icdnNode = new NetNode(mContext, NetNode.TYPE_OCDN, "ICDN");
        ecdnNode = new NetNode(mContext, NetNode.TYPE_ECDN, "ECDN");
        platformNode = new NetNode(mContext, NetNode.TYPE_ECDN, "云平台");

        ocdnLinePaint = new Paint();
        ocdnLinePaint.setStrokeWidth(5);
        ocdnLinePaint.setAntiAlias(true);
        ocdnLinePaint.setStyle(Paint.Style.STROKE);
        ocdnLinePaint.setColor(Color.WHITE);
        ocdnBackPaint = new Paint();
        ocdnBackPaint.setAntiAlias(true);
        ocdnBackPaint.setStyle(Paint.Style.FILL);
        ocdnBackPaint.setColor(Color.parseColor("#95B83A"));

        icdnLinePaint = new Paint();
        icdnLinePaint.setStrokeWidth(5);
        icdnLinePaint.setAntiAlias(true);
        icdnLinePaint.setStyle(Paint.Style.STROKE);
        icdnLinePaint.setColor(Color.WHITE);
        icdnBackPaint = new Paint();
        icdnBackPaint.setAntiAlias(true);
        icdnBackPaint.setStyle(Paint.Style.FILL);
        icdnBackPaint.setColor(Color.parseColor("#056989"));

        ecdnLinePaint = new Paint();
        ecdnLinePaint.setStrokeWidth(5);
        ecdnLinePaint.setAntiAlias(true);
        ecdnLinePaint.setStyle(Paint.Style.STROKE);
        ecdnLinePaint.setColor(Color.parseColor("#57BFBC"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.W = getWidth();
        this.H = getHeight();
        this.ocdnHorizontalLineLength1 = W / 10;
        this.ocdnHorizontalLineLength2 = W / 20;
        this.icdnHorizontalLineLength1 = W / 10;
        this.icdnHorizontalLineLength2 = W / 20;
        this.ecdnHorizontalLineLength1 = W / 10;
        this.ecdnHorizontalLineLength2 = W / 20;
        removeAllViews();
        drawOCDN(canvas);
        drawICDN(canvas);
        drawECDN(canvas);
        drawPlatform(canvas);
    }

    /**
     * 画OCDN区域
     * y坐标范围： 1/8H --> 3/8H
     * x坐标范围： 0    --> 3/5W - marginRight
     *
     * @param canvas
     */
    private void drawOCDN(Canvas canvas) {
        // 画背景
        RectF ocdnRect = new RectF(containerPadding / 2, H / 8, W * 3 / 5 - containerPadding / 2, H * 3 / 8);
        canvas.drawRoundRect(ocdnRect, 20, 20, ocdnBackPaint);
        if (ocdnList.size() == 0)
            return;
        // 测量
        Log.e(getClass().getSimpleName(), "drawOCDN() H=" + H);

        ocdnNode.tv().setX(containerPadding);
        ocdnNode.tv().setY(H / 4 - ocdnNode.tv().getHeight() / 2);
        if (ocdnList.size() == 1) {
            NetNode netNode = ocdnList.get(0);
            netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + ocdnHorizontalLineLength1 + ocdnHorizontalLineLength2);
            netNode.tv().setY(netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2 - netNode.tv().getHeight());
            netNode.clearLinePoints();
            netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
        } else {
            int startY = H / 8 + containerPadding;
            int divideY = (H / 4 - containerPadding * 2) / (ocdnList.size() - 1);

            for (int i = 0; i < ocdnList.size(); i++) {
                NetNode netNode = ocdnList.get(i);
                netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + ocdnHorizontalLineLength1 + ocdnHorizontalLineLength2);
                netNode.tv().setY(startY + divideY * i - netNode.tv().getHeight() / 2);
                netNode.clearLinePoints();
                netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.tv().getX() - ocdnHorizontalLineLength2, netNode.tv().getY() + netNode.tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.tv().getX() - ocdnHorizontalLineLength2, netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
            }
        }

        // 摆放和绘制
        addNodeToParent(ocdnNode, canvas);
        for (int i = 0; i < ocdnList.size(); i++) {
            NetNode netNode = ocdnList.get(i);
            addNodeToParent(netNode, canvas);
            // 画连接线
            for (int j = 1; j < netNode.linePoints.size(); j++) {
                PointF lastPointF = netNode.linePoints.get(j - 1);
                PointF nowPointF = netNode.linePoints.get(j);
                canvas.drawLine(lastPointF.x, lastPointF.y, nowPointF.x, nowPointF.y, ocdnLinePaint);
            }
        }

    }

    /**
     * 画ICDN区域
     * y坐标范围： 5/8H --> 7/8H
     * x坐标范围： 0    --> 3/5W - marginRight
     *
     * @param canvas
     */
    private void drawICDN(Canvas canvas) {
        // 画背景
        RectF icdnRect = new RectF(containerPadding / 2, H * 5 / 8, W * 3 / 5 - containerPadding / 2, H * 7 / 8);
        canvas.drawRoundRect(icdnRect, 20, 20, icdnBackPaint);
        if (icdnList.size() == 0)
            return;
        // 测量
        Log.e(getClass().getSimpleName(), "drawicdn() H=" + H);

        icdnNode.tv().setX(containerPadding);
        icdnNode.tv().setY(H * 6 / 8 - icdnNode.tv().getHeight() / 2);
        if (icdnList.size() == 1) {
            NetNode netNode = icdnList.get(0);
            netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + icdnHorizontalLineLength1 + icdnHorizontalLineLength2);
            netNode.tv().setY(netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2 - netNode.tv().getHeight());
            netNode.clearLinePoints();
            netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
        } else {
            int startY = H * 5 / 8 + containerPadding;
            int divideY = (H / 4 - containerPadding * 2) / (icdnList.size() - 1);

            for (int i = 0; i < icdnList.size(); i++) {
                NetNode netNode = icdnList.get(i);
                netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + icdnHorizontalLineLength1 + icdnHorizontalLineLength2);
                netNode.tv().setY(startY + divideY * i - netNode.tv().getHeight() / 2);
                netNode.clearLinePoints();
                netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.tv().getX() - icdnHorizontalLineLength2, netNode.tv().getY() + netNode.tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.tv().getX() - icdnHorizontalLineLength2, netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
            }
        }

        // 摆放和绘制
        addNodeToParent(icdnNode, canvas);
        for (int i = 0; i < icdnList.size(); i++) {
            NetNode netNode = icdnList.get(i);
            addNodeToParent(netNode, canvas);

            // 画连接线
            for (int j = 1; j < netNode.linePoints.size(); j++) {
                PointF lastPointF = netNode.linePoints.get(j - 1);
                PointF nowPointF = netNode.linePoints.get(j);
                canvas.drawLine(lastPointF.x, lastPointF.y, nowPointF.x, nowPointF.y, icdnLinePaint);
            }
        }
    }

    /**
     * 画ECDN区域
     * 节点范围：
     * y坐标范围： 0    --  H
     * x坐标范围： 3/5W --> W
     *
     * @param canvas
     */
    private void drawECDN(Canvas canvas) {
        if (ecdnList.size() == 0)
            return;
        // 测量
        Log.e(getClass().getSimpleName(), "drawecdn() H=" + H);

        ecdnNode.tv().setX(W * 3 / 5 - ecdnHorizontalLineLength1 - ecdnNode.tv().getWidth());  // TODO: 2019/6/21 待确定
        ecdnNode.tv().setY(H * 4 / 8 - ecdnNode.tv().getHeight() / 2);
        if (ecdnList.size() == 1) {
            NetNode netNode = ecdnList.get(0);
            netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + ecdnHorizontalLineLength1 + ecdnHorizontalLineLength2);
            netNode.tv().setY(netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2 - netNode.tv().getHeight());
            netNode.clearLinePoints();
            netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
        } else {
            int startY = containerPadding;
            int divideY = (H - containerPadding * 2) / (ecdnList.size() - 1);

            for (int i = 0; i < ecdnList.size(); i++) {
                NetNode netNode = ecdnList.get(i);
                netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + ecdnHorizontalLineLength1 + ecdnHorizontalLineLength2);
                netNode.tv().setY(startY + divideY * i - netNode.tv().getHeight() / 2);
                netNode.clearLinePoints();
                netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.tv().getX() - ecdnHorizontalLineLength2, netNode.tv().getY() + netNode.tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.tv().getX() - ecdnHorizontalLineLength2, netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
                netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
            }
        }

        // 摆放和绘制
        addNodeToParent(ecdnNode, canvas);
        for (int i = 0; i < ecdnList.size(); i++) {
            NetNode netNode = ecdnList.get(i);
            addNodeToParent(netNode, canvas);

            // 画连接线
            for (int j = 1; j < netNode.linePoints.size(); j++) {
                PointF lastPointF = netNode.linePoints.get(j - 1);
                PointF nowPointF = netNode.linePoints.get(j);
                canvas.drawLine(lastPointF.x, lastPointF.y, nowPointF.x, nowPointF.y, ecdnLinePaint);
            }
        }
    }

    /**
     * 画云平台区域
     * x: 0  -- >
     * y: H/2
     *
     * @param canvas
     */
    private void drawPlatform(Canvas canvas) {
        // 测量
        platformNode.tv().setX(containerPadding);
        platformNode.tv().setY(H / 2 - platformNode.tv().getHeight() / 2);
        // 摆放和绘制
        addNodeToParent(platformNode, canvas);
        // 画连接线
        // 到OCDN的连接线
        int startX = (int) (platformNode.tv().getX() + platformNode.tv().getWidth() / 2);
        int endX = (int) (ocdnNode.tv().getX() + ocdnNode.tv().getWidth() / 2);
        int startY = (int) (platformNode.tv().getY());
        int endY = (int) (ocdnNode.tv().getY() + ocdnNode.tv().getHeight());
        ocdnLinePaint.setColor(Color.GREEN);
        canvas.drawLine(startX, startY, endX, endY, ocdnLinePaint);
        // 到ICDN的连接线
        startX = (int) (platformNode.tv().getX() + platformNode.tv().getWidth() / 2);
        endX = (int) (icdnNode.tv().getX() + icdnNode.tv().getWidth() / 2);
        startY = (int) (platformNode.tv().getY()+platformNode.tv().getHeight());
        endY = (int) (icdnNode.tv().getY());
        icdnLinePaint.setColor(Color.GREEN);
        canvas.drawLine(startX, startY, endX, endY, icdnLinePaint);
        // 到ECDN的连接线
        startX = (int) (platformNode.tv().getX() + platformNode.tv().getWidth());
        endX = (int) (ecdnNode.tv().getX());
        startY = (int) (platformNode.tv().getY()+platformNode.tv().getHeight()/2);
        endY = (int) (ecdnNode.tv().getY()+ecdnNode.tv().getHeight()/2);
        canvas.drawLine(startX, startY, endX, endY, ecdnLinePaint);
    }


    private void addNodeToParent(NetNode netNode, Canvas canvas) {
        addView(netNode.tv());
        // 画线
//        netNode.clearLinePoints();
//        if(netNode.getParent()==null ||netNode.getParent().getChilds().size()==0)
//            return;
//        if(netNode.getParent().getChilds().size()==1){
//            netNode.addLinePoint(new PointF(netNode.tv().getX(),netNode.tv().getY()+netNode.tv().getHeight()/2));
//            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX()+netNode.getParent().tv().getWidth(),netNode.getParent().tv().getY()+netNode.getParent().tv().getHeight()/2));
//        }else{
//            netNode.addLinePoint(new PointF(netNode.tv().getX(),netNode.tv().getY()+netNode.tv().getHeight()/2));
//            netNode.addLinePoint(new PointF(netNode.tv().getX()-ocdnHorizontalLineLength2,netNode.tv().getY()+netNode.tv().getHeight()/2));
//            netNode.addLinePoint(new PointF(netNode.tv().getX()-ocdnHorizontalLineLength2,netNode.getParent().tv().getY()+netNode.getParent().tv().getHeight()/2));
//            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX()+netNode.getParent().tv().getWidth(),netNode.getParent().tv().getY()+netNode.getParent().tv().getHeight()/2));
//        }
//        // 画连接线
//        for (int j = 1; j < netNode.linePoints.size(); j++) {
//            PointF lastPointF = netNode.linePoints.get(j - 1);
//            PointF nowPointF = netNode.linePoints.get(j);
//            canvas.drawLine(lastPointF.x, lastPointF.y, nowPointF.x, nowPointF.y, new Paint());
//        }
    }

//    /**
//     * 统一长度
//     *
//     * @param list
//     */
//    private void adjustWidth(List<NetNode> list) {
//        int maxWidth = 0;
//        // 找出最大长度
//        for (int i = 0; i < list.size(); i++) {
//            NetNode netNode = list.get(i);
//            int width = netNode.tv().getWidth();
//            if (maxWidth < width) {
//                maxWidth = width;
//            }
//        }
//        // 应用最大长度
//        for (int i = 0; i < list.size(); i++) {
//            list.get(i).tv().setWidth(maxWidth);
//        }
//    }

    //=====================================================

    public void setOcdnList(List<NetNode> ocdnList) {
        this.ocdnList = ocdnList;
        if (ocdnNode != null) {
            ocdnNode.setChilds(ocdnList);
//            adjustWidth(ocdnList);
        }
    }

    public void setIcdnList(List<NetNode> icdnList) {
        this.icdnList = icdnList;
        if (icdnList != null) {
            icdnNode.setChilds(icdnList);
        }
    }

    public void setEcdnList(List<NetNode> ecdnList) {
        this.ecdnList = ecdnList;
        if (ecdnNode != null) {
            ecdnNode.setChilds(ecdnList);
        }
    }

    public void show() {
        invalidate();
    }
}
