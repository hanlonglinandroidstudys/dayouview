package com.example.diyview.customview.iserverrouteview2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * @author 韩龙林
 * @date 2019/6/10 13:46
 */
public class IserverTvRouteView extends RelativeLayout {

    /**
     * 矩形上边y坐标
     */
    int rectTopY;
    /**
     * 矩形下边y坐标
     */
    int rectBottomY;

    /**
     * 左边距，默认为10
     */
    private int paddingLeft = 10;
    /**
     * 右边距，默认为10
     */
    private int paddingRight = 10;

    /**
     * 点击增长的距离
     */
    private int itemLineRiseLength = 20;

    Paint mPaint;
    Paint rectPaint;

    /**
     * 总共的最大条目 maxItemSize = 第一行+第二行 = 第二行*2+1
     */
    private int maxItemSize = 9;

    /**
     * 条目数据集合
     */
    List<TvRouteItem> tvRouteItems = new ArrayList<>();

    /**
     * 标题, 大部分情况需要设置透明背景
     */
    TvRouteItem titleRouteItem;

    /**
     * 监听器
     */
    OnTvRouteItemClickListener onTvRouteItemClickListener;

    public IserverTvRouteView(Context context) {
        super(context);
        init();
    }

    public IserverTvRouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.GRAY);
        rectPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 每次调用必须先清除所有view
        removeAllViews();
        drawRect(canvas);
        measureRouteItems();
        addRouteItemViews(canvas);
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
        rectTopY = H / 5 * 2;
        rectBottomY = H / 5 * 3;
        RectF rectF = new RectF((float) (0 + paddingLeft), (float) rectTopY, (float) (W - paddingRight), (float) rectBottomY);
        //canvas.drawRect(rect, rectPaint);
        canvas.drawRoundRect(rectF, 5, 5, rectPaint);

        // 画title 画在矩形的中间
        if (titleRouteItem != null) {
            titleRouteItem.tv().setX(W / 2 - titleRouteItem.tv().getWidth() / 2);
            titleRouteItem.tv().setY(H / 2 - titleRouteItem.tv().getHeight() / 2);
            addView(titleRouteItem.tv());
        }
    }

    /**
     * 添加各项子view
     *
     * @param canvas
     */
    private void addRouteItemViews(Canvas canvas) {
        // 绘制每个条目
        for (int i = 0; i < tvRouteItems.size(); i++) {
            TvRouteItem tvRouteItem = tvRouteItems.get(i);
            if (tvRouteItem != null
                    && tvRouteItem.tv() != null
                    && tvRouteItem.getEndLinePoint() != null
                    && tvRouteItem.getStartLinePoint() != null) {
                // 添加veiw
                addView(tvRouteItem.tv());
                // 画连接线
                mPaint.setColor(tvRouteItem.getLineColor());
                if (tvRouteItem.isChecked()) {
                    mPaint.setStrokeWidth(tvRouteItem.getLineWidth() * 2);
                } else {
                    mPaint.setStrokeWidth(tvRouteItem.getLineWidth());
                }
                canvas.drawLine(tvRouteItem.getStartLinePoint().x, tvRouteItem.getStartLinePoint().y, tvRouteItem.endLinePoint.x, tvRouteItem.endLinePoint.y, mPaint);
            }
        }
    }

    /**
     * 测量每个条目的x,y 坐标，以及连接线的坐标
     */
    private void measureRouteItems() {
        // 测量每个条目
        int H = getHeight();
        int W = getWidth();
        int secondMaxItemSize = maxItemSize / 2;
        int divideWith = getWidth() / (secondMaxItemSize + 1);
        for (int i = 0; i < tvRouteItems.size(); i++) {
            TvRouteItem tvRouteItem = tvRouteItems.get(i);
            if (tvRouteItem == null) continue;
            if (i < (secondMaxItemSize + 1)) {
                // 第一行
                if (tvRouteItem.isChecked()) {
                    tvRouteItem.tv().setY(H / 5 - tvRouteItem.tv().getHeight() - itemLineRiseLength);
                } else {
                    tvRouteItem.tv().setY(H / 5 - tvRouteItem.tv().getHeight());
                }
                tvRouteItem.tv().setX(divideWith * (i + 1) - divideWith / 2 - tvRouteItem.tv().getWidth() / 2);
                tvRouteItem.setStartLinePoint(new Point((int) (tvRouteItem.tv().getX() + tvRouteItem.tv().getWidth() / 2), (int) (tvRouteItem.tv().getY() + tvRouteItem.tv().getHeight())));
                tvRouteItem.setEndLinePoint(new Point((int) (tvRouteItem.tv().getX() + tvRouteItem.tv().getWidth() / 2), rectTopY));
            } else if (i >= (secondMaxItemSize + 1) && i < (secondMaxItemSize * 2 + 1)) {
                // 第二行
                if (tvRouteItem.isChecked()) {
                    tvRouteItem.tv().setY(H * 4 / 5 + itemLineRiseLength);
                } else {
                    tvRouteItem.tv().setY(H * 4 / 5);
                }
                tvRouteItem.tv().setX(divideWith * (i - secondMaxItemSize) - tvRouteItem.tv().getWidth() / 2);
                tvRouteItem.setStartLinePoint(new Point((int) (tvRouteItem.tv().getX() + tvRouteItem.tv().getWidth() / 2), (int) (tvRouteItem.tv().getY())));
                tvRouteItem.setEndLinePoint(new Point((int) (tvRouteItem.tv().getX() + tvRouteItem.tv().getWidth() / 2), rectBottomY));
            } else {
                // 超过两行 不测量了 一般不会走这里

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            for (TvRouteItem item : tvRouteItems) {
                if (item.isClicked(x, y) && !item.isChecked()) {
                    // 还原所有状态
                    unCheckAll();
                    // 设置点击item状态
                    item.setChecked(true);
                    // 重新显示 刷新界面
                    show();
                    if (onTvRouteItemClickListener != null) {
                        // 响应监听事件
                        onTvRouteItemClickListener.onItemClick(item);
                        break;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void unCheckAll() {
        for (TvRouteItem item : tvRouteItems) {
            item.setChecked(false);
        }
    }

    // ======================================= 对外开放api


    public IserverTvRouteView setTitleRouteItem(TvRouteItem titleRouteItem) {
        this.titleRouteItem = titleRouteItem;
        return this;
    }

    public IserverTvRouteView setTvRouteItems(List<TvRouteItem> tvRouteItems) {
        this.tvRouteItems = tvRouteItems;
        return this;
    }

    public IserverTvRouteView setMaxItemSize(int maxItemSize) {
        this.maxItemSize = maxItemSize;
        return this;
    }

    public IserverTvRouteView setRectLineColor(int color) {
        rectPaint.setColor(color);
        return this;
    }

    public IserverTvRouteView setRectLineWidth(int width) {
        rectPaint.setStrokeWidth(width);
        return this;
    }

    public IserverTvRouteView setItemLineRiseLength(int itemLineRiseLength) {
        this.itemLineRiseLength = itemLineRiseLength;
        return this;
    }

    public void show() {
        invalidate();
    }

    public void setOnTvRouteItemClickListener(OnTvRouteItemClickListener onTvRouteItemClickListener) {
        this.onTvRouteItemClickListener = onTvRouteItemClickListener;
    }
}
