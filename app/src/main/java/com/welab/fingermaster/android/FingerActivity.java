package com.welab.fingermaster.android;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.welab.fingermaster.R;
import com.welab.fingermaster.utils.SingleToast;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;


public class FingerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "xieshangwu";


    private AppCompatButton mSoterSettingBtn;
    private View mCheckBtn;
    private AppCompatTextView mResultTv;
    private TextView mNoteTv;
    private Animation mFlashAnimation;

    private FingerPrintUiHelper fingerPrintUiHelper;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        initView();
        checkFingerSupport();
    }

    /**
     * 是否支持指纹
     */
    private boolean checkFingerSupport() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0以下不支持
            return false;
        }

        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(this);
        if(!fingerprintManager.isHardwareDetected()) {
            //是否支持指纹识别
            return false;
        } else if(!fingerprintManager.hasEnrolledFingerprints()) {
            //是否已注册指纹
            return false;
        }
        return true;
    }

    private void initView() {
        mSoterSettingBtn = (AppCompatButton) findViewById(R.id.soter_setting_btn);
        mSoterSettingBtn.setOnClickListener(this);
        mCheckBtn = findViewById(R.id.check_btn);
        mCheckBtn.setOnClickListener(this);
        mResultTv = (AppCompatTextView) findViewById(R.id.result_tv);
        mNoteTv = (TextView) findViewById(R.id.note_tv);
        mFlashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_flash);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.soter_setting_btn) {
            clickSettingBtn();
        } else if(view.getId() == R.id.check_btn) {
            clickCheckBtn();
        }
    }

    /**
     * 开启/关闭指纹解锁
     */
    private void clickCheckBtn() {
        fingerPrintUiHelper.startFingerPrintListen(new FingerprintManagerCompat
                .AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                          result) {
                SingleToast.showToast(FingerActivity.this, "指纹识别成功");
            }

            @Override
            public void onAuthenticationFailed() {
                SingleToast.showToast(FingerActivity.this, "指纹识别失败");
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                SingleToast.showToast(FingerActivity.this, helpString.toString());
                Log.e("xeishangwu", helpString.toString());
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                SingleToast.showToast(FingerActivity.this, "errMsgId=" + errMsgId + "|" +
                        errString);
                if(errMsgId == 7) {
                    SingleToast.showToast(FingerActivity.this, "你的错误次数过多, 请用密码登陆");
                    Log.e("xieshangwu", "你的错误次数过多, 请用密码登陆");
                }
            }
        });
    }

    /**
     * 指纹识别
     */
    private void clickSettingBtn() {
        fingerPrintUiHelper = new FingerPrintUiHelper(this);
        SingleToast.showToast(FingerActivity.this, "已开启");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(fingerPrintUiHelper != null) {
            fingerPrintUiHelper.stopsFingerPrintListen();
        }
    }
}
