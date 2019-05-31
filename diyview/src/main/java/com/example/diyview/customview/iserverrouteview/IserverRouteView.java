package com.example.diyview.customview.iserverrouteview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义云端路结构控件
 * <br>
 *
 * @author 韩龙林
 * @date 2019/5/28 11:12
 */
public class IserverRouteView extends View {

    /**
     * 条目字体大小,默认25
     */
    private int itemTextSize = 25;

    /**
     * 未选中,条目字体颜色,默认gray
     */
    private int itemTextColorUnChecked = Color.GRAY;

    /**
     * 选中时条目字体颜色，默认black
     */
    private int itemTextColorChecked = Color.BLACK;

    /**
     * 标题字体大小,默认35
     */
    private int titleTextSize = 35;

    /**
     * 标题字体颜色，默认black
     */
    private int titleTextColor = Color.BLACK;

    /**
     * 中间矩形边框粗细,默认2
     */
    private int rectBorderWidth = 2;

    /**
     * 中间矩形边框颜色，默认blue
     */
    private int rectBorderColor = Color.BLUE;

    /**
     * 条目连接线粗细,默认2
     */
    private int itemLineWidth = 2;

    /**
     * 未运行时条目连接线颜色，默认gray
     */
    private int itemLineColorNotRunning = Color.GRAY;
    /**
     * 正在运行时条目连接线颜色，默认green
     */
    private int itemLineColorRunning = Color.GREEN;
    /**
     * 异常时条目连接线的颜色，默认黄色
     */
    private int itemLineColorError = Color.YELLOW;


    /**
     * 点击条目后连接线伸长的距离，默认为20
     */
    private int itemLineRiseLength = 20;

    /**
     * 左边距，默认为10
     */
    private int paddingLeft = 10;
    /**
     * 右边距，默认为10
     */
    private int paddingRight = 10;


    /**
     * 总共的最大条目 maxItemSize = 第一行+第二行 = 第二行*2+1
     */
    private int maxItemSize = 9;

    /**
     * 第二行的条目最大数目 一共两行，第一行最大数目为maxItemSize+1,所以总共的最大数量为maxItemSize*2+1
     * <br>
     * 默认为4
     */
    private int secondMaxItemSize = 4;

    // 画笔
    Paint rectPaint;
    Paint itemLinePaint;
    Paint titleTextPaint;
    Paint itemTextPaint;

    /**
     * 条目数据集合
     */
    List<RouteItem> itemDataList = new ArrayList<>();

    /**
     * 标题
     */
    String title;

    /**
     * 监听器
     * <br>
     * 需要设置此监听器来响应点击每个条目的事件
     */
    OnRouteItemClickListener onRouteItemClickListener;

    // 位置信息
    /**
     * 矩形上边y坐标
     */
    int rectTop;
    /**
     * 矩形下边y坐标
     */
    int rectBottom;


    public IserverRouteView(Context context) {
        super(context);
        init();
    }

    public IserverRouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.STROKE);
        itemLinePaint = new Paint();
        itemLinePaint.setAntiAlias(true);
        itemLinePaint.setStyle(Paint.Style.STROKE);
        itemLinePaint.setStrokeCap(Paint.Cap.ROUND);
        itemTextPaint = new Paint();
        itemTextPaint.setAntiAlias(true);
        titleTextPaint = new Paint();
        titleTextPaint.setAntiAlias(true);
    }

    private void refreshStyle() {
        rectPaint.setStrokeWidth(rectBorderWidth);
        rectPaint.setColor(rectBorderColor);
        itemLinePaint.setStrokeWidth(itemLineWidth);
        itemTextPaint.setTextSize(itemTextSize);
        titleTextPaint.setColor(titleTextColor);
        titleTextPaint.setTextSize(titleTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refreshStyle();
        drawRect(canvas);
        drawItems(canvas);
    }

    /**
     * 画中间的矩形和里面的文字
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        int W = getWidth();
        int H = getHeight();

        // 画矩形
        rectTop = H / 5 * 2;
        rectBottom = H / 5 * 3;
        RectF rectF = new RectF((float) (0 + paddingLeft), (float) rectTop, (float) (W - paddingRight), (float) rectBottom);
        //canvas.drawRect(rect, rectPaint);
        canvas.drawRoundRect(rectF, 5, 5, rectPaint);

        // 画title 画在矩形的中间
        float titleWidth = titleTextPaint.measureText(title);
        int titleLeft = (int) (W / 2 - titleWidth / 2);
        Paint.FontMetrics fontMetrics = titleTextPaint.getFontMetrics();
        int titleBaselineY = H / 2 + (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        canvas.drawText(title, titleLeft, titleBaselineY, titleTextPaint);
    }

    /**
     * 画每个条目的文字和连接线
     *
     * @param canvas
     */
    private void drawItems(Canvas canvas) {
        // 测量每个条目
        int H = getHeight();
        int W = getWidth();
        int divideWith = getWidth() / (secondMaxItemSize + 1);
        for (int i = 0; i < itemDataList.size(); i++) {
            if (i < (secondMaxItemSize + 1)) {
                // 第一行
                Rect rect = new Rect();
                itemTextPaint.getTextBounds(itemDataList.get(i).getText(), 0, itemDataList.get(i).getText().length(), rect);
                itemDataList.get(i).setWidth(rect.width());
                itemDataList.get(i).setHeight(rect.height());
                itemDataList.get(i).setX(divideWith * (i + 1) - divideWith / 2);
                itemDataList.get(i).setY(H / 5);
                if (itemDataList.get(i).isChecked()) {
                    itemDataList.get(i).setY(itemDataList.get(i).getY() - itemLineRiseLength);
                }
            } else if (i >= (secondMaxItemSize + 1) && i < (secondMaxItemSize * 2 + 1)) {
                // 第二行
                Rect rect = new Rect();
                itemTextPaint.getTextBounds(itemDataList.get(i).getText(), 0, itemDataList.get(i).getText().length(), rect);
                itemDataList.get(i).setWidth(rect.width());
                itemDataList.get(i).setHeight(rect.height());
                itemDataList.get(i).setX(divideWith * (i - secondMaxItemSize));
                itemDataList.get(i).setY(H * 4 / 5 + itemDataList.get(i).getHeight());
                if (itemDataList.get(i).isChecked()) {
                    itemDataList.get(i).setY(itemDataList.get(i).getY() + itemLineRiseLength);
                }
            } else {
                // 超过两行 不测量了
                itemDataList.get(i).setX(0);
                itemDataList.get(i).setY(0);
                Rect rect = new Rect();
                itemTextPaint.getTextBounds(itemDataList.get(i).getText(), 0, itemDataList.get(i).getText().length(), rect);
                itemDataList.get(i).setWidth(rect.width());
                itemDataList.get(i).setHeight(rect.height());
            }
        }

        // 摆放和绘制每个条目
        for (int i = 0; i < itemDataList.size(); i++) {
            RouteItem item = itemDataList.get(i);
            //Log.e("TAG", "绘制：" + item.getX() + "," + item.getY());
            if (item.getX() != 0 && item.getY() != 0) {
                if (item.getY() < H / 2) {
                    if (item.getState() == RouteItem.STATE_RUNNING) {
                        itemLinePaint.setColor(itemLineColorRunning);
                    } else if (item.getState() == RouteItem.STATE_NOT_RUNNING) {
                        itemLinePaint.setColor(itemLineColorNotRunning);
                    } else if (item.getState() == RouteItem.STATE_ERROR) {
                        itemLinePaint.setColor(itemLineColorError);
                    }
                    if (item.isChecked()) {
                        itemTextPaint.setColor(itemTextColorChecked);
                        itemLinePaint.setStrokeWidth(itemLineWidth * 2);
                    } else {
                        itemTextPaint.setColor(itemTextColorUnChecked);
                        itemLinePaint.setStrokeWidth(itemLineWidth);
                    }
                    // 写字
                    canvas.drawText(item.getText(), item.getX() - item.getWidth() / 2, item.getY(), itemTextPaint);
                    // 画上连接线
                    canvas.drawLine(item.getX(), item.getY() + item.getHeight() / 2, item.getX(), rectTop, itemLinePaint);
                } else {
                    if (item.getState() == RouteItem.STATE_RUNNING) {
                        itemLinePaint.setColor(itemLineColorRunning);
                    } else if (item.getState() == RouteItem.STATE_NOT_RUNNING) {
                        itemLinePaint.setColor(itemLineColorNotRunning);
                    } else if (item.getState() == RouteItem.STATE_ERROR) {
                        itemLinePaint.setColor(itemLineColorError);
                    }
                    if (item.isChecked()) {
                        itemTextPaint.setColor(itemTextColorChecked);
                        itemLinePaint.setStrokeWidth(itemLineWidth * 2);
                    } else {
                        itemTextPaint.setColor(itemTextColorUnChecked);
                        itemLinePaint.setStrokeWidth(itemLineWidth);
                    }
                    // 写字
                    canvas.drawText(item.getText(), item.getX() - item.getWidth() / 2, item.getY(), itemTextPaint);
                    // 画下连接线
                    canvas.drawLine(item.getX(), item.getY() - item.getHeight() * 3 / 2, item.getX(), rectBottom, itemLinePaint);
                }

            }
        }

    }

    private void unCheckAll() {
        for (RouteItem item : itemDataList) {
            item.setChecked(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            for (RouteItem item : itemDataList) {
                if (item.isClicked(x, y) && !item.isChecked()) {
                    // 还原所有状态
                    unCheckAll();
                    // 设置点击item状态
                    item.setChecked(true);
                    // 重新显示 刷新界面
                    show();
                    if (onRouteItemClickListener != null) {
                        // 响应监听事件
                        onRouteItemClickListener.onItemClick(item);
                        break;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    // =============外部配置参数api==============

    /**
     * 设置标题字体大小
     *
     * @param titleTextSize
     * @return
     */
    public IserverRouteView setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    /**
     * 设置标题字体颜色
     *
     * @param titleTextColor
     * @return
     */
    public IserverRouteView setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    /**
     * 设置矩形边框宽度
     *
     * @param rectBorderWidth
     * @return
     */
    public IserverRouteView setRectBorderWidth(int rectBorderWidth) {
        this.rectBorderWidth = rectBorderWidth;
        return this;
    }

    /**
     * 设置矩形边框颜色
     *
     * @param rectBorderColor
     * @return
     */
    public IserverRouteView setRectBorderColor(int rectBorderColor) {
        this.rectBorderColor = rectBorderColor;
        return this;
    }

    /**
     * 设置条目字体大小
     *
     * @param itemTextSize
     * @return
     */
    public IserverRouteView setItemTextSize(int itemTextSize) {
        this.itemTextSize = itemTextSize;
        return this;
    }

    /**
     * 设置未选中时的条目字体颜色
     *
     * @param itemTextColorUnChecked
     * @return
     */
    public IserverRouteView setItemTextColorUnChecked(int itemTextColorUnChecked) {
        this.itemTextColorUnChecked = itemTextColorUnChecked;
        return this;
    }

    /**
     * 设置选中时的条目字体颜色
     *
     * @param itemTextColorChecked
     * @return
     */
    public IserverRouteView setItemTextColorChecked(int itemTextColorChecked) {
        this.itemTextColorChecked = itemTextColorChecked;
        return this;
    }

    /**
     * 设置条目连接线的宽度
     *
     * @param itemLineWidth
     * @return
     */
    public IserverRouteView setItemLineWidth(int itemLineWidth) {
        this.itemLineWidth = itemLineWidth;
        return this;
    }

    /**
     * 设置未运行时的条目连接线的颜色
     *
     * @param itemLineColorNotRunning
     * @return
     */
    public IserverRouteView setItemLineColorNotRunning(int itemLineColorNotRunning) {
        this.itemLineColorNotRunning = itemLineColorNotRunning;
        return this;
    }

    /**
     * 设置运行时的条目连接线的颜色
     *
     * @param itemLineColorRunning
     * @return
     */
    public IserverRouteView setItemLineColorRunning(int itemLineColorRunning) {
        this.itemLineColorRunning = itemLineColorRunning;
        return this;
    }

    /**
     * 设置异常时条目连接线的颜色
     *
     * @param itemLineColorError
     * @return
     */
    public IserverRouteView setItemLineColorError(int itemLineColorError) {
        this.itemLineColorError = itemLineColorError;
        return this;
    }

    /**
     * 设置标题的文字
     *
     * @param title
     * @return
     */
    public IserverRouteView setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置条目数据列表
     *
     * @param itemDataList
     * @return
     */
    public IserverRouteView setItemDataList(List<RouteItem> itemDataList) {
        this.itemDataList = itemDataList;
        return this;
    }

    /**
     * 设置显示条目的最大数量
     *
     * @param maxItemSize
     * @return
     */
    public IserverRouteView setMaxItemSize(int maxItemSize) {
        this.maxItemSize = maxItemSize;
        this.secondMaxItemSize = maxItemSize / 2;
        return this;
    }

    /**
     * 设置选中条目时连接线伸长的距离
     *
     * @param itemLineRiseLength
     * @return
     */
    public IserverRouteView setItemLineRiseLength(int itemLineRiseLength) {
        this.itemLineRiseLength = itemLineRiseLength;
        return this;
    }

    /**
     * 显示刷新
     * <br>
     * 所有的属性设置完成之后需要调用此方法，不然无法刷新界面
     */
    public void show() {
        invalidate();
    }

    /**
     * 设置点击条目的监听器
     *
     * @param onRouteItemClickListener
     */
    public void setOnRouteItemClickListener(OnRouteItemClickListener onRouteItemClickListener) {
        this.onRouteItemClickListener = onRouteItemClickListener;
    }

    // =============外部配置参数api end==============

}
