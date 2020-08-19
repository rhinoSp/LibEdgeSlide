package com.rhino.edgeslide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.rhino.edgeslide.callback.SlideCallBack;
import com.rhino.edgeslide.view.EdgeSlideView;
import com.rhino.edgeslide.view.EdgeSlideInterceptLayout;


/**
 * @author rhino
 * @since Create on 2019/12/17.
 **/
public class EdgeSlideManager {

    /**
     * 滑动模式 左
     */
    public static final int EDGE_LEFT = 1;

    /**
     * 滑动模式 右
     */
    public static final int EDGE_RIGHT = 2;

    /**
     * 滑动模式 上
     */
    public static final int EDGE_TOP = 4;

    /**
     * 滑动模式 下
     */
    public static final int EDGE_BOTTOM = 8;
    /**
     * 滑动控件 左
     */
    private EdgeSlideView edgeSlideViewLeft;
    /**
     * 滑动控件 右
     */
    private EdgeSlideView edgeSlideViewRight;
    /**
     * 滑动控件 上
     */
    private EdgeSlideView edgeSlideViewTop;
    /**
     * 滑动控件 下
     */
    private EdgeSlideView edgeSlideViewBottom;

    /**
     * 允许滑动 左
     */
    private boolean isAllowEdgeLeft;
    /**
     * 允许滑动 右
     */
    private boolean isAllowEdgeRight;
    /**
     * 允许滑动 上
     */
    private boolean isAllowEdgeTop;
    /**
     * 允许滑动 下
     */
    private boolean isAllowEdgeBottom;

    /**
     * 上下文
     */
    private Context context;
    /**
     * 滑动区域容器
     */
    private FrameLayout container;
    /**
     * 滑动区域容器中是否包含滑动控件
     */
    private boolean containScrollChildView;

    /**
     * 回调
     */
    private SlideCallBack callBack;

    /**
     * 滑动控件 背景色
     */
    @ColorInt
    private int slideViewBackgroundColor = Color.BLACK;
    /**
     * 滑动控件 箭头颜色
     */
    @ColorInt
    private int arrowColor = Color.WHITE;
    /**
     * 滑动控件 宽
     */
    private float slideViewWidth;
    /**
     * 滑动控件 箭头图标大小
     */
    private float arrowSize;
    /**
     * 滑动控件 最大拉动距离
     */
    private float maxSlideLength;

    /**
     * 响应距离
     */
    private float slideActiveLength;
    /**
     * 阻尼系数
     */
    private float dragRate;


    public static EdgeSlideManager with(Activity activity) {
        return new EdgeSlideManager((FrameLayout) activity.getWindow().getDecorView());
    }

    public static EdgeSlideManager with(FrameLayout container) {
        return new EdgeSlideManager(container);
    }

    EdgeSlideManager(FrameLayout container) {
        this.context = container.getContext().getApplicationContext();
        this.container = container;
        containScrollChildView = false;

        slideViewWidth = dp2px(30);
        arrowSize = dp2px(5);
        slideActiveLength = dp2px(40);
        maxSlideLength = dp2px(50);
        dragRate = 3;

        isAllowEdgeLeft = true;
        isAllowEdgeRight = false;
        isAllowEdgeTop = false;
        isAllowEdgeBottom = false;
    }

    /**
     * 回调
     */
    public EdgeSlideManager callBack(SlideCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    /**
     * 设置滑动区域容器
     *
     * @param container 滑动区域容器
     */
    public void setContainer(FrameLayout container) {
        this.container = container;
    }

    /**
     * 滑动区域容器中是否包含滑动控件 默认false
     */
    public EdgeSlideManager containScrollChildView(boolean containScrollChildView) {
        this.containScrollChildView = containScrollChildView;
        return this;
    }

    /**
     * 设置返回Icon背景色
     *
     * @param backgroundColor ColorInt
     */
    public EdgeSlideManager setSlideViewBackgroundColor(@ColorInt int backgroundColor) {
        this.slideViewBackgroundColor = backgroundColor;
        return this;
    }

    /**
     * 滑动控件 箭头颜色
     *
     * @param arrowColor ColorInt
     */
    public EdgeSlideManager setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
        return this;
    }

    /**
     * 滑动控件 宽 dp
     */
    public EdgeSlideManager setSlideViewWidth(float slideViewWidth) {
        this.slideViewWidth = dp2px(slideViewWidth);
        return this;
    }

    /**
     * 滑动控件 箭头图标大小 dp
     */
    public EdgeSlideManager setArrowSize(float arrowSize) {
        this.arrowSize = dp2px(arrowSize);
        return this;
    }

    /**
     * 滑动控件 最大拉动距离 dp
     */
    public EdgeSlideManager setMaxSlideLength(float maxSlideLength) {
        this.maxSlideLength = dp2px(maxSlideLength);
        return this;
    }

    /**
     * 滑动控件 响应距离 dp
     */
    public EdgeSlideManager setSlideActiveLength(float slideActiveLength) {
        this.slideActiveLength = dp2px(slideActiveLength);
        return this;
    }

    /**
     * 阻尼系数 默认3（越小越灵敏）
     */
    public EdgeSlideManager setDragRate(float dragRate) {
        this.dragRate = dragRate;
        return this;
    }

    /**
     * 设置滑动模式
     */
    public EdgeSlideManager setEdgeMode(int edgeMode) {
        isAllowEdgeLeft = (edgeMode & EDGE_LEFT) != 0;
        isAllowEdgeRight = (edgeMode & EDGE_RIGHT) != 0;
        isAllowEdgeTop = (edgeMode & EDGE_TOP) != 0;
        isAllowEdgeBottom = (edgeMode & EDGE_BOTTOM) != 0;
        return this;
    }

    /**
     * 需要使用滑动的页面注册
     */
    public void setup() {
        if (containScrollChildView) {
            EdgeSlideInterceptLayout interceptLayout = new EdgeSlideInterceptLayout(context);
            interceptLayout.setSlideActiveLength(slideActiveLength);
            interceptLayout.setAllowEdgeLeft(isAllowEdgeLeft);
            interceptLayout.setAllowEdgeRight(isAllowEdgeRight);
            interceptLayout.setAllowEdgeTop(isAllowEdgeTop);
            interceptLayout.setAllowEdgeBottom(isAllowEdgeBottom);
            addInterceptLayout(container, interceptLayout);
        }
        if (isAllowEdgeLeft) {
            edgeSlideViewLeft = EdgeSlideView.create(context, slideViewBackgroundColor, slideViewWidth,
                    arrowSize, arrowColor, maxSlideLength, EdgeSlideView.ARROW_ORIENTATION_HORIZONTAL);
            container.addView(edgeSlideViewLeft);
        }
        if (isAllowEdgeRight) {
            edgeSlideViewRight = EdgeSlideView.create(context, slideViewBackgroundColor, slideViewWidth,
                    arrowSize, arrowColor, maxSlideLength, EdgeSlideView.ARROW_ORIENTATION_HORIZONTAL);
            edgeSlideViewRight.setRotationY(180);
            container.addView(edgeSlideViewRight);
        }
        if (isAllowEdgeTop) {
            edgeSlideViewTop = EdgeSlideView.create(context, slideViewBackgroundColor, slideViewWidth,
                    arrowSize, arrowColor, maxSlideLength, EdgeSlideView.ARROW_ORIENTATION_VERTICAL);
            container.addView(edgeSlideViewTop);
        }
        if (isAllowEdgeBottom) {
            edgeSlideViewBottom = EdgeSlideView.create(context, slideViewBackgroundColor, slideViewWidth,
                    arrowSize, arrowColor, maxSlideLength, EdgeSlideView.ARROW_ORIENTATION_VERTICAL);
            edgeSlideViewBottom.setRotationX(180);
            container.addView(edgeSlideViewBottom);
        }
        setOnTouch();
    }

    /**
     * 设置监听
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouch() {
        container.setOnTouchListener(new View.OnTouchListener() {
            private boolean isSideSlideLeft = false;  // 是否从左边边缘开始滑动
            private boolean isSideSlideRight = false;  // 是否从右边边缘开始滑动
            private boolean isSideSlideTop = false;  // 是否从顶部边缘开始滑动
            private boolean isSideSlideBottom = false;  // 是否从底部边缘开始滑动
            private float downX = 0; // 按下的X轴坐标
            private float downY = 0; // 按下的Y轴坐标
            private float moveXLength = 0; // 位移的X轴距离
            private float moveYLength = 0; // 位移的Y轴距离

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // 按下
                        // 更新按下点坐标
                        downX = getRealRawX(event);
                        downY = getRealRawY(event);

                        // 检验是否从边缘开始滑动，区分左右
                        if (isAllowEdgeLeft && downX <= slideActiveLength) {
                            isSideSlideLeft = true;
                        } else if (isAllowEdgeRight && downX >= container.getWidth() - slideActiveLength) {
                            isSideSlideRight = true;
                        } else if (isAllowEdgeTop && downY <= slideActiveLength) {
                            isSideSlideTop = true;
                        } else if (isAllowEdgeBottom && downY >= container.getHeight() - slideActiveLength) {
                            isSideSlideBottom = true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE: // 移动
                        if (isSideSlideLeft || isSideSlideRight) { // 从边缘开始滑动
                            // 获取X轴位移距离
                            moveXLength = Math.abs(getRealRawX(event) - downX);

                            float slideLength = Math.min(moveXLength / dragRate, maxSlideLength);
                            // 如果位移距离在可拉动距离内，更新SlideBackIconView的当前拉动距离并重绘，区分左右
                            if (isAllowEdgeLeft && isSideSlideLeft) {
                                edgeSlideViewLeft.updateSlideLength(slideLength);
                            } else if (isAllowEdgeRight && isSideSlideRight) {
                                edgeSlideViewRight.updateSlideLength(slideLength);
                            }

                            // 根据Y轴位置给SlideBackIconView定位
                            if (isAllowEdgeLeft && isSideSlideLeft) {
                                moveEdgeSlideView(edgeSlideViewLeft, EdgeSlideView.ARROW_ORIENTATION_VERTICAL, getRealRawY(event));
                            } else if (isAllowEdgeRight && isSideSlideRight) {
                                moveEdgeSlideView(edgeSlideViewRight, EdgeSlideView.ARROW_ORIENTATION_VERTICAL, getRealRawY(event));
                            }
                        }
                        if (isSideSlideTop || isSideSlideBottom) { // 从边缘开始滑动
                            // 获取Y轴位移距离
                            moveYLength = Math.abs(getRealRawY(event) - downY);

                            float slideLength = Math.min(moveYLength / dragRate, maxSlideLength);
                            // 如果位移距离在可拉动距离内，更新SlideBackIconView的当前拉动距离并重绘，区分左右
                            if (isAllowEdgeTop && isSideSlideTop) {
                                edgeSlideViewTop.updateSlideLength(slideLength);
                            } else if (isAllowEdgeBottom && isSideSlideBottom) {
                                edgeSlideViewBottom.updateSlideLength(slideLength);
                            }

                            // 根据X轴位置给SlideBackIconView定位
                            if (isAllowEdgeTop && isSideSlideTop) {
                                moveEdgeSlideView(edgeSlideViewTop, EdgeSlideView.ARROW_ORIENTATION_HORIZONTAL, getRealRawX(event));
                            } else if (isAllowEdgeBottom && isSideSlideBottom) {
                                moveEdgeSlideView(edgeSlideViewBottom, EdgeSlideView.ARROW_ORIENTATION_HORIZONTAL, getRealRawX(event));
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP: // 抬起
                        // 是从边缘开始滑动 且 抬起点的X轴坐标大于某值(默认3倍最大滑动长度) 且 回调不为空
                        if (((isSideSlideLeft || isSideSlideRight) && moveXLength / dragRate >= maxSlideLength)
                                || ((isSideSlideTop || isSideSlideBottom) && moveYLength / dragRate >= maxSlideLength)) {
                            if (null != callBack) {
                                if (isSideSlideLeft) {
                                    callBack.onSlide(EDGE_LEFT);
                                } else if (isSideSlideRight) {
                                    callBack.onSlide(EDGE_RIGHT);
                                } else if (isSideSlideTop) {
                                    callBack.onSlide(EDGE_TOP);
                                } else if (isSideSlideBottom) {
                                    callBack.onSlide(EDGE_BOTTOM);
                                }
                            }
                        }

                        // 恢复SlideBackIconView的状态
                        if (isAllowEdgeLeft && isSideSlideLeft) {
                            edgeSlideViewLeft.updateSlideLength(0);
                        } else if (isAllowEdgeRight && isSideSlideRight) {
                            edgeSlideViewRight.updateSlideLength(0);
                        } else if (isAllowEdgeTop && isSideSlideTop) {
                            edgeSlideViewTop.updateSlideLength(0);
                        } else if (isAllowEdgeBottom && isSideSlideBottom) {
                            edgeSlideViewBottom.updateSlideLength(0);
                        }

                        // 从边缘开始滑动结束
                        isSideSlideLeft = false;
                        isSideSlideRight = false;
                        isSideSlideTop = false;
                        isSideSlideBottom = false;
                        break;
                    default:
                        break;
                }
                return isSideSlideLeft || isSideSlideRight || isSideSlideTop || isSideSlideBottom;
            }
        });
    }

    /**
     * 获取根据父布局偏移后的点击坐标 X
     *
     * @param event MotionEvent
     * @return X
     */
    private float getRealRawX(MotionEvent event) {
        return event.getRawX() - container.getLeft();
    }

    /**
     * 获取根据父布局偏移后的点击坐标 Y
     *
     * @param event MotionEvent
     * @return Y
     */
    private float getRealRawY(MotionEvent event) {
        return event.getRawY() - container.getTop();
    }

    /**
     * 给根布局包上一层事件拦截处理Layout
     */
    private void addInterceptLayout(ViewGroup decorView, EdgeSlideInterceptLayout interceptLayout) {
        View rootLayout = decorView.getChildAt(0); // 取出根布局
        decorView.removeView(rootLayout); // 先移除根布局
        // 用事件拦截处理Layout将原根布局包起来，再添加回去
        interceptLayout.addView(rootLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        decorView.addView(interceptLayout);
    }

    /**
     * 将根布局还原，移除SlideBackInterceptLayout
     */
    private void removeInterceptLayout(ViewGroup decorView) {
        FrameLayout rootLayout = (FrameLayout) decorView.getChildAt(0); // 取出根布局
        decorView.removeView(rootLayout); // 先移除根布局
        // 将根布局的第一个布局(原根布局)取出放回decorView
        View oriLayout = rootLayout.getChildAt(0);
        rootLayout.removeView(oriLayout);
        decorView.addView(oriLayout);
    }

    /**
     * 移动滑动控件位置
     */
    private void moveEdgeSlideView(EdgeSlideView view, int orientation, float position) {
        int margin = (int) (position - (view.getSlideViewWidth() / 2));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getLayoutParams());
        if (orientation == EdgeSlideView.ARROW_ORIENTATION_VERTICAL) {
            layoutParams.topMargin = margin;
        } else if (orientation == EdgeSlideView.ARROW_ORIENTATION_HORIZONTAL) {
            layoutParams.leftMargin = margin;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * dp 转px
     *
     * @param dpValue dp
     * @return px
     */
    private float dp2px(float dpValue) {
        if (dpValue == 0) {
            return 0;
        }
        return dpValue * context.getResources().getDisplayMetrics().density + 0.5f;
    }
}