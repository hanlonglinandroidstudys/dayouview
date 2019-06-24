package com.example.diyview.customview.networkspace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.example.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络空间自定义控件
 *
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
    private int ecdnHorizontalLineLength2 = 50;

    /**
     * OCDN 或 ICDN 区域内边距
     **/
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
    Paint platformLinePaint;
    private TextPaint textPaint;


    private OnItemClickListener onItemClickListener;

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

        Drawable iserver_drawable = getResources().getDrawable(R.drawable.iserver_icon);
        iserver_drawable.setBounds(0, 0, 50, 30);
        ocdnNode = new NetNode(mContext, NetNode.TYPE_OCDN, "OCDN");
        ocdnNode.tv().setCompoundDrawables(null, null, null, iserver_drawable);
        ocdnNode.tv().setBackgroundResource(R.drawable.shape_icdn_round_rect);

        icdnNode = new NetNode(mContext, NetNode.TYPE_OCDN, "ICDN");
        icdnNode.tv().setCompoundDrawables(null, null, null, iserver_drawable);
        icdnNode.tv().setBackgroundResource(R.drawable.shape_icdn_round_rect);

        ecdnNode = new NetNode(mContext, NetNode.TYPE_ECDN, "ECDN");
        ecdnNode.tv().setCompoundDrawables(null, null, null, iserver_drawable);
        ecdnNode.tv().setBackgroundResource(R.drawable.shape_icdn_round_rect);

        platformNode = new NetNode(mContext, NetNode.TYPE_ECDN, "云平台");

        ocdnLinePaint = new Paint();
        ocdnLinePaint.setStrokeWidth(2);
        ocdnLinePaint.setAntiAlias(true);
        ocdnLinePaint.setStyle(Paint.Style.STROKE);
        ocdnLinePaint.setStrokeCap(Paint.Cap.ROUND); //圆角效果
        ocdnLinePaint.setColor(Color.WHITE);
        ocdnBackPaint = new Paint();
        ocdnBackPaint.setAntiAlias(true);
        ocdnBackPaint.setStyle(Paint.Style.FILL);
        ocdnBackPaint.setColor(Color.parseColor("#95B83A"));

        icdnLinePaint = new Paint();
        icdnLinePaint.setStrokeWidth(2);
        icdnLinePaint.setAntiAlias(true);
        icdnLinePaint.setStyle(Paint.Style.STROKE);
        icdnLinePaint.setStrokeCap(Paint.Cap.ROUND); //圆角效果
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

        platformLinePaint = new Paint();
        platformLinePaint.setStrokeWidth(2);
        platformLinePaint.setAntiAlias(true);
        platformLinePaint.setStyle(Paint.Style.STROKE);
        platformLinePaint.setStrokeCap(Paint.Cap.ROUND); //圆角效果

        textPaint = new TextPaint();
        textPaint.setTextSize(18);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(1);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.W = getWidth();
        this.H = getHeight();
        this.ocdnHorizontalLineLength1 = W * 3 / 20;
        this.ocdnHorizontalLineLength2 = W / 20;
        this.icdnHorizontalLineLength1 = W * 3 / 20;
        this.icdnHorizontalLineLength2 = W / 20;
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
        // 测量节点
        Log.e(getClass().getSimpleName(), "drawOCDN() H=" + H);

        ocdnNode.tv().setX(containerPadding);
        ocdnNode.tv().setY(H / 4 - ocdnNode.tv().getHeight() / 2);
        if (ocdnList.size() == 1) {
            NetNode netNode = ocdnList.get(0);
            netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + ocdnHorizontalLineLength1 + ocdnHorizontalLineLength2);
            netNode.tv().setY(netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2 - netNode.tv().getHeight());
            netNode.clearLinePoints();
            netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + ocdnHorizontalLineLength1, netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
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
//                netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
            }
        }

        // 摆放和绘制节点
        addNodeToParent(ocdnNode, canvas);
        ocdnLinePaint.setStrokeWidth(5);
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
        //绘制箭头
        int arrowMargin = 10;
        int arrowLength = 10;
        int arrowStartX = (int) (ocdnNode.tv().getX() + ocdnNode.tv().getWidth());
        int arrowStartY = (int) (ocdnNode.tv().getY() + ocdnNode.tv().getHeight() / 2);
        int arrowEndX = (int) (arrowStartX + ocdnHorizontalLineLength1 - arrowMargin);
        int arrowEndY = arrowStartY;
        drawLineArrows(canvas, ocdnLinePaint, arrowStartX, arrowStartY, arrowEndX, arrowEndY, arrowLength, 1);
//        drawDoubleLine(arrowStartX,arrowStartY,arrowEndX,arrowEndY,ocdnLinePaint,canvas);
//        drawArrows(canvas,ocdnLinePaint,arrowStartX + ocdnHorizontalLineLength1,arrowEndY,3);

        // 绘制箭头上的文字
        String text = "精化管理";
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        drawLinesText(canvas, arrowStartX + (arrowEndX - arrowStartX) / 2, (int) (arrowStartY - (fontMetrics.bottom - fontMetrics.top) - arrowMargin), text, arrowEndX - arrowStartX);
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

        icdnNode.tv().setX(ocdnNode.tv().getX() + ocdnNode.tv().getWidth() / 2 - icdnNode.tv().getWidth() / 2);
        icdnNode.tv().setY(H * 6 / 8 - icdnNode.tv().getHeight() / 2);
        if (icdnList.size() == 1) {
            NetNode netNode = icdnList.get(0);
            netNode.tv().setX(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() + icdnHorizontalLineLength1 + icdnHorizontalLineLength2);
            netNode.tv().setY(netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2 - netNode.tv().getHeight());
            netNode.clearLinePoints();
            netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth() - icdnHorizontalLineLength1, netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
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
//                netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
            }
        }

        // 摆放和绘制
        addNodeToParent(icdnNode, canvas);
        icdnLinePaint.setStrokeWidth(5);
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

        //绘制箭头
        int arrowMargin = 10;
        int arrowLength = 10;
        int arrowStartX = (int) (icdnNode.tv().getX() + icdnNode.tv().getWidth() + arrowMargin);
        int arrowStartY = (int) (icdnNode.tv().getY() + icdnNode.tv().getHeight() / 2);
        int arrowEndX = (int) (arrowStartX + icdnHorizontalLineLength1 - arrowMargin);
        int arrowEndY = arrowStartY;
        drawLineArrows(canvas, icdnLinePaint, arrowStartX, arrowStartY, arrowEndX, arrowEndY, arrowLength, 0);
//        drawDoubleLine(arrowStartX,arrowStartY,arrowEndX,arrowEndY,icdnLinePaint,canvas);
//        drawArrows(canvas,icdnLinePaint,(int)(icdnNode.tv().getX() + icdnNode.tv().getWidth()),arrowEndY,2);

        // 绘制箭头上的文字
        String text = "输入收集";
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        drawLinesText(canvas, arrowStartX + (arrowEndX - arrowStartX) / 2, (int) (arrowStartY - (fontMetrics.bottom - fontMetrics.top) - arrowMargin), text, arrowEndX - arrowStartX);
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

        ecdnNode.tv().setX(W * 2 / 5 - ecdnNode.tv().getWidth() / 2);  // TODO: 2019/6/21 待确定
        ecdnNode.tv().setY(H * 4 / 8 - ecdnNode.tv().getHeight() / 2);
        if (ecdnList.size() == 1) {
            NetNode netNode = ecdnList.get(0);
            netNode.tv().setX(W * 3 / 5 + ecdnHorizontalLineLength2);
            netNode.tv().setY(netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2 - netNode.tv().getHeight());
            netNode.clearLinePoints();
            netNode.addLinePoint(new PointF(netNode.tv().getX(), netNode.tv().getY() + netNode.tv().getHeight() / 2));
            netNode.addLinePoint(new PointF(netNode.getParent().tv().getX() + netNode.getParent().tv().getWidth(), netNode.getParent().tv().getY() + netNode.getParent().tv().getHeight() / 2));
        } else {
            int startY = containerPadding;
            int divideY = (H - containerPadding * 2) / (ecdnList.size() - 1);

            for (int i = 0; i < ecdnList.size(); i++) {
                NetNode netNode = ecdnList.get(i);
                netNode.tv().setX(W * 3 / 5 + ecdnHorizontalLineLength2);
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
        platformNode.tv().setX(ocdnNode.tv().getX() + ocdnNode.tv().getWidth() / 2 - platformNode.tv().getWidth() / 2);
        platformNode.tv().setY(H / 2 - platformNode.tv().getHeight() / 2);
        // 摆放和绘制
        addNodeToParent(platformNode, canvas);
        // 画连接线
        int arrowMargin = 10;
        int arrowLength = 10;

        // 到OCDN的连接线
        platformLinePaint.setStrokeWidth(5);
        platformLinePaint.setColor(Color.parseColor("#57BFBC"));
        int startX = (int) (platformNode.tv().getX() + platformNode.tv().getWidth() / 2);
        int endX = (int) (ocdnNode.tv().getX() + ocdnNode.tv().getWidth() / 2);
        int startY = (int) (platformNode.tv().getY());
        int endY = (int) (ocdnNode.tv().getY() + ocdnNode.tv().getHeight() + arrowMargin);
        drawLineArrows(canvas, platformLinePaint, startX, startY, endX, endY, arrowLength, 1);
//        drawDoubleLine(startX,startY,endX,endY,platformLinePaint,canvas);
//        drawArrows(canvas,platformLinePaint,startX,(int) (ocdnNode.tv().getY() + ocdnNode.tv().getHeight()),0);
        // 画文字
        String text = "库结构内容播发";
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textPaint.setColor(Color.parseColor("#95B83A"));
        drawLinesText(canvas, (int) (arrowMargin + (ocdnNode.tv().getX() + ocdnNode.tv().getWidth() / 2 - arrowMargin * 2) / 2), H * 7 / 16 - (int) (fontMetrics.bottom - fontMetrics.top) / 2, text, (int) (ocdnNode.tv().getX() + ocdnNode.tv().getWidth() / 2 - arrowMargin));

        // 到ICDN的连接线
        platformLinePaint.setStrokeWidth(5);
        platformLinePaint.setColor(Color.parseColor("#57BFBC"));
        startX = (int) (platformNode.tv().getX() + platformNode.tv().getWidth() / 2);
        endX = (int) (icdnNode.tv().getX() + icdnNode.tv().getWidth() / 2);
        startY = (int) (platformNode.tv().getY() + platformNode.tv().getHeight() + arrowMargin);
        endY = (int) (icdnNode.tv().getY());
        drawLineArrows(canvas, platformLinePaint, startX, startY, endX, endY, arrowLength, 0);
//        drawDoubleLine(startX,startY,endX,endY,platformLinePaint,canvas);
//        drawArrows(canvas,platformLinePaint,startX,(int) (platformNode.tv().getY() + platformNode.tv().getHeight()),0);
        // 画文字
        text = "加标上传";
        fontMetrics = textPaint.getFontMetrics();
        textPaint.setColor(Color.parseColor("#056989"));
        drawLinesText(canvas, (int) (arrowMargin + (icdnNode.tv().getX() + icdnNode.tv().getWidth() / 2 - arrowMargin * 2) / 2), H * 9 / 16 - (int) (fontMetrics.bottom - fontMetrics.top) / 2, text, (int) (icdnNode.tv().getX() + icdnNode.tv().getWidth() / 2 - arrowMargin));

        // 到ECDN的连接线
        platformLinePaint.setStrokeWidth(5);
        platformLinePaint.setColor(Color.parseColor("#57BFBC"));
        startX = (int) (platformNode.tv().getX() + platformNode.tv().getWidth() + arrowMargin);
        endX = (int) (ecdnNode.tv().getX() - arrowMargin);
        startY = (int) (platformNode.tv().getY() + platformNode.tv().getHeight() / 2);
        endY = (int) (ecdnNode.tv().getY() + ecdnNode.tv().getHeight() / 2);
        drawLineArrows(canvas, platformLinePaint, startX, startY, endX, endY, arrowLength, 2);
//        drawDoubleLine(startX,startY,endX,endY,platformLinePaint,canvas);
//        drawArrows(canvas,platformLinePaint,(int)(platformNode.tv().getX() + platformNode.tv().getWidth()),startY,2);
//        drawArrows(canvas,platformLinePaint,(int)(ecdnNode.tv().getX()),startY,3);
        // 画文字
        text = "第二网络";
        fontMetrics = textPaint.getFontMetrics();
        textPaint.setColor(Color.parseColor("#57BFBC"));
        drawLinesText(canvas, startX + (endX - startX) / 2, startY - (int) (fontMetrics.bottom - fontMetrics.top) - arrowMargin, text, (int) (endX - startX));
    }

    /**
     * 画线和箭头,当前只支持横向和竖向的箭头
     *
     * @param canvas
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param type   0 箭头在起始点 1 箭头在终点 2 双箭头
     */
    private void drawLineArrows(Canvas canvas, Paint paint, int startX, int startY, int endX, int endY, int arrowsLength, int type) {
        // 画线
        canvas.drawLine(startX, startY, endX, endY, paint);
        // 画箭头
        int diffX = endX - startX;
        int diffY = endY - startY;
        if (diffX == 0) {
            //竖直方向
            switch (type) {
                case 0:
                    if (endY > startY) {
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY + arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY + arrowsLength, paint);
                    } else {
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY - arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY - arrowsLength, paint);
                    }
                    break;
                case 1:
                    if (endY > startY) {
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY - arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY - arrowsLength, paint);
                    } else {
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY + arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY + arrowsLength, paint);
                    }
                    break;
                case 2:
                    if (endY > startY) {
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY + arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY + arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY - arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY - arrowsLength, paint);
                    } else {
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY - arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY - arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY + arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY + arrowsLength, paint);
                    }
                    break;
            }
        } else if (diffY == 0) {
            // 水平方向
            switch (type) {
                case 0:
                    if (endX > startX) {
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY - arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY + arrowsLength, paint);
                    } else {
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY - arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY + arrowsLength, paint);
                    }
                    break;
                case 1:
                    if (endX > startX) {
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY - arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY + arrowsLength, paint);
                    } else {
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY - arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY + arrowsLength, paint);
                    }
                    break;
                case 2:
                    if (endX > startX) {
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY - arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX + arrowsLength, startY + arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY - arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX - arrowsLength, endY + arrowsLength, paint);
                    } else {
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY - arrowsLength, paint);
                        canvas.drawLine(startX, startY, startX - arrowsLength, startY + arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY - arrowsLength, paint);
                        canvas.drawLine(endX, endY, endX + arrowsLength, endY + arrowsLength, paint);
                    }
                    break;
            }
        } else {
            return;
        }
    }

    /**
     * 画自动换行的文字
     *
     * @param canvas
     * @param centerX 文字的中心坐标
     * @param startY
     * @param text
     * @param width   文字的长度 超过此长度自动换行
     */
    private void drawLinesText(Canvas canvas, int centerX, int startY, String text, int width) {
        canvas.save();
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        canvas.translate(centerX, startY);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    /**
     * 画双线，当前只支持横向和竖向
     *
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @param paint
     * @param canvas
     */
    private void drawDoubleLine(int startX, int startY, int endX, int endY, Paint paint, Canvas canvas) {
        int width = 6;
        int radiuX = 3;
        int radiuY = 3;
        int diffX = endX - startX;
        int diffY = endY - startY;
        if (diffX == 0) {
            // 竖直方向
            RectF rectF = new RectF(startX - width / 2, startY, startX + width / 2, endY);
            canvas.drawRoundRect(rectF, radiuX, radiuY, paint);
        } else if (diffY == 0) {
            // 水平方向
            RectF rectF = new RectF(startX, startY - width / 2, endX, startY + width / 2);
            canvas.drawRoundRect(rectF, radiuX, radiuY, paint);
        }
    }

    /**
     * 画箭头
     *
     * @param canvas
     * @param startX
     * @param startY
     * @param type 0 上  1 下 2 左 3 右
       */
    private void drawArrows(Canvas canvas,Paint paint,int startX,int startY,int type){
        int arrowLength=10;
        int width=6;
        Path path=new Path();
        switch (type){
            case 0:
                // 向上
                path.moveTo(startX,startY);
                path.lineTo(startX-arrowLength,startY+arrowLength);
                path.lineTo((int)(startX-arrowLength+width/1.4),(int)(startY+arrowLength+width/1.4));
                path.lineTo(startX,(int)(startY+width*1.4));
                path.lineTo((int)(startX+arrowLength-width/1.4),(int)(startY+arrowLength+width/1.4));
                path.lineTo(startX+arrowLength,startY+arrowLength);
                path.close();
                break;
            case 1:
                // 向下
                path.moveTo(startX,startY);
                path.lineTo(startX-arrowLength,startY-arrowLength);
                path.lineTo((int)(startX-arrowLength+width/1.4),(int)(startY-arrowLength-width/1.4));
                path.lineTo(startX,(int)(startY-width*1.4));
                path.lineTo((int)(startX+arrowLength-width/1.4),(int)(startY-arrowLength+width/1.4));
                path.lineTo(startX+arrowLength,startY-arrowLength);
                path.close();
            case 2:
                // 向左
                path.moveTo(startX,startY);
                path.lineTo(startX+arrowLength,startY-arrowLength);
                path.lineTo((int)(startX+arrowLength+width/1.4),(int)(startY-arrowLength+width/1.4));
                path.lineTo((int)(startX+width*1.4),startY);
                path.lineTo((int)(startX+arrowLength+width/1.4),(int)(startY+arrowLength-width/1.4));
                path.lineTo(startX+arrowLength,startY+arrowLength);
                path.close();
                break;
            case 3:
                //向右
                path.moveTo(startX,startY);
                path.lineTo(startX-arrowLength,startY-arrowLength);
                path.lineTo((int)(startX-arrowLength-width/1.4),(int)(startY-arrowLength+width/1.4));
                path.lineTo((int)(startX-width*1.4),startY);
                path.lineTo((int)(startX-arrowLength-width/1.4),(int)(startY+arrowLength-width/1.4));
                path.lineTo(startX-arrowLength,startY+arrowLength);
                path.close();
                break;
        }
        canvas.drawPath(path,paint);
    }

    private void addNodeToParent(NetNode netNode, Canvas canvas) {
        addView(netNode.tv());
    }

    // 点击事件

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            // 遍历ocdn
            if (ocdnNode.isClicked(x, y)) {
                performItemClick(ocdnNode);
            }
            for (int i = 0; i < ocdnList.size(); i++) {
                if (ocdnList.get(i).isClicked(x, y)) {
                    performItemClick(ocdnList.get(i));
                }
            }
            // 遍历icdn
            if (icdnNode.isClicked(x, y)) {
                performItemClick(icdnNode);
            }
            for (int i = 0; i < icdnList.size(); i++) {
                if (icdnList.get(i).isClicked(x, y)) {
                    performItemClick(icdnList.get(i));
                }
            }
            // 遍历ecdn
            if (ecdnNode.isClicked(x, y)) {
                performItemClick(ecdnNode);
            }
            for (int i = 0; i < ecdnList.size(); i++) {
                if (ecdnList.get(i).isClicked(x, y)) {
                    performItemClick(ecdnList.get(i));
                }
            }
            // 遍历云平台
            if (platformNode.isClicked(x, y)) {
                performItemClick(platformNode);
            }
        }
        return super.onTouchEvent(event);
    }

    private void performItemClick(NetNode netNode) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(netNode);
        }
    }

    // 接口
    public interface OnItemClickListener {
        void onClick(NetNode netNode);
    }

    //=====================================================

    public NetworkSpaceView setOcdnList(List<NetNode> ocdnList) {
        this.ocdnList = ocdnList;
        if (ocdnNode != null) {
            ocdnNode.setChilds(ocdnList);
        }
        return this;
    }

    public NetworkSpaceView setIcdnList(List<NetNode> icdnList) {
        this.icdnList = icdnList;
        if (icdnList != null) {
            icdnNode.setChilds(icdnList);
        }
        return this;
    }

    public NetworkSpaceView setEcdnList(List<NetNode> ecdnList) {
        this.ecdnList = ecdnList;
        if (ecdnNode != null) {
            ecdnNode.setChilds(ecdnList);
        }
        return this;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void show() {
        invalidate();
    }
}
