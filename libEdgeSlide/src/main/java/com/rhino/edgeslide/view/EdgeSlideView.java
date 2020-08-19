package com.rhino.edgeslide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author rhino
 * @since Create on 2019/12/17.
 **/
public class EdgeSlideView extends View {

    public static final int ARROW_ORIENTATION_VERTICAL = 1;
    public static final int ARROW_ORIENTATION_HORIZONTAL = 2;
    /**
     * 箭头方向
     */
    private int arrowOrientation = ARROW_ORIENTATION_HORIZONTAL;

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

    private Path bgPath, arrowPath;
    private Paint bgPaint, arrowPaint;
    private float slideLength = 0;

    public static EdgeSlideView create(Context context, @ColorInt int backgroundColor, float slideViewWidth,
                                       float arrowSize, @ColorInt int arrowColor,
                                       float maxSlideLength, int orientation) {
        EdgeSlideView edgeSlideView = new EdgeSlideView(context);
        edgeSlideView.setSlideViewBackgroundColor(backgroundColor);
        edgeSlideView.setSlideViewWidth(slideViewWidth);
        edgeSlideView.setArrowSize(arrowSize);
        edgeSlideView.setArrowColor(arrowColor);
        edgeSlideView.setMaxSlideLength(maxSlideLength);
        edgeSlideView.setArrowOrientation(orientation);
        return edgeSlideView;
    }

    public EdgeSlideView(Context context) {
        this(context, null);
    }

    public EdgeSlideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EdgeSlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgPath = new Path();
        arrowPath = new Path();

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bgPaint.setColor(slideViewBackgroundColor);
        bgPaint.setStrokeWidth(1);

        arrowPaint = new Paint();
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setColor(arrowColor);
        arrowPaint.setStrokeWidth(8);
        arrowPaint.setStrokeJoin(Paint.Join.ROUND);

        setAlpha(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 背景
        bgPaint.setColor(slideViewBackgroundColor);
        bgPath.reset(); // 会多次绘制，所以先重置

        if (arrowOrientation == ARROW_ORIENTATION_HORIZONTAL) {
            bgPath.moveTo(0, 0);
            bgPath.cubicTo(0, slideViewWidth * 2 / 9, slideLength, slideViewWidth / 3, slideLength, slideViewWidth / 2);
            bgPath.cubicTo(slideLength, slideViewWidth * 2 / 3, 0, slideViewWidth * 7 / 9, 0, slideViewWidth);
        } else if (arrowOrientation == ARROW_ORIENTATION_VERTICAL) {
            bgPath.moveTo(0, 0);
            bgPath.cubicTo(slideViewWidth * 2 / 9, 0, slideViewWidth / 3, slideLength, slideViewWidth / 2, slideLength);
            bgPath.cubicTo(slideViewWidth * 2 / 3, slideLength, slideViewWidth * 7 / 9, 0, slideViewWidth, 0);
        }
        canvas.drawPath(bgPath, bgPaint); // 根据设置的贝塞尔曲线路径用画笔绘制

        // 箭头
        if (arrowSize > 0) {
            // 箭头是先直线由短变长再折弯变成箭头状的
            // 依据当前拉动距离和最大拉动距离计算箭头大小值
            // 大小到一定值后开始折弯，计算箭头角度值
            float arrowZoom = slideLength / maxSlideLength; // 箭头大小变化率
            float arrowAngle = arrowZoom < 0.75f ? 0 : (arrowZoom - 0.75f) * 2; // 箭头角度变化率
            arrowPath.reset(); // 先重置
            // 结合箭头大小值与箭头角度值设置折线路径
            if (arrowOrientation == ARROW_ORIENTATION_HORIZONTAL) {
                arrowPath.moveTo(slideLength / 2 + (arrowSize * arrowAngle), slideViewWidth / 2 - (arrowZoom * arrowSize));
                arrowPath.lineTo(slideLength / 2 - (arrowSize * arrowAngle), slideViewWidth / 2);
                arrowPath.lineTo(slideLength / 2 + (arrowSize * arrowAngle), slideViewWidth / 2 + (arrowZoom * arrowSize));
            } else if (arrowOrientation == ARROW_ORIENTATION_VERTICAL) {
                arrowPath.moveTo(slideViewWidth / 2 - (arrowZoom * arrowSize), slideLength / 2 + (arrowSize * arrowAngle));
                arrowPath.lineTo(slideViewWidth / 2, slideLength / 2 - (arrowSize * arrowAngle));
                arrowPath.lineTo(slideViewWidth / 2 + (arrowZoom * arrowSize), slideLength / 2 + (arrowSize * arrowAngle));
            }
            arrowPaint.setColor(arrowColor);
            canvas.drawPath(arrowPath, arrowPaint);
        }

        setAlpha(slideLength / maxSlideLength - 0.2f); // 最多0.8透明度
    }

    /**
     * 更新当前拉动距离并重绘
     *
     * @param slideLength 当前拉动距离
     */
    public void updateSlideLength(float slideLength) {
        // 避免无意义重绘
        if (this.slideLength == slideLength) return;

        this.slideLength = slideLength;
        invalidate();
    }

    /**
     * 设置箭头方向
     *
     * @param arrowOrientation ARROW_ORIENTATION_VERTICAL or ARROW_ORIENTATION_HORIZONTAL
     */
    public void setArrowOrientation(int arrowOrientation) {
        this.arrowOrientation = arrowOrientation;
    }

    /**
     * 设置滑动控件 最大拉动距离
     *
     * @param maxSlideLength px值
     */
    public void setMaxSlideLength(float maxSlideLength) {
        this.maxSlideLength = maxSlideLength;
    }

    /**
     * 设置箭头图标大小
     *
     * @param arrowSize px值
     */
    public void setArrowSize(float arrowSize) {
        this.arrowSize = arrowSize;
    }

    /**
     * 设置滑动控件 背景色
     *
     * @param slideViewBackgroundColor ColorInt
     */
    public void setSlideViewBackgroundColor(@ColorInt int slideViewBackgroundColor) {
        this.slideViewBackgroundColor = slideViewBackgroundColor;
    }

    /**
     * 设置箭头颜色
     *
     * @param arrowColor ColorInt
     */
    public void setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
    }

    /**
     * 设置滑动控件 宽
     *
     * @param slideViewWidth px值
     */
    public void setSlideViewWidth(float slideViewWidth) {
        this.slideViewWidth = slideViewWidth;
    }

    /**
     * 获取滑动控件 宽
     *
     * @return px值
     */
    public float getSlideViewWidth() {
        return slideViewWidth;
    }


}