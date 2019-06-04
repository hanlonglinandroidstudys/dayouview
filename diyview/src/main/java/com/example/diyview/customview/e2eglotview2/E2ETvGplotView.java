package com.example.diyview.customview.e2eglotview2;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * 自定义云端拓扑图
 *
 * @author 韩龙林
 * @date 2019/6/3 11:55
 */
public class E2ETvGplotView extends RelativeLayout {

    // 数据项
    /**
     * 左节点集合
     */
    List<TvNode> leftNodes;

    /**
     * 右节点结合
     */
    List<TvNode> rightNodes;

    /**
     * 底部节点结合
     */
    List<TvNode> bottomNodes;

    /**
     * 主节点
     */
    TvNode mainNode;

    // 长度信息
    /**
     * 控件宽
     */
    int W = 0;

    /**
     * 控件高
     */
    int H = 0;

    /**
     * 横向连接线长度
     */
    private int horizontalLineLength = 60;

    /**
     * 纵向连接线长度
     */
    private int verticalLineLength = 50;

    /**
     * 画笔 用来画连接线
     */
    private Paint mPaint;

    /**
     * 点击节点监听器
     */
    OnTvNodeItemClickListener onE2eNodeItemClicked;

    public E2ETvGplotView(Context context) {
        super(context);
        init();
    }

    public E2ETvGplotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        W = getWidth();
        H = getHeight();
        removeAllViews();
        addMainNode(canvas);
        addLeftNodes(canvas);
        addRightNodes(canvas);
        addBottomNodes(canvas);
    }

    private void startAnim(TvNode tvNode) {
        if (tvNode.isStartAnim) {
            return;
        }
        if (tvNode.getStatus() == TvNode.STATUS_CLOSE) {
            return;
        }
        tvNode.objectAnimator = ObjectAnimator.ofInt(tvNode, "lineColor", Color.WHITE, Color.RED);
        tvNode.objectAnimator.setDuration(1000);
        tvNode.objectAnimator.setEvaluator(new ArgbEvaluator());
        tvNode.objectAnimator.setInterpolator(new LinearInterpolator());
        tvNode.objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        tvNode.objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        tvNode.objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                E2ETvGplotView.this.invalidate();
                Log.e("TAG", "正在刷新");
            }
        });
        tvNode.objectAnimator.start();
        tvNode.isStartAnim = true;
    }

    private void cancelAnim(TvNode tvNode) {
        if (!tvNode.isStartAnim) {
            return;
        }
        if (tvNode.objectAnimator == null) {
            return;
        }
        tvNode.objectAnimator.removeAllListeners();
        tvNode.objectAnimator.removeAllUpdateListeners();
        tvNode.objectAnimator.cancel();
        tvNode.isStartAnim = false;
    }

    /**
     * 递归方法
     * 开启所有子节点的动画
     *
     * @param parentNode
     */
    private void startChildAnim(TvNode parentNode) {
        if (parentNode.getChilds() == null || parentNode.getChilds().size() == 0) {
            return;
        }
        for (int i = 0; i < parentNode.getChilds().size(); i++) {
            TvNode tvNode = parentNode.getChilds().get(i);
            startAnim(tvNode);
            startChildAnim(tvNode);
        }
    }

    /**
     * 递归方法
     * 关闭所有子节点的动画
     *
     * @param parentNode
     */
    private void cancelChildAnim(TvNode parentNode) {
        if (parentNode.getChilds() == null || parentNode.getChilds().size() == 0) {
            return;
        }
        for (int i = 0; i < parentNode.getChilds().size(); i++) {
            TvNode tvNode = parentNode.getChilds().get(i);
            cancelAnim(tvNode);
            cancelChildAnim(tvNode);
        }
    }

    public void show() {
        invalidate();
    }

    private void addMainNode(Canvas canvas) {
        /*
         * mainNode在 W/2,H/3 的位置
         */
        if (mainNode != null) {
            mainNode.tv.setX(W / 2 - mainNode.tv.getWidth() / 2);
            mainNode.tv.setY(H / 3 - mainNode.tv.getHeight() / 2);
            addNodeToParent(mainNode, canvas);
        }
    }

    private void addLeftNodes(Canvas canvas) {
        if (leftNodes == null || leftNodes.size() == 0)
            return;
        for (int i = 0; i < leftNodes.size(); i++) {
            leftNodes.get(i).setParent(mainNode);
        }
        if (leftNodes.size() == 1) {
            TvNode tvNode = leftNodes.get(0);
            tvNode.tv.setX(mainNode.tv.getX() - horizontalLineLength - tvNode.tv.getWidth());
            tvNode.tv.setY(mainNode.tv.getY() + mainNode.tv.getHeight() / 2 - tvNode.tv.getHeight() / 2);
            addNodeToParent(tvNode, canvas);
            //递归调用
            addLeftChilds(tvNode, canvas, H * 2 / 3);
        } else {
            int divideLength = H * 2 / 3 / (leftNodes.size() + 1);
            float startY = mainNode.tv.getY() + mainNode.tv.getHeight() / 2 - H * 2 / 3 / 2;
            for (int i = 0; i < leftNodes.size(); i++) {
                TvNode tvNode = leftNodes.get(i);
                tvNode.tv.setY(startY + divideLength * (i + 1) - tvNode.tv.getHeight() / 2);
                tvNode.tv.setX(mainNode.tv.getX() - horizontalLineLength - horizontalLineLength - tvNode.tv.getWidth());
                addNodeToParent(tvNode, canvas);
                //递归调用
                addLeftChilds(tvNode, canvas, H * 2 / 3 / leftNodes.size());
            }
        }
    }

    private void addRightNodes(Canvas canvas) {
        if (rightNodes == null || rightNodes.size() == 0)
            return;
        for (int i = 0; i < rightNodes.size(); i++) {
            rightNodes.get(i).setParent(mainNode);
        }
        if (rightNodes.size() == 1) {
            TvNode tvNode = rightNodes.get(0);
            tvNode.tv.setX(mainNode.tv.getX() + mainNode.tv.getWidth() + horizontalLineLength);
            tvNode.tv.setY(mainNode.tv.getY() + mainNode.tv.getHeight() / 2 - tvNode.tv.getHeight() / 2);
            addNodeToParent(tvNode, canvas);
            //递归调用
            addRightChilds(tvNode, canvas, H * 2 / 3);
        } else {
            int divideLength = H * 2 / 3 / (rightNodes.size() + 1);
            float startY = mainNode.tv.getY() + mainNode.tv.getHeight() / 2 - H * 2 / 3 / 2;
            for (int i = 0; i < rightNodes.size(); i++) {
                TvNode tvNode = rightNodes.get(i);
                tvNode.tv.setY(startY + divideLength * (i + 1) - tvNode.tv.getHeight() / 2);
                tvNode.tv.setX(mainNode.tv.getX() + mainNode.tv.getWidth() + horizontalLineLength + horizontalLineLength);
                addNodeToParent(tvNode, canvas);
                //递归调用
                addRightChilds(tvNode, canvas, H * 2 / 3 / rightNodes.size());
            }
        }

    }

    private void addBottomNodes(Canvas canvas) {
        if (bottomNodes == null || bottomNodes.size() == 0)
            return;
        for (int i = 0; i < bottomNodes.size(); i++) {
            bottomNodes.get(i).setParent(mainNode);
        }
        if (bottomNodes.size() == 1) {
            TvNode tvNode = bottomNodes.get(0);
            tvNode.tv.setX(mainNode.tv.getX() + mainNode.tv.getWidth() / 2 - tvNode.tv.getWidth() / 2);
            tvNode.tv.setY(mainNode.tv.getY() + mainNode.tv.getHeight() + verticalLineLength);
            addNodeToParent(tvNode, canvas);
            //递归调用
            addBottomChilds(tvNode, canvas, W);
        } else {
            int divideLength = W / (bottomNodes.size() + 1);
            float startX = mainNode.tv.getX() + mainNode.tv.getWidth() / 2 - W / 2;
            for (int i = 0; i < bottomNodes.size(); i++) {
                TvNode tvNode = bottomNodes.get(i);
                tvNode.tv.setX(startX + divideLength * (i + 1) - tvNode.tv.getWidth() / 2);
                tvNode.tv.setY(mainNode.tv.getY() + mainNode.tv.getHeight() + verticalLineLength + verticalLineLength);
                addNodeToParent(tvNode, canvas);
                //递归调用
                addBottomChilds(tvNode, canvas, W);
            }
        }
    }

    private void addBottomChilds(TvNode parentNode, Canvas canvas, int maxW) {
        if (parentNode.getChilds() == null || parentNode.getChilds().size() == 0)
            return;
        if (parentNode.getChilds().size() == 1) {
            TvNode tvNode = parentNode.getChilds().get(0);
            tvNode.tv.setX(parentNode.tv.getX() + parentNode.tv.getWidth() / 2 - tvNode.tv.getWidth() / 2);
            tvNode.tv.setY(parentNode.tv.getY() + parentNode.tv.getHeight() + verticalLineLength);
            addNodeToParent(tvNode, canvas);

            //递归调用
            addBottomChilds(tvNode, canvas, maxW / parentNode.getChilds().size());
        } else {
            int divideLength = maxW / (parentNode.getChilds().size() + 1);
            float startX = parentNode.tv.getX() + parentNode.tv.getWidth() / 2 - maxW / 2;
            for (int i = 0; i < parentNode.getChilds().size(); i++) {
                TvNode tvNode = parentNode.getChilds().get(i);
                tvNode.tv.setX(startX + divideLength * (i + 1) - tvNode.tv.getWidth() / 2);
                tvNode.tv.setY(parentNode.tv.getY() + parentNode.tv.getHeight() + verticalLineLength + verticalLineLength);
                addNodeToParent(tvNode, canvas);

                //递归调用
                addBottomChilds(tvNode, canvas, maxW / parentNode.getChilds().size());
            }
        }
    }

    private void addLeftChilds(TvNode parentNode, Canvas canvas, int maxH) {
        if (parentNode.getChilds() == null || parentNode.getChilds().size() == 0)
            return;
        if (parentNode.getChilds().size() == 1) {
            TvNode tvNode = parentNode.getChilds().get(0);
            tvNode.tv.setX(parentNode.tv.getX() - horizontalLineLength - tvNode.tv.getWidth());
            tvNode.tv.setY(parentNode.tv.getY() + parentNode.tv.getHeight() / 2 - tvNode.tv.getHeight() / 2);
            addNodeToParent(tvNode, canvas);
            //递归调用
            addLeftChilds(tvNode, canvas, maxH);
        } else {
            int divideLength = maxH / (parentNode.getChilds().size() + 1);
            float startY = parentNode.tv.getY() + parentNode.tv.getHeight() / 2 - maxH / 2;
            for (int i = 0; i < parentNode.getChilds().size(); i++) {
                TvNode tvNode = parentNode.getChilds().get(i);
                tvNode.tv.setY(startY + divideLength * (i + 1) - tvNode.tv.getHeight() / 2);
                tvNode.tv.setX(parentNode.tv.getX() - horizontalLineLength - horizontalLineLength - tvNode.tv.getWidth());
                addNodeToParent(tvNode, canvas);
                //递归调用
                addLeftChilds(tvNode, canvas, maxH / parentNode.getChilds().size());
            }
        }
    }

    private void addRightChilds(TvNode parentNode, Canvas canvas, int maxH) {
        if (parentNode.getChilds() == null || parentNode.getChilds().size() == 0)
            return;
        if (parentNode.getChilds().size() == 1) {
            TvNode tvNode = parentNode.getChilds().get(0);
            tvNode.tv.setX(parentNode.tv.getX() + parentNode.tv.getWidth() + horizontalLineLength);
            tvNode.tv.setY(parentNode.tv.getY() + parentNode.tv.getHeight() / 2 - tvNode.tv.getHeight() / 2);
            addNodeToParent(tvNode, canvas);
            //递归调用
            addRightChilds(tvNode, canvas, maxH);
        } else {
            int divideLength = maxH / (parentNode.getChilds().size() + 1);
            float startY = parentNode.tv.getY() + parentNode.tv.getHeight() / 2 - maxH / 2;
            for (int i = 0; i < parentNode.getChilds().size(); i++) {
                TvNode tvNode = parentNode.getChilds().get(i);
                tvNode.tv.setY(startY + divideLength * (i + 1) - tvNode.tv.getHeight() / 2);
                tvNode.tv.setX(parentNode.tv.getX() + parentNode.tv.getWidth() + horizontalLineLength + horizontalLineLength);
                addNodeToParent(tvNode, canvas);
                //递归调用
                addRightChilds(tvNode, canvas, maxH / parentNode.getChilds().size());
            }
        }
    }

    private void addNodeToParent(TvNode tvNode, Canvas canvas) {
        addView(tvNode.tv);
        // 画连接线
        if (tvNode.getParent() == null) {
            return;
        }
        tvNode.clearLinePoint();
        // 1.统计线上的点
        switch (tvNode.getType()) {
            case TvNode.TYPE_BOTTOM: {
                float nodeX = tvNode.tv.getX() + tvNode.tv.getWidth() / 2;
                float nodeY = tvNode.tv.getY();
                float parentX = tvNode.getParent().tv.getX() + tvNode.getParent().tv.getWidth() / 2;
                float parentY = tvNode.getParent().tv.getY() + tvNode.getParent().tv.getHeight();

                if (tvNode.getParent().getChilds().size() == 1) {
                    tvNode.addLinePoint(new PointF(nodeX, nodeY));
                    tvNode.addLinePoint(new PointF(parentX, parentY));
                } else {
                    tvNode.addLinePoint(new PointF(nodeX, nodeY));
                    tvNode.addLinePoint(new PointF(nodeX, nodeY - verticalLineLength));
                    tvNode.addLinePoint(new PointF(parentX, nodeY - verticalLineLength));
                    tvNode.addLinePoint(new PointF(parentX, parentY));
                }
                break;
            }
            case TvNode.TYPE_LEFT: {
                float nodeX = tvNode.tv.getX() + tvNode.tv.getWidth();
                float nodeY = tvNode.tv.getY() + tvNode.tv.getHeight() / 2;
                float parentX = tvNode.getParent().tv.getX();
                float parentY = tvNode.getParent().tv.getY() + tvNode.getParent().tv.getHeight() / 2;

                if (tvNode.getParent().getChilds().size() == 1) {
                    tvNode.addLinePoint(new PointF(nodeX, nodeY));
                    tvNode.addLinePoint(new PointF(parentX, parentY));
                } else {
                    tvNode.addLinePoint(new PointF(nodeX, nodeY));
                    tvNode.addLinePoint(new PointF(nodeX + horizontalLineLength, nodeY));
                    tvNode.addLinePoint(new PointF(nodeX + horizontalLineLength, parentY));
                    tvNode.addLinePoint(new PointF(parentX, parentY));
                }
                break;
            }
            case TvNode.TYPE_RIGHT: {
                float nodeX = tvNode.tv.getX();
                float nodeY = tvNode.tv.getY() + tvNode.tv.getHeight() / 2;
                float parentX = tvNode.getParent().tv.getX() + tvNode.getParent().tv.getWidth();
                float parentY = tvNode.getParent().tv.getY() + tvNode.getParent().tv.getHeight() / 2;

                if (tvNode.getParent().getChilds().size() == 1) {
                    tvNode.addLinePoint(new PointF(nodeX, nodeY));
                    tvNode.addLinePoint(new PointF(parentX, parentY));
                } else {
                    tvNode.addLinePoint(new PointF(nodeX, nodeY));
                    tvNode.addLinePoint(new PointF(nodeX - horizontalLineLength, nodeY));
                    tvNode.addLinePoint(new PointF(nodeX - horizontalLineLength, parentY));
                    tvNode.addLinePoint(new PointF(parentX, parentY));
                }
                break;
            }
            default:
                break;
        }
        // 2.画出线
        if (tvNode.linePoints.size() > 1) {
            for (int i = 1; i < tvNode.linePoints.size(); i++) {
                PointF lastPointF = tvNode.linePoints.get(i - 1);
                PointF nowPointF = tvNode.linePoints.get(i);
                mPaint.setColor(tvNode.getLineColor());
                canvas.drawLine(lastPointF.x, lastPointF.y, nowPointF.x, nowPointF.y, mPaint);
//                if (tvNode.getStatus() == TvNode.STATUS_OPEN) {
//                    // 画连接线上的圆点 通过不停刷新它来显示动画
//                    if (tvNode.circlePoint != null) {
//                        canvas.drawCircle(tvNode.circlePoint.x, tvNode.circlePoint.y, 5, mPaint);
//                    }
//                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            TvNode nodeClicked = findNodeClicked(x, y);
            if (nodeClicked != null) {
                if (onE2eNodeItemClicked != null) {
                    onE2eNodeItemClicked.onItemClicked(nodeClicked);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private TvNode findNodeClicked(int x, int y) {
        // 查找mainNode
        if (mainNode != null) {
            if (mainNode.isClicked(x, y))
                return mainNode;
        }
        // 查找bottomNodes
        if (bottomNodes != null) {
            for (int i = 0; i < bottomNodes.size(); i++) {
                TvNode tvNode = bottomNodes.get(i);
                if (tvNode.isClicked(x, y)) {
                    return tvNode;
                } else {
                    TvNode childClicked = tvNode.findChildClicked(x, y);
                    if (childClicked != null)
                        return childClicked;
                }
            }
        }
        // 查找leftNodes
        if (leftNodes != null) {
            for (int i = 0; i < leftNodes.size(); i++) {
                TvNode tvNode = leftNodes.get(i);
                if (tvNode.isClicked(x, y)) {
                    return tvNode;
                } else {
                    TvNode childClicked = tvNode.findChildClicked(x, y);
                    if (childClicked != null)
                        return childClicked;
                }
            }
        }

        // 查找rightNodes
        if (rightNodes != null) {
            for (int i = 0; i < rightNodes.size(); i++) {
                TvNode tvNode = rightNodes.get(i);
                if (tvNode.isClicked(x, y)) {
                    return tvNode;
                } else {
                    TvNode childClicked = tvNode.findChildClicked(x, y);
                    if (childClicked != null)
                        return childClicked;
                }
            }
        }
        return null;
    }

    //========================对外api===================================

    public E2ETvGplotView setMainNode(TvNode mainNode) {
        this.mainNode = mainNode;
        return this;
    }

    public E2ETvGplotView setBottomNodes(List<TvNode> bottomNodes) {
        this.bottomNodes = bottomNodes;
        return this;
    }

    public E2ETvGplotView setLeftNodes(List<TvNode> leftNodes) {
        this.leftNodes = leftNodes;
        return this;
    }

    public E2ETvGplotView setRightNodes(List<TvNode> rightNodes) {
        this.rightNodes = rightNodes;
        return this;
    }

    public E2ETvGplotView setVerticalLineLength(int verticalLineLength) {
        this.verticalLineLength = verticalLineLength;
        return this;
    }

    public E2ETvGplotView setHorizontalLineLength(int horizontalLineLength) {
        this.horizontalLineLength = horizontalLineLength;
        return this;
    }

    public void setOnE2eNodeItemClicked(OnTvNodeItemClickListener onE2eNodeItemClicked) {
        this.onE2eNodeItemClicked = onE2eNodeItemClicked;
    }

    /**
     * 开启动画 需要手动开启，默认不开启
     */
    public void startAllAnim() {
        // bottom
        if (bottomNodes != null) {
            for (int i = 0; i < bottomNodes.size(); i++) {
                startAnim(bottomNodes.get(i));
                startChildAnim(bottomNodes.get(i));
            }
        }
        //left
        if (leftNodes != null) {
            for (int i = 0; i < leftNodes.size(); i++) {
                startAnim(leftNodes.get(i));
                startChildAnim(leftNodes.get(i));
            }
        }
        //right
        if (rightNodes != null) {
            for (int i = 0; i < rightNodes.size(); i++) {
                startAnim(rightNodes.get(i));
                startChildAnim(rightNodes.get(i));
            }
        }
    }

    /**
     * 关闭动画 因为动画是无限循环的，开启动画后一定要记得关闭动画
     */
    public void cancelAllAnim() {
        // bottom
        if (bottomNodes != null) {
            for (int i = 0; i < bottomNodes.size(); i++) {
                cancelAnim(bottomNodes.get(i));
                cancelChildAnim(bottomNodes.get(i));
            }
        }
        //left
        if (leftNodes != null) {
            for (int i = 0; i < leftNodes.size(); i++) {
                cancelAnim(leftNodes.get(i));
                cancelChildAnim(leftNodes.get(i));
            }
        }
        //right
        if (rightNodes != null) {
            for (int i = 0; i < rightNodes.size(); i++) {
                cancelAnim(rightNodes.get(i));
                cancelChildAnim(rightNodes.get(i));
            }
        }
    }

    //========================对外api-end===================================
}
