package com.example.diyview.customview.e2egplotview;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.example.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 拓扑图节点
 *
 * @author 韩龙林
 * @date 2019/5/30 16:11
 */
public class E2ENode {
    public static final int TYPE_LEFT=1;
    public static final int TYPE_RIGHT=2;
    public static final int TYPE_BOTTOM=3;

    int type=TYPE_BOTTOM;
    /**
     * 左上角x
     */
    int x = 0;
    /**
     * 左上角y
     */
    int y = 0;
    String text = "";
    int textSize = 30;
    int textPaddingLeft = 30;
    int textPaddingTop = 20;
    int drawable = R.drawable.back4;
    int width = 0;
    int height = 0;
    int textWidth = 0;
    int textHeight = 0;
    /**
     * 到父节点的连接线
     */
    Path path=new Path();

    E2ENode parent = null;
    List<E2ENode> childs = new ArrayList<>();


//    E2ENodePoint e2ENodePoint = null;

    public E2ENode(int type,String text) {
        this.type=type;
        this.text = text;
        init();
    }

    public E2ENode(int type,String text, int drawable) {
        this.type=type;
        this.text = text;
        this.drawable = drawable;
        init();
    }


    public E2ENode(int type,String text, int textSize, int drawable) {
        this.type=type;
        this.text = text;
        this.textSize = textSize;
        this.drawable = drawable;
        init();
    }

    private void init() {
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.BLACK);
//        paint.setAntiAlias(true);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTextHeight(int textHeight) {
        this.textHeight = textHeight;
    }

    public void setTextWidth(int textWidth) {
        this.textWidth = textWidth;
    }

    public int getTextWidth() {
        return textWidth;
    }

    public int getTextHeight() {
        return textHeight;
    }

    public int getTextPaddingLeft() {
        return textPaddingLeft;
    }

    public int getTextPaddingTop() {
        return textPaddingTop;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public E2ENode getParent() {
        return parent;
    }

    public void setParent(E2ENode parent) {
        this.parent = parent;
    }

    public List<E2ENode> getChilds() {
        return childs;
    }

    public void setChilds(List<E2ENode> childs) {
        this.childs = childs;
        for (E2ENode e2ENode : childs) {
            e2ENode.setParent(this);
        }
    }

//    public void setE2ENodePoint(E2ENodePoint e2ENodePoint) {
//        this.e2ENodePoint = e2ENodePoint;
//    }
//
//    public E2ENodePoint getE2ENodePoint() {
//        return e2ENodePoint;
//    }
    //=============================功能函数=====================================

    public void addChild(E2ENode node) {
        childs.add(node);
        node.setParent(this);
    }

    public void removeChild(E2ENode node) {
        childs.remove(node);
    }

    public void recycle() {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        textWidth = 0;
        textHeight = 0;
        text = "";
        parent = null;
        childs.clear();
    }

    /**
     * 是否被点击
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isClicked(int x, int y) {
        if (x > getX()
                && x < (getX() + getWidth())
                && y > getY()
                && y < (getY() + getHeight())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 递归查找子节点是否被点击
     *
     * @param x
     * @param y
     * @return 返回非空表示找到了被点击的节点
     */
    public E2ENode findChildClicked(int x, int y) {
        if (getChilds() == null || getChilds().size() == 0) {
            return null;
        }
        for (int i = 0; i < getChilds().size(); i++) {
            E2ENode e2ENode = getChilds().get(i);
            if (e2ENode.isClicked(x, y)) {
                return e2ENode;
            } else {
                E2ENode childClicked = e2ENode.findChildClicked(x, y);
                if (childClicked != null) {
                    return childClicked;
                }
            }
        }
        return null;
    }


}
