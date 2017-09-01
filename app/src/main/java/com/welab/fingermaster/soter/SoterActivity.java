package com.welab.fingermaster.soter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.tencent.soter.wrapper.SoterWrapperApi;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessAuthenticationResult;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessKeyPreparationResult;
import com.tencent.soter.wrapper.wrap_fingerprint.SoterFingerprintCanceller;
import com.tencent.soter.wrapper.wrap_fingerprint.SoterFingerprintStateCallback;
import com.tencent.soter.wrapper.wrap_task.AuthenticationParam;
import com.welab.fingermaster.R;
import com.welab.fingermaster.utils.SpUtils;

public class SoterActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "xieshangwu";
    private static final int REQUEST_CODE_GESTURE = 4;
    private SoterFingerprintCanceller mCanceller = null;
    private AppCompatButton mSoterSettingBtn;
    private View mCheckBtn;
    private AppCompatTextView mResultTv;
    private TextView mNoteTv;
    private AuthenticationParam mParam;
    private Animation mFlashAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soter);
        initView();
        checkSupportSoter();
    }

    /**
     * 1. 检查是否支持soter
     */
    private void checkSupportSoter() {
        if(!true) {
            Log.e(TAG, "-----------------   该设备不支持soter -------------------");
            mNoteTv.setText("该设备不支持soter 指纹识别");
            mCheckBtn.setVisibility(View.GONE);
            mSoterSettingBtn.setVisibility(View.GONE);
        } else {
            mCheckBtn.setVisibility(View.VISIBLE);
            mSoterSettingBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mSoterSettingBtn = (AppCompatButton) findViewById(R.id.soter_setting_btn);
        mSoterSettingBtn.setOnClickListener(this);
        mCheckBtn = findViewById(R.id.check_btn);
        mCheckBtn.setOnClickListener(this);
        mResultTv = (AppCompatTextView) findViewById(R.id.result_tv);
        mNoteTv = (TextView) findViewById(R.id.note_tv);
        mFlashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_flash);
        updateUi();
    }

    private void updateUi() {
        boolean enabelFingerPritn = (boolean) SpUtils.get(this, Constants.ENABLE_FINGER_PRINT,
                false);
        if(enabelFingerPritn) {
            mSoterSettingBtn.setText("关闭指纹识别");
            mCheckBtn.setVisibility(View.VISIBLE);
        } else {
            mSoterSettingBtn.setText("开启指纹识别");
            mCheckBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化参数
     */
    private void initSoterParam() {
        if(mCanceller == null) {
            mCanceller = new SoterFingerprintCanceller();
        }
        if(mParam == null) {
            mParam = new AuthenticationParam.AuthenticationParamBuilder().setScene(Constants
                    .SCENE_CHECK)
                    .setContext(SoterActivity.this)
                    .setFingerprintCanceller(mCanceller)
                    .setPrefilledChallenge("test challenge")
                    .setSoterFingerprintStateCallback(new SoterFingerprintStateCallback() {
                        @Override
                        public void onStartAuthentication() {
                            Log.e(TAG, "onStartAuthentication: ", null);
                        }

                        @Override
                        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                            Log.e(TAG, "onAuthenticationHelp: ", null);
                            setResultMsg("再试一次", true);
                        }

                        @Override
                        public void onAuthenticationSucceed() {
                            Log.e(TAG, "onAuthenticationSucceed: ");
                            mResultTv.setText("验证成功");
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            Log.e(TAG, "onAuthenticationFailed: ");
                            setResultMsg("再试一次", true);
                        }

                        @Override
                        public void onAuthenticationCancelled() {
                            Log.e(TAG, "onAuthenticationCancelled: ");
                        }

                        @Override
                        public void onAuthenticationError(int errorCode, CharSequence errorString) {
                            Log.e(TAG, "onAuthenticationError: ");
                            setResultMsg("验证次数过多  请稍后验证", true);
                        }
                    })
                    .build();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.soter_setting_btn) {
            clickSettingBtn();
        } else if(v.getId() == R.id.check_btn) {
            clickCheckBtn();
        }
    }

    /**
     * 指纹开关设置
     */
    private void clickSettingBtn() {
        boolean enabelFingerPritn = (boolean) SpUtils.get(this, Constants.ENABLE_FINGER_PRINT,
                false);
        if(enabelFingerPritn) {
            // 已开启指纹识别  -->  去关闭
            SoterWrapperApi.removeAuthKeyByScene(Constants.SCENE_CHECK);
            SpUtils.put(this, Constants.ENABLE_FINGER_PRINT, false);
            updateUi();
        } else {
            // 未开启指纹识别  -->  去开启
            startActivityForResult(new Intent(this, GestureActivity.class), REQUEST_CODE_GESTURE);
        }
    }

    /**
     * 开始指纹验证
     */
    private void clickCheckBtn() {
        setResultMsg("开始验证", true);
        mNoteTv.setVisibility(View.VISIBLE);
        mCheckBtn.setEnabled(false);

        initSoterParam();
        SoterWrapperApi.requestAuthorizeAndSign(new SoterProcessCallback<SoterProcessAuthenticationResult>() {
            @Override
            public void onResult(@NonNull SoterProcessAuthenticationResult result) {
                Log.e(TAG, " Auth :onResult: -----" + result.toString());
                mNoteTv.setVisibility(View.GONE);
                mCheckBtn.setEnabled(true);
            }
        }, mParam);
    }


    /**
     * 生成AuthKey
     */
    private void doPrepareAuthKey() {
        Log.e(TAG, "----------start---------");
        SoterWrapperApi.prepareAuthKey(new SoterProcessCallback<SoterProcessKeyPreparationResult>
                () {
            @Override
            public void onResult(@NonNull SoterProcessKeyPreparationResult result) {
                Log.e(TAG, "prepareAuthKey: " + result.toString());
                confirmFingerPrintFirstTime();
            }
        }, false, true, Constants.SCENE_CHECK, null, null);
        Log.e(TAG, "----------end---------");

    }

    /**
     * 首次确认指纹
     */
    private void confirmFingerPrintFirstTime() {
        Log.e(TAG, "confirmFingerPrintFirstTime: -------  首次验证指纹 --------");
        mNoteTv.setVisibility(View.VISIBLE);

        AuthenticationParam firstConfirm = new AuthenticationParam.AuthenticationParamBuilder()
                .setScene(Constants.SCENE_CHECK)
                .setContext(SoterActivity.this)
                .setFingerprintCanceller(mCanceller)
                .setPrefilledChallenge("test challenge")
                .setSoterFingerprintStateCallback(new SoterFingerprintStateCallback() {
                    @Override
                    public void onStartAuthentication() {
                        Log.e(TAG, "onStartAuthentication: ", null);
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        Log.e(TAG, "onAuthenticationHelp: ", null);
                        setResultMsg("再试一次", true);
                    }

                    @Override
                    public void onAuthenticationSucceed() {
                        SpUtils.put(SoterActivity.this, Constants.ENABLE_FINGER_PRINT, true);
                        Log.e(TAG, "onAuthenticationSucceed: ");
                        mResultTv.setText("验证成功");
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Log.e(TAG, "onAuthenticationFailed: ");
                        setResultMsg("再试一次", true);
                    }

                    @Override
                    public void onAuthenticationCancelled() {
                        SoterWrapperApi.removeAuthKeyByScene(Constants.SCENE_CHECK);
                        SpUtils.put(SoterActivity.this, Constants.ENABLE_FINGER_PRINT, false);
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errorString) {
                        SoterWrapperApi.removeAuthKeyByScene(Constants.SCENE_CHECK);
                        SpUtils.put(SoterActivity.this, Constants.ENABLE_FINGER_PRINT, false);
                        Log.e(TAG, "onAuthenticationError: ");
                        setResultMsg("验证次数过多  请稍后验证", true);
                    }
                })
                .build();

        SoterWrapperApi.requestAuthorizeAndSign(new SoterProcessCallback<SoterProcessAuthenticationResult>() {
            @Override
            public void onResult(@NonNull SoterProcessAuthenticationResult result) {
                Log.e(TAG, " first Auth :onResult: -----" + result.toString());
                updateUi();
                mNoteTv.setVisibility(View.GONE);
            }
        }, firstConfirm);

    }

    /**
     * 更新提示消息
     */
    private void setResultMsg(String msg, boolean isFlash) {
        mResultTv.setText(msg);
        if(isFlash) {
            mResultTv.startAnimation(mFlashAnimation);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_CODE_GESTURE:
                boolean result = data.getBooleanExtra("result", false);
                if(result) {
                    // 3. 手势正确, 生成Authkey,并验证指纹
                    doPrepareAuthKey();
                }
                Log.e(TAG, "onActivityResult: " + result);
                break;
            default:
                break;
        }
    }

}
