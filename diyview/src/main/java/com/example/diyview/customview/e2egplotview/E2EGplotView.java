package com.example.diyview.customview.e2egplotview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * E2E拓扑图自定义控件
 *
 * @author 韩龙林
 * @date 2019/5/29 18:31
 */
public class E2EGplotView extends View {

    /**
     * 左侧节点
     */
    List<E2ENode> leftNodes = new ArrayList<>();
    /**
     * 右侧节点
     */
    List<E2ENode> rightNodes = new ArrayList<>();
    /**
     * 下方节点
     */
    List<E2ENode> bottomNodes = new ArrayList<>();

    /**
     * 主节点
     */
    E2ENode mainNode;

    Paint mPaint;

    /**
     * 横向线长度
     */
    private int horizontalLineLength = 50;
    /**
     * 纵向线长度
     */
    private int verticalLineLength = 20;

    /**
     * 监听器
     */
    OnE2eNodeItemClicked onE2eNodeItemClicked;


    public E2EGplotView(Context context) {
        super(context);
        init();
    }

    public E2EGplotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMainNode(canvas);
        drawLeftNodes(canvas);
        drawRightNodes(canvas);
        drawBottomNodes(canvas);
    }

    private void drawMainNode(Canvas canvas) {
        int W = getWidth();
        int H = getHeight();

        // 测量主节点高度和宽度
        measureNode(mainNode);
        mainNode.setX(W / 2 - mainNode.getWidth() / 2);
        mainNode.setY(H / 3 - mainNode.getHeight() / 2);

        // 主节点的中心点在（W/2,H/3）
        // 画主节点的bitmap
        drawNode(mainNode, canvas);
    }

    private void drawLeftNodes(Canvas canvas) {
        if (leftNodes == null || leftNodes.size() == 0) {
            return;
        }
        //画横线
        int startHorizontalLineX = mainNode.getX() - horizontalLineLength;
        int startHorizontalLineY = mainNode.getY() + mainNode.getHeight() / 2;
        canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
        // 画节点
        if (leftNodes.size() == 1) {
            // 直接画节点
            // 1.测量节点
            E2ENode e2ENode = leftNodes.get(0);
            measureNode(e2ENode);
            e2ENode.setX(mainNode.getX() - horizontalLineLength - e2ENode.getWidth());
            e2ENode.setY(mainNode.getY() + mainNode.getHeight() / 2 - e2ENode.getHeight() / 2);
            // 2.画出来
            drawNode(e2ENode, canvas);
            // 递归调用
            drawLeftChilds(e2ENode, canvas, getHeight() / 3 * 2);
        } else {
            int maxH = getHeight() / 3 * 2;
            int divideLength = maxH / (leftNodes.size() + 1);
            // 画竖线
            int nodeRegionY = mainNode.getY() + mainNode.getHeight() / 2 - maxH / 2;
            int startVerticalLineY = nodeRegionY + divideLength;
            int startVertivalLineX = mainNode.getX() - horizontalLineLength;
            canvas.drawLine(startVertivalLineX, startVerticalLineY, startVertivalLineX, nodeRegionY + divideLength * leftNodes.size(), mPaint);

            for (int i = 0; i < leftNodes.size(); i++) {
                // 画横线
                startHorizontalLineX = mainNode.getX() - horizontalLineLength - horizontalLineLength;
                startHorizontalLineY = nodeRegionY + divideLength * (i + 1);
                canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
                E2ENode e2ENode = leftNodes.get(i);
                measureNode(e2ENode);
                e2ENode.setX(startHorizontalLineX - e2ENode.getWidth());
                e2ENode.setY(startHorizontalLineY - e2ENode.getHeight() / 2);
                drawNode(e2ENode, canvas);

                // 递归调用
                drawLeftChilds(e2ENode, canvas, getHeight() / 3 * 2 / leftNodes.size());
            }

        }
    }

    private void drawRightNodes(Canvas canvas) {
        if (rightNodes == null || rightNodes.size() == 0) {
            return;
        }
        //画横线
        int startHorizontalLineX = mainNode.getX() + mainNode.getWidth();
        int startHorizontalLineY = mainNode.getY() + mainNode.getHeight() / 2;
        canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
        // 画节点
        if (rightNodes.size() == 1) {
            // 直接画节点
            // 1.测量节点
            E2ENode e2ENode = rightNodes.get(0);
            measureNode(e2ENode);
            e2ENode.setX(mainNode.getX() + horizontalLineLength);
            e2ENode.setY(mainNode.getY() + mainNode.getHeight() / 2 - e2ENode.getHeight() / 2);
            // 2.画出来
            drawNode(e2ENode, canvas);
            // 递归调用
            drawRightChilds(e2ENode, canvas, getHeight() / 3 * 2);
        } else {
            int maxH = getHeight() / 3 * 2;
            int divideLength = maxH / (rightNodes.size() + 1);
            // 画竖线
            int nodeRegionY = mainNode.getY() + mainNode.getHeight() / 2 - maxH / 2;
            int startVerticalLineY = nodeRegionY + divideLength;
            int startVertivalLineX = mainNode.getX() + mainNode.getWidth() + horizontalLineLength;
            canvas.drawLine(startVertivalLineX, startVerticalLineY, startVertivalLineX, nodeRegionY + divideLength * rightNodes.size(), mPaint);

            for (int i = 0; i < rightNodes.size(); i++) {
                // 画横线
                startHorizontalLineX = mainNode.getX() + mainNode.getWidth() + horizontalLineLength;
                startHorizontalLineY = nodeRegionY + divideLength * (i + 1);
                canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
                E2ENode e2ENode = rightNodes.get(i);
                measureNode(e2ENode);
                e2ENode.setX(startHorizontalLineX + horizontalLineLength);
                e2ENode.setY(startHorizontalLineY - e2ENode.getHeight() / 2);
                drawNode(e2ENode, canvas);

                // 递归调用
                drawRightChilds(e2ENode, canvas, getHeight() / 3 * 2 / rightNodes.size());
            }

        }
    }

    private void drawBottomNodes(Canvas canvas) {
        if (bottomNodes == null || bottomNodes.size() == 0) {
            return;
        }
        // 画竖线
        int startVerticalLineX = mainNode.getX() + mainNode.getWidth() / 2;
        int startVerdticalLineY = mainNode.getY() + mainNode.getHeight();
        canvas.drawLine(startVerticalLineX, startVerdticalLineY, startVerticalLineX, startVerdticalLineY + verticalLineLength, mPaint);
        // 画节点
        if (bottomNodes.size() == 1) {
            // 直接画节点
            // 1.测量节点
            E2ENode e2ENode = bottomNodes.get(0);
            measureNode(e2ENode);
            e2ENode.setX(mainNode.getX() + mainNode.getWidth() / 2 - e2ENode.getWidth() / 2);
            e2ENode.setY(mainNode.getY() + mainNode.getHeight() + verticalLineLength);

            // 2.画出来
            drawNode(e2ENode, canvas);

            drawBottomChilds(e2ENode, canvas, getWidth());
        } else {
            int maxW = getWidth();
            int divideLength = maxW / (bottomNodes.size() + 1);
            // 画横线
            int nodeRegionX = mainNode.getX() + mainNode.getWidth() / 2 - maxW / 2;
            int startHorizontalLineX = nodeRegionX + divideLength;
            int startHorizontalLineY = mainNode.getY() + mainNode.getHeight() + verticalLineLength;
            canvas.drawLine(startHorizontalLineX, startHorizontalLineY, nodeRegionX + divideLength * bottomNodes.size(), startHorizontalLineY, mPaint);

            for (int i = 0; i < bottomNodes.size(); i++) {
                // 画竖线
                startVerticalLineX = nodeRegionX + divideLength * (i + 1);
                startVerdticalLineY = mainNode.getY() + mainNode.getHeight() + verticalLineLength;
                canvas.drawLine(startVerticalLineX, startVerdticalLineY, startVerticalLineX, startVerdticalLineY + verticalLineLength, mPaint);
                // 测量和画节点
                E2ENode e2ENode = bottomNodes.get(i);
                int centerX = nodeRegionX + divideLength * (i + 1);
                int y = startVerdticalLineY + verticalLineLength;
                measureNode(e2ENode);
                e2ENode.setX(centerX - e2ENode.getWidth() / 2);
                e2ENode.setY(y);
                drawNode(e2ENode, canvas);

                // 递归调用
                drawBottomChilds(e2ENode, canvas, maxW / bottomNodes.size());
            }
        }
    }

    /**
     * 递归方法 画向下方向的节点
     *
     * @param parentNode
     * @param canvas
     * @param maxW       此节点下可分配的最长长度
     */
    private void drawBottomChilds(E2ENode parentNode, Canvas canvas, int maxW) {
        if (parentNode.getChilds() == null || parentNode.getChilds().size() == 0)
            return;
        // 画竖线
        int startVerticalLineX = parentNode.getX() + parentNode.getWidth() / 2;
        int startVerdticalLineY = parentNode.getY() + parentNode.getHeight();
        canvas.drawLine(startVerticalLineX, startVerdticalLineY, startVerticalLineX, startVerdticalLineY + verticalLineLength, mPaint);
        if (parentNode.getChilds().size() == 1) {
            // 直接画节点
            // 1.测量节点
            E2ENode e2ENode = parentNode.getChilds().get(0);
            e2ENode.setX(parentNode.getX());
            e2ENode.setY(parentNode.getY() + parentNode.getHeight() + verticalLineLength);
            measureNode(e2ENode);
            // 2.画出来
            drawNode(e2ENode, canvas);

            // 递归调用
            drawBottomChilds(e2ENode, canvas, maxW / parentNode.getChilds().size());

        } else {
            int divideLength = maxW / (parentNode.getChilds().size() + 1);
            // 画横线
            int nodeRegionX = parentNode.getX() + parentNode.getWidth() / 2 - maxW / 2;
            int startHorizontalLineX = nodeRegionX + divideLength;
            int startHorizontalLineY = parentNode.getY() + parentNode.getHeight() + verticalLineLength;
            canvas.drawLine(startHorizontalLineX, startHorizontalLineY, nodeRegionX + divideLength * parentNode.getChilds().size(), startHorizontalLineY, mPaint);

            for (int i = 0; i < parentNode.getChilds().size(); i++) {
                // 画竖线
                startVerticalLineX = nodeRegionX + divideLength * (i + 1);
                startVerdticalLineY = parentNode.getY() + parentNode.getHeight() + verticalLineLength;
                canvas.drawLine(startVerticalLineX, startVerdticalLineY, startVerticalLineX, startVerdticalLineY + verticalLineLength, mPaint);
                // 测量和画节点
                E2ENode e2ENode = parentNode.getChilds().get(i);
                int centerX = nodeRegionX + divideLength * (i + 1);
                int y = startVerdticalLineY + verticalLineLength;
                measureNode(e2ENode);
                e2ENode.setX(centerX - e2ENode.getWidth() / 2);
                e2ENode.setY(y);
                drawNode(e2ENode, canvas);
                // 递归调用
                drawBottomChilds(e2ENode, canvas, maxW / parentNode.getChilds().size());
            }
        }
    }

    /**
     * 递归方法 画向左的节点
     *
     * @param parentNode
     * @param canvas
     * @param maxH
     */
    private void drawLeftChilds(E2ENode parentNode, Canvas canvas, int maxH) {
        if (parentNode == null || parentNode.getChilds().size() == 0) {
            return;
        }
        //画横线
        int startHorizontalLineX = parentNode.getX() - horizontalLineLength;
        int startHorizontalLineY = parentNode.getY() + parentNode.getHeight() / 2;
        canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
        // 画节点
        if (parentNode.getChilds().size() == 1) {
            // 直接画节点
            // 1.测量节点
            E2ENode e2ENode = parentNode.getChilds().get(0);
            measureNode(e2ENode);
            e2ENode.setX(parentNode.getX() - horizontalLineLength - e2ENode.getWidth());
            e2ENode.setY(parentNode.getY() + parentNode.getHeight() / 2 - e2ENode.getHeight() / 2);
            // 2.画出来
            drawNode(e2ENode, canvas);
            // 递归调用
            drawLeftChilds(e2ENode, canvas, maxH / parentNode.getChilds().size());
        } else {
            int divideLength = maxH / (parentNode.getChilds().size() + 1);
            // 画竖线
            int nodeRegionY = parentNode.getY() + parentNode.getHeight() / 2 - maxH / 2;
            int startVerticalLineY = nodeRegionY + divideLength;
            int startVertivalLineX = parentNode.getX() - horizontalLineLength;
            canvas.drawLine(startVertivalLineX, startVerticalLineY, startVertivalLineX, nodeRegionY + divideLength * parentNode.getChilds().size(), mPaint);

            for (int i = 0; i < parentNode.getChilds().size(); i++) {
                // 画横线
                startHorizontalLineX = parentNode.getX() - horizontalLineLength - horizontalLineLength;
                startHorizontalLineY = nodeRegionY + divideLength * (i + 1);
                canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
                E2ENode e2ENode = parentNode.getChilds().get(i);
                measureNode(e2ENode);
                e2ENode.setX(startHorizontalLineX - e2ENode.getWidth());
                e2ENode.setY(startHorizontalLineY - e2ENode.getHeight() / 2);
                drawNode(e2ENode, canvas);

                // 递归调用
                drawLeftChilds(e2ENode, canvas, maxH / parentNode.getChilds().size());
            }

        }
    }

    /**
     * 递归方法 画向右的节点
     *
     * @param parentNode
     * @param canvas
     * @param maxH
     */
    private void drawRightChilds(E2ENode parentNode, Canvas canvas, int maxH) {
        if (parentNode == null || parentNode.getChilds().size() == 0) {
            return;
        }
        //画横线
        int startHorizontalLineX = parentNode.getX() + parentNode.getWidth();
        int startHorizontalLineY = parentNode.getY() + parentNode.getHeight() / 2;
        canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
        // 画节点
        if (parentNode.getChilds().size() == 1) {
            // 直接画节点
            // 1.测量节点
            E2ENode e2ENode = parentNode.getChilds().get(0);
            measureNode(e2ENode);
            e2ENode.setX(parentNode.getX() + parentNode.getWidth() + horizontalLineLength);
            e2ENode.setY(parentNode.getY() + parentNode.getHeight() / 2 - e2ENode.getHeight() / 2);
            // 2.画出来
            drawNode(e2ENode, canvas);
            // 递归调用
            drawRightChilds(e2ENode, canvas, maxH / parentNode.getChilds().size());
        } else {
            int divideLength = maxH / (parentNode.getChilds().size() + 1);
            // 画竖线
            int nodeRegionY = parentNode.getY() + parentNode.getHeight() / 2 - maxH / 2;
            int startVerticalLineY = nodeRegionY + divideLength;
            int startVerticalLineX = parentNode.getX() + parentNode.getWidth() + horizontalLineLength;
            canvas.drawLine(startVerticalLineX, startVerticalLineY, startVerticalLineX, nodeRegionY + divideLength * parentNode.getChilds().size(), mPaint);

            for (int i = 0; i < parentNode.getChilds().size(); i++) {
                // 画横线
                startHorizontalLineX = parentNode.getX() + parentNode.getWidth() + horizontalLineLength;
                startHorizontalLineY = nodeRegionY + divideLength * (i + 1);
                canvas.drawLine(startHorizontalLineX, startHorizontalLineY, startHorizontalLineX + horizontalLineLength, startHorizontalLineY, mPaint);
                E2ENode e2ENode = parentNode.getChilds().get(i);
                measureNode(e2ENode);
                e2ENode.setX(startHorizontalLineX + horizontalLineLength);
                e2ENode.setY(startHorizontalLineY - e2ENode.getHeight() / 2);
                drawNode(e2ENode, canvas);

                // 递归调用
                drawRightChilds(e2ENode, canvas, maxH / parentNode.getChilds().size());
            }

        }
    }

    /**
     * 计算和保存节点的信息
     * 必须设置节点的text(文字信息)
     *
     * @param e2ENode
     */
    private void measureNode(E2ENode e2ENode) {
        // 测量保存节点长和宽
        String text = e2ENode.getText();
        Rect textRect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), textRect);
        e2ENode.setTextHeight(textRect.height());
        e2ENode.setTextWidth(textRect.width());
        e2ENode.setWidth(textRect.width() + e2ENode.getTextPaddingLeft() * 2);
        e2ENode.setHeight(textRect.height() + e2ENode.getTextPaddingTop() * 2);

        // 测量保存连接线
        /*
        if (e2ENode.getParent() == null)
            return;
        switch (e2ENode.getType()) {
            case E2ENode.TYPE_BOTTOM: {
                int nodeX = e2ENode.getX() + e2ENode.getWidth() / 2;
                int nodeY = e2ENode.getY();
                int parentX = e2ENode.getParent().getX() + e2ENode.getParent().getWidth() / 2;
                int parentY = e2ENode.getParent().getY() + e2ENode.getParent().getHeight();
                if (e2ENode.getParent().getChilds().size()==1) {
                    // 父节点只有一个子节点
                    e2ENode.getPath().reset();
                    e2ENode.getPath().moveTo(nodeX, nodeY);
                    e2ENode.getPath().lineTo(parentX, parentY);
                    e2ENode.getPath().close();
                } else {
                    // 父节点有多个子节点
                    e2ENode.getPath().reset();
                    e2ENode.getPath().moveTo(nodeX, nodeY);
                    e2ENode.getPath().lineTo(nodeX, nodeY - verticalLineLength);
                    e2ENode.getPath().lineTo(parentX, nodeY - verticalLineLength);
                    e2ENode.getPath().lineTo(parentX, parentY);
                    e2ENode.getPath().close();
                }
                break;
            }
            case E2ENode.TYPE_LEFT: {
                int nodeX = e2ENode.getX() + e2ENode.getWidth();
                int nodeY = e2ENode.getY()+e2ENode.getHeight()/2;
                int parentX = e2ENode.getParent().getX();
                int parentY = e2ENode.getParent().getY() - e2ENode.getParent().getHeight()/2;
                if (e2ENode.getParent().getChilds().size()==1) {
                    // 父节点只有一个子节点
                    e2ENode.getPath().reset();
                    e2ENode.getPath().moveTo(nodeX, nodeY);
                    e2ENode.getPath().lineTo(parentX, parentY);
                    e2ENode.getPath().close();
                } else {
                    // 父节点有多个子节点
                    e2ENode.getPath().reset();
                    e2ENode.getPath().moveTo(nodeX, nodeY);
                    e2ENode.getPath().lineTo(nodeX+horizontalLineLength, nodeY);
                    e2ENode.getPath().lineTo(nodeX+horizontalLineLength, parentY);
                    e2ENode.getPath().lineTo(parentX, parentY);
                    e2ENode.getPath().close();
                }
                break;
            }
            case E2ENode.TYPE_RIGHT: {
                int nodeX = e2ENode.getX();
                int nodeY = e2ENode.getY()+e2ENode.getHeight()/2;
                int parentX = e2ENode.getParent().getX()+e2ENode.getParent().getWidth();
                int parentY = e2ENode.getParent().getY() - e2ENode.getParent().getHeight()/2;
                if (e2ENode.getParent().getChilds().size()==1) {
                    // 父节点只有一个子节点
                    e2ENode.getPath().reset();
                    e2ENode.getPath().moveTo(nodeX, nodeY);
                    e2ENode.getPath().lineTo(parentX, parentY);
                    e2ENode.getPath().close();
                } else {
                    // 父节点有多个子节点
                    e2ENode.getPath().reset();
                    e2ENode.getPath().moveTo(nodeX, nodeY);
                    e2ENode.getPath().lineTo(nodeX-horizontalLineLength, nodeY);
                    e2ENode.getPath().lineTo(nodeX+horizontalLineLength, parentY);
                    e2ENode.getPath().lineTo(parentX, parentY);
                    e2ENode.getPath().close();
                }
                break;
            }
            default:
                break;
        }
        */
    }

    /**
     * 绘制节点
     * 必须提前设置好节点的x,y,width,height,textWidth,textHeight
     *
     * @param e2ENode
     */
    private void drawNode(E2ENode e2ENode, Canvas canvas) {
        // 画节点的图像
        int bitmapLeft = e2ENode.getX();
        int bitmapTop = e2ENode.getY();
        Rect bitmapRect = new Rect(bitmapLeft, bitmapTop, bitmapLeft + e2ENode.getWidth(), bitmapTop + e2ENode.getHeight());
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), e2ENode.getDrawable()), null, bitmapRect, mPaint);
        // 画节点的文字
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mPaint.setTextSize(e2ENode.getTextSize());
        canvas.drawText(e2ENode.getText(), e2ENode.getX() + e2ENode.getWidth() / 2 - e2ENode.getTextWidth() / 2, e2ENode.getY() + mainNode.getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom, mPaint);
        // 画节点的线
        //canvas.drawPath(e2ENode.getPath(),mPaint);
    }

//    private void drawNodePoint(E2ENode e2ENode, Canvas canvas) {
//        E2ENodePoint e2ENodePoint = e2ENode.getE2ENodePoint();
//        if (e2ENodePoint == null) {
//            return;
//        }
//        canvas.drawCircle(e2ENodePoint.getX(),e2ENodePoint.getY(),e2ENodePoint.getR(),mPaint);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            E2ENode nodeClicked = findNodeClicked(x, y);
            if (nodeClicked != null) {
                if (onE2eNodeItemClicked != null)
                    onE2eNodeItemClicked.onItemClicked(nodeClicked);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 查找所有节点 找到点击的节点
     *
     * @param x
     * @param y
     * @return 返回非空则为被点击的节点
     */
    private E2ENode findNodeClicked(int x, int y) {
        // 查找mainNode
        if (mainNode != null) {
            if (mainNode.isClicked(x, y))
                return mainNode;
        }
        // 查找bottoNodes
        if (bottomNodes != null) {
            for (int i = 0; i < bottomNodes.size(); i++) {
                E2ENode e2ENode = bottomNodes.get(i);
                if (e2ENode.isClicked(x, y)) {
                    return e2ENode;
                } else {
                    E2ENode childClicked = e2ENode.findChildClicked(x, y);
                    if (childClicked != null) {
                        return childClicked;
                    }
                }
            }
        }
        // 查找leftNodes
        if (leftNodes != null) {
            for (int i = 0; i < leftNodes.size(); i++) {
                E2ENode e2ENode = leftNodes.get(i);
                if (e2ENode.isClicked(x, y)) {
                    return e2ENode;
                } else {
                    E2ENode childClicked = e2ENode.findChildClicked(x, y);
                    if (childClicked != null) {
                        return childClicked;
                    }
                }
            }
        }
        // 查找rightNodes
        if (rightNodes != null) {
            for (int i = 0; i < rightNodes.size(); i++) {
                E2ENode e2ENode = rightNodes.get(i);
                if (e2ENode.isClicked(x, y)) {
                    return e2ENode;
                } else {
                    E2ENode childClicked = e2ENode.findChildClicked(x, y);
                    if (childClicked != null) {
                        return childClicked;
                    }
                }
            }
        }

        return null;
    }

    // ===============================对外设置参数api============================================


    public E2EGplotView setBottomNodes(List<E2ENode> bottomNodes) {
        this.bottomNodes = bottomNodes;
        return this;
    }

    public E2EGplotView setLeftNodes(List<E2ENode> leftNodes) {
        this.leftNodes = leftNodes;
        return this;
    }

    public E2EGplotView setRightNodes(List<E2ENode> rightNodes) {
        this.rightNodes = rightNodes;
        return this;
    }

    public E2EGplotView setMainNode(E2ENode mainNode) {
        this.mainNode = mainNode;
        return this;
    }

    public E2EGplotView setHorizontalLineLength(int horizontalLineLength) {
        this.horizontalLineLength = horizontalLineLength;
        return this;
    }

    public E2EGplotView setVerticalLineLength(int verticalLineLength) {
        this.verticalLineLength = verticalLineLength;
        return this;
    }

    public void show() {
        invalidate();
    }

    public void setOnE2eNodeItemClicked(OnE2eNodeItemClicked onE2eNodeItemClicked) {
        this.onE2eNodeItemClicked = onE2eNodeItemClicked;
    }

    /**
     * 测试打印日志
     *
     * @param text
     */
    private void log(String text) {
        Log.e(getClass().getSimpleName(), text);
    }
}
