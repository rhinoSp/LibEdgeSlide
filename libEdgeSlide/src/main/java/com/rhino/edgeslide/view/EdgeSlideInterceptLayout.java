package com.rhino.edgeslide.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author rhino
 * @since Create on 2019/12/17.
 **/
public class EdgeSlideInterceptLayout extends FrameLayout {

    private float slideActiveLength = 0; // 边缘滑动响应距离
    private boolean isAllowEdgeLeft; // 使用左侧侧滑
    private boolean isAllowEdgeRight; // 使用右侧侧滑
    private boolean isAllowEdgeTop; // 使用顶部侧滑
    private boolean isAllowEdgeBottom; // 使用底部侧滑

    public EdgeSlideInterceptLayout(Context context) {
        this(context, null);
    }

    public EdgeSlideInterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EdgeSlideInterceptLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isAllowEdgeLeft && ev.getRawX() <= slideActiveLength) {
                return true;
            }
            if (isAllowEdgeRight && ev.getRawX() >= getWidth() - slideActiveLength) {
                return true;
            }
            if (isAllowEdgeTop && ev.getRawY() <= slideActiveLength) {
                return true;
            }
            if (isAllowEdgeBottom && ev.getRawY() >= getHeight() - slideActiveLength) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setSlideActiveLength(float slideActiveLength) {
        this.slideActiveLength = slideActiveLength;
    }

    public void setAllowEdgeLeft(boolean allowEdgeLeft) {
        isAllowEdgeLeft = allowEdgeLeft;
    }

    public void setAllowEdgeRight(boolean allowEdgeRight) {
        isAllowEdgeRight = allowEdgeRight;
    }

    public void setAllowEdgeTop(boolean allowEdgeTop) {
        isAllowEdgeTop = allowEdgeTop;
    }

    public void setAllowEdgeBottom(boolean allowEdgeBottom) {
        isAllowEdgeBottom = allowEdgeBottom;
    }
}