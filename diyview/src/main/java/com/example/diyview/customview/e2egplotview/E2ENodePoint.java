package com.example.diyview.customview.e2egplotview;

/**
 * 节点连接线上的红点 可以跳动
 *
 * @author 韩龙林
 * @date 2019/5/31 14:20
 */
public class E2ENodePoint {

    public static final int TYPE_LEFT=1;
    public static final int TYPE_RIGHT=2;
    public static final int TYPE_BOTTOM=3;

    int type=TYPE_BOTTOM;
    int x;
    int y;
    int r;

    public E2ENodePoint() {
    }

    public E2ENodePoint(int type, int x, int y, int r) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public static int getTypeLeft() {
        return TYPE_LEFT;
    }

    public static int getTypeRight() {
        return TYPE_RIGHT;
    }

    public static int getTypeBottom() {
        return TYPE_BOTTOM;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
