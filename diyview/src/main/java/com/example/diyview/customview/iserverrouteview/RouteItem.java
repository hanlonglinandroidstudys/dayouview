package com.example.diyview.customview.iserverrouteview;

/**
 * 云端路结构条目bean
 *
 * @author 韩龙林
 * @date 2019/5/28 12:48
 */
public class RouteItem {

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

    String text;
    int x = 0;
    int y = 0;
    int width;
    int height;
    boolean checked = false;
    int state = STATE_RUNNING;

    public RouteItem(String text) {
        this.text = text;
    }

    public RouteItem(String text, int state) {
        this.text = text;
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 判断是否被点击
     *
     * @param x 点击x坐标
     * @param y 点击y坐标
     * @return true 被点击 false 未点击
     */
    public boolean isClicked(int x, int y) {
        if ((x < (getX() + getWidth() / 2))
                && (x > (getX() - getWidth() / 2))
                && (y > (getY() - getHeight()))
                && (y < getY())) {
            return true;
        }
        return false;
    }
}
