package com.rhino.edgeslide.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.anim_translate_right_out);
    }

    public void onViewClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_normal) {
            startActivity(new Intent(this, NormalActivity.class));
            overridePendingTransition(R.anim.anim_translate_right_in, 0);
//            overridePendingTransition(R.anim.anim_translate_left_in, 0);
//            overridePendingTransition(R.anim.anim_translate_top_in, 0);
//            overridePendingTransition(R.anim.anim_translate_bottom_in, 0);
        }
    }
}
