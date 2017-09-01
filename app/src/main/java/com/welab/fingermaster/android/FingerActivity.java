package com.welab.fingermaster.android;

import android.app.KeyguardManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback;
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

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


@SuppressWarnings("NewApi")
public class FingerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "xieshangwu";

    static final String DEFAULT_KEY_NAME = "default_key";

    private AppCompatButton mSoterSettingBtn;
    private View mCheckBtn;
    private AppCompatTextView mResultTv;
    private TextView mNoteTv;
    private Animation mFlashAnimation;

    private FingerPrintUiHelper fingerPrintUiHelper;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private Cipher mDefaultCipher;
    private CancellationSignal mCancellationSignal;
    private FingerprintManagerCompat mFingerprintManager;

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

        if(!mFingerprintManager.isHardwareDetected()) {
            //是否支持指纹识别
            return false;
        } else if(!mFingerprintManager.hasEnrolledFingerprints()) {
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
     * 指纹识别
     */
    private void clickCheckBtn() {
        if(initCipher(mDefaultCipher, DEFAULT_KEY_NAME)) {
            mCancellationSignal = new CancellationSignal();
            mFingerprintManager = FingerprintManagerCompat.from(this);
            mFingerprintManager.authenticate(new FingerprintManagerCompat.CryptoObject
                    (mDefaultCipher), 0, mCancellationSignal, new
                    AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat
                                                              .AuthenticationResult result) {
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
            }, null);
        }
    }

    /**
     * 旧指纹识别方案
     */
    private void old() {
        /*fingerPrintUiHelper = new FingerPrintUiHelper(this);

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
        });*/
    }

    /**
     * 开启/关闭指纹解锁
     */
    private void clickSettingBtn() {
        // 生成KeyStore 存放key
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch(KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }

        // 生成 Key 生产者
        try {
            mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
        } catch(NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }

        // 加密规则
        try {
            mDefaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch(NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }

        KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
        FingerprintManagerCompat fingerprintManager = getSystemService(FingerprintManagerCompat
                .class);

        createKey(DEFAULT_KEY_NAME, true);
        Log.e(TAG, "----init success");
    }

    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        try {
            mKeyStore.load(null);
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch(NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                CertificateException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch(KeyPermanentlyInvalidatedException e) {
            return false;
        } catch(KeyStoreException | CertificateException | UnrecoverableKeyException |
                IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(fingerPrintUiHelper != null) {
            fingerPrintUiHelper.stopsFingerPrintListen();
        }
    }
}
