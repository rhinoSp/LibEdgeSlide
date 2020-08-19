package com.rhino.edgeslide.callback;

/**
 * @author rhino
 * @since Create on 2019/12/17.
 **/
public interface SlideCallBack {
    /**
     * 滑动来源： <br>
     * EDGE_LEFT    左侧侧滑 <br>
     * EDGE_RIGHT   右侧侧滑 <br>
     * EDGE_TOP     顶部侧滑 <br>
     * EDGE_BOTTOM  底部侧滑 <br>
     */
    void onSlide(int edgeFrom);
}