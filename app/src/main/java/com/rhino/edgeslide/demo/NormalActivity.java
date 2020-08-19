package com.rhino.edgeslide.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.rhino.edgeslide.EdgeSlideManager;
import com.rhino.edgeslide.callback.SlideCallBack;


public class NormalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_normal);

        EdgeSlideManager.with(this)
                .callBack(new SlideCallBack() {
                    @Override
                    public void onSlide(int edgeFrom) {
                        Log.d("xxxDDD", "edgeFrom = " + edgeFrom);
                        if (edgeFrom == EdgeSlideManager.EDGE_LEFT) {
                            finish();
                        }
                    }
                })
                .setEdgeMode(EdgeSlideManager.EDGE_LEFT |  EdgeSlideManager.EDGE_RIGHT |  EdgeSlideManager.EDGE_TOP |  EdgeSlideManager.EDGE_BOTTOM )
                .setSlideViewBackgroundColor(0x330000FF)
                .setArrowColor(0x33FFFFFF)
                .setArrowSize(10)
                .setSlideViewWidth(160)
                .setSlideActiveLength(50)
                .setMaxSlideLength(50)
                .setup();
        EdgeSlideManager.with(findViewById(R.id.container))
                .callBack(new SlideCallBack() {
                    @Override
                    public void onSlide(int edgeFrom) {
                        Log.d("xxxDDD", "edgeFrom = " + edgeFrom);
                        if (edgeFrom == EdgeSlideManager.EDGE_LEFT) {
                        }
                    }
                })
                .setEdgeMode(EdgeSlideManager.EDGE_LEFT |  EdgeSlideManager.EDGE_RIGHT |  EdgeSlideManager.EDGE_TOP |  EdgeSlideManager.EDGE_BOTTOM )
                .setSlideViewBackgroundColor(0x11FFFFFF)
                .setArrowSize(0)
                .setSlideViewWidth(500)
                .setSlideActiveLength(50)
                .setMaxSlideLength(50)
                .setup();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.anim_translate_right_out);
//        overridePendingTransition(0, R.anim.anim_translate_left_out);
//        overridePendingTransition(0, R.anim.anim_translate_top_out);
//        overridePendingTransition(0, R.anim.anim_translate_bottom_out);
    }
}
