package com.example.diyview.customview.e2eglotview2;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 拓扑图节点
 *
 * @author 韩龙林
 * @date 2019/5/30 16:11
 */
public class TvNode {
    // 状态
    public static final int STATUS_OPEN = 1;
    public static final int STATUS_CLOSE = 0;

    // 位置类型
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;
    public static final int TYPE_BOTTOM = 3;
    public static final int TYPE_MAIN = 4;

    /**
     * 位置类型 有四种
     * <br>
     * TYPE_LEFT 左节点
     * <br>
     * TYPE_RIGHT 右节点
     * <br>
     * TYPE_BOTTOM 下节点
     * <br>
     * TYPE_MAIN  主节点
     * <br>
     * 添加节点时一定要设置节点的位置类型，不然会影响连接线的绘制
     */
    int type = -1;

    /**
     * 状态 有两种
     * <br>
     * STATUS_OPEN    连通状态（默认）
     * <br>
     * STATUS_CLOSE   不连通状态
     * <br>
     * 连通状态的节点在开启动画时会有动画显示
     */
    int status = STATUS_OPEN;

    /**
     * 是否开启动画
     */
    boolean isStartAnim=false;

    /**
     * 属性动画类
     */
    ObjectAnimator objectAnimator=null;

    /**
     * 连接线的点 连接到父节点的线
     */
    List<PointF> linePoints = new ArrayList<>();

    /**
     * 连接线颜色 连接到父节点的线
     */
    int lineColor=Color.GRAY;

    /**
     * 显示控件
     */
    TextView tv;

    /**
     * 父节点
     */
    TvNode parent = null;

    /**
     * 子节点列表
     */
    List<TvNode> childs = new ArrayList<>();

    private TvNode(int type) {
        this.type = type;
    }

    public static TvNode createNode(Context context, int type, String text) {
        return createNode(context, type, text, STATUS_OPEN);
    }

    public static TvNode createNode(Context context, int type, String text, int status) {
        TvNode e2ENode2 = new TvNode(type);
        e2ENode2.setStatus(status);
        e2ENode2.tv = new TextView(context);
        e2ENode2.tv.setText(text);
        e2ENode2.tv.setTextColor(Color.DKGRAY);
        e2ENode2.tv.setTextSize(18);
        e2ENode2.tv.setPadding(10, 3, 10, 3);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        e2ENode2.tv.setGravity(Gravity.CENTER);
        e2ENode2.tv.setBackgroundResource(R.drawable.shape_round_rect);
        e2ENode2.tv.setLayoutParams(params);
        return e2ENode2;
    }

    public void setChilds(List<TvNode> childs) {
        this.childs = childs;
        for (TvNode e2ENode : childs) {
            e2ENode.setParent(this);
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setParent(TvNode parent) {
        this.parent = parent;
    }

    public TvNode getParent() {
        return parent;
    }

    public List<TvNode> getChilds() {
        return childs;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setObjectAnimator(ObjectAnimator objectAnimator) {
        this.objectAnimator = objectAnimator;
    }

    public ObjectAnimator getObjectAnimator() {
        return objectAnimator;
    }

    //=============================外部api=====================================

    public void addChild(TvNode node) {
        childs.add(node);
        node.setParent(this);
    }

    public void removeChild(TvNode node) {
        childs.remove(node);
    }

    public void addLinePoint(PointF p) {
        this.linePoints.add(p);
    }

    public void clearLinePoint() {
        this.linePoints.clear();
    }

    public void recycle() {
        tv = null;
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
        if (x > tv.getX()
                && x < (tv.getX() + tv.getWidth())
                && y > tv.getY()
                && y < (tv.getY() + tv.getHeight())) {
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
    public TvNode findChildClicked(int x, int y) {
        if (getChilds() == null || getChilds().size() == 0) {
            return null;
        }
        for (int i = 0; i < getChilds().size(); i++) {
            TvNode e2ENode = getChilds().get(i);
            if (e2ENode.isClicked(x, y)) {
                return e2ENode;
            } else {
                TvNode childClicked = e2ENode.findChildClicked(x, y);
                if (childClicked != null) {
                    return childClicked;
                }
            }
        }
        return null;
    }
    public TextView tv() {
        return tv;
    }

}
