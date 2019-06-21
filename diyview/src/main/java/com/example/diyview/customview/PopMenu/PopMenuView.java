
package com.example.diyview.customview.PopMenu;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 韩龙林
 * @date 2019/6/20 16:07
 */
public class PopMenuView extends android.support.v7.widget.AppCompatButton {
    private Context context;
    private List<Button> itemList = new ArrayList<>();

    Boolean isMenuOpen = false;
    /**
     * 半径
     **/
    private int radius = 200;

    public PopMenuView(Context context) {
        super(context);
        init(context);
    }

    public PopMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isMenuOpen) {
                isMenuOpen = false;
                openMenu();
            } else {
                isMenuOpen = true;
                closeMenu();
            }
        }
        return super.onTouchEvent(event);
    }

    private void closeMenu() {
        for (int i = 0; i < itemList.size(); i++) {
            doAnimationOpen(itemList.get(i), i + 1, itemList.size());
        }
    }

    private void openMenu() {
        for (int i = 0; i < itemList.size(); i++) {
            doAnimationClose(itemList.get(i), i + 1, itemList.size());
        }
    }

    private void doAnimationOpen(View view, int i, int total) {
        if (view.getVisibility() != VISIBLE) {
            view.setVisibility(VISIBLE);
        }
        double degree = Math.toRadians(180) / (total + 1) * i;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "alpha", 0, 1f)
        );
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    private void doAnimationClose(final View view, int i, int total) {
        double degree = Math.toRadians(180) / (total + 1) * i;
        int translationX = -(int) (radius * Math.sin(degree));
        int translationY = -(int) (radius * Math.cos(degree));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        );
        animatorSet.setDuration(1000);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (view.getVisibility() == VISIBLE) {
                    view.setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void addItem(Button itemBtn, int width, int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        itemBtn.setLayoutParams(params);
        if (height != 0) {
            itemBtn.setHeight(height);
        }
        if (width != 0) {
            itemBtn.setWidth(width);
        }
        itemBtn.setVisibility(GONE);
        if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            parent.addView(itemBtn);
            itemList.add(itemBtn);
        }
    }

    public void addItem(Button itemBtn) {
        addItem(itemBtn, 0, 0);
    }

    public void recycle() {
        if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            Iterator<Button> iterator = itemList.iterator();
            while (iterator.hasNext()) {
                Button next = iterator.next();
                parent.removeView(next);
            }
        }
        itemList.clear();
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

}
