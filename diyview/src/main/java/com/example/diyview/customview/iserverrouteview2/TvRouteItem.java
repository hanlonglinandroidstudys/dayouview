package com.example.diyview.customview.iserverrouteview2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.diyview.R;


/**
 * @author 韩龙林
 * @date 2019/6/10 13:51
 */
public class TvRouteItem {
    /**
     * 正在运行状态
     */
    public static final int STATE_RUNNING = 1;
    /**
     * 未运行状态
     */
    public static final int STATE_NOT_RUNNING = 0;
    /**
     * 异常状态
     */
    public static final int STATE_ERROR = -1;

    public static int defaultTextSize = 15;
    public static int defaultPaddingLeft = 5;
    public static int defaultPaddingRight = 5;
    public static int defaultPaddingTop = 3;
    public static int defaultPaddingBottom = 3;

    TextView tv;
    Point startLinePoint;
    Point endLinePoint;
    int lineColor = Color.GRAY;
    int lineWidth = 2;
    boolean checked = false;
    int state = STATE_RUNNING;

    public static TvRouteItem createItem(Context context, String text) {
        return createItem(context, STATE_RUNNING, text);
    }

    public static TvRouteItem createItem(Context context, int state, String text) {
        TvRouteItem tvRouteItem = new TvRouteItem();
        tvRouteItem.setState(state);
        tvRouteItem.tv = new TextView(context);
        tvRouteItem.tv.setTextSize(defaultTextSize);
        tvRouteItem.tv.setTextColor(Color.DKGRAY);
        tvRouteItem.tv.setText(text);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvRouteItem.tv.setGravity(Gravity.CENTER);
        tvRouteItem.tv.setBackgroundResource(R.drawable.shape_round_rect);
        tvRouteItem.tv.setLayoutParams(params);
        tvRouteItem.tv.setPadding(defaultPaddingLeft, defaultPaddingTop, defaultPaddingRight, defaultPaddingBottom);
        return tvRouteItem;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setStartLinePoint(Point startLinePoint) {
        this.startLinePoint = startLinePoint;
    }

    public Point getStartLinePoint() {
        return startLinePoint;
    }

    public void setEndLinePoint(Point endLinePoint) {
        this.endLinePoint = endLinePoint;
    }

    public Point getEndLinePoint() {
        return endLinePoint;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public TextView tv() {
        return tv;
    }

    /**
     * 统一设置textSize
     * @param defaultTextSize
     */
    public static void setDefaultTextSize(int defaultTextSize) {
        TvRouteItem.defaultTextSize = defaultTextSize;
    }

    public static void setDefaultPadding(int defaultPaddingLeft, int defaultPaddingTop, int defaultPaddingRight, int defaultPaddingBottom) {
        TvRouteItem.defaultPaddingLeft=defaultPaddingLeft;
        TvRouteItem.defaultPaddingTop=defaultPaddingTop;
        TvRouteItem.defaultPaddingRight=defaultPaddingRight;
        TvRouteItem.defaultPaddingBottom = defaultPaddingBottom;
    }

    /**
     * 判断是否被点击
     *
     * @param x 点击x坐标
     * @param y 点击y坐标
     * @return true 被点击 false 未点击
     */
    public boolean isClicked(int x, int y) {
        if ((x < (tv.getX() + tv.getWidth()))
                && (x > tv.getX())
                && (y > tv.getY())
                && (y < (tv.getY() + tv.getHeight()))) {
            return true;
        }
        return false;
    }

}
