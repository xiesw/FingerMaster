package com.welab.fingermaster.soter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.welab.fingermaster.R;


public class GestureActivity extends AppCompatActivity implements View.OnClickListener {

    protected AppCompatButton mGestureOkBtn;
    protected AppCompatButton mGestureCancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_gesture);
        initView();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if(view.getId() == R.id.gesture_ok_btn) {
            intent.putExtra("result", true);
            setResult(0, intent);
            finish();
        } else if(view.getId() == R.id.gesture_cancel_btn) {
            intent.putExtra("result", false);
            setResult(0, intent);
            finish();
        }
    }

    private void initView() {
        mGestureOkBtn = (AppCompatButton) findViewById(R.id.gesture_ok_btn);
        mGestureOkBtn.setOnClickListener(GestureActivity.this);
        mGestureCancelBtn = (AppCompatButton) findViewById(R.id.gesture_cancel_btn);
        mGestureCancelBtn.setOnClickListener(GestureActivity.this);
    }
}
