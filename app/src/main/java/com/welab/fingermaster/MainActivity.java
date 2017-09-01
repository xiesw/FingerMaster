package com.welab.fingermaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.welab.fingermaster.android.FingerActivity;
import com.welab.fingermaster.soter.SoterActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String tag = "xieshangwu";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.plan_android).setOnClickListener(this);
        findViewById(R.id.plan_soter).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.plan_android:
                startActivity(new Intent(this, FingerActivity.class));
                break;
            case R.id.plan_soter:
                startActivity(new Intent(this, SoterActivity.class));
                break;

            default:
                break;
        }
    }

    /*private static final String TAG = "MainActivity";
    private Button check;
    private FingerprintManagerCompat manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check = (Button) findViewById(R.id.btn_activity_main_finger);

        check.setOnClickListener(this);

        // 获取一个FingerPrintManagerCompat的实例
        manager = FingerprintManagerCompat.from(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_main_finger:
                *//**
     * 开始验证，什么时候停止由系统来确定，如果验证成功，那么系统会关系sensor，如果失败，则允许
     * 多次尝试，如果依旧失败，则会拒绝一段时间，然后关闭sensor，过一段时候之后再重新允许尝试
     *
     * 第四个参数为重点，需要传入一个FingerprintManagerCompat.AuthenticationCallback的子类
     * 并重写一些方法，不同的情况回调不同的函数
     *//*
                manager.authenticate(null, 0, null, new MyCallBack(), null);
                break;
        }
    }

    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.d(TAG, "onAuthenticationError: " + errString);
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            Log.d(TAG, "onAuthenticationFailed: " + "验证失败");
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.d(TAG, "onAuthenticationHelp: " + helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                      result) {
            Log.d(TAG, "onAuthenticationSucceeded: " + "验证成功");
        }
    }*/

/*
    private TextView textView;

    FingerprintManager manager;
    KeyguardManager mKeyManager;
    private final static int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0;
    private final static String TAG = "finger_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);

        //获取钥匙管理者
        mKeyManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);

        //找到按钮控件
        Button btn_finger = (Button) findViewById(R.id.btn_activity_main_finger);
        textView = (TextView) findViewById(R.id.textView);
        //为按钮设置点击事件
        btn_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFinger()) {
                    Toast.makeText(MainActivity.this, "请进行指纹识别", Toast.LENGTH_LONG).show();

                    startListening(null);
                } else {
                    textView.setText("您的手机暂不支持指纹识别");
                    return;
                }
            }
        });

    }


    *//**
     * 设备条件判断
     * - 设备是否支持指纹识别
     * - 设备是否处于安全保护中（有指纹识别的手机，在使用指纹识别的时候，还需要强制设置密码或者图案解锁，如果未设置的话是不许使用指纹识别的）
     * - 设备是否已经注册过指纹（如果用户未使用过这个指纹技术，那么只能提示用户到系统设置里面去设置）指纹识别API调用
     **//*
    public boolean isFinger() {
        //判断当前手机版本
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) !=
            PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
                return false;
            }

            Log(TAG, "有指纹权限");

            //判断硬件是否支持指纹识别
            if(!manager.isHardwareDetected()) {
                Toast.makeText(this, "没有指纹识别模块", Toast.LENGTH_SHORT).show();
                return false;
            }

            Log(TAG, "有指纹模块");

            //判断 是否开启锁屏密码

            if(!mKeyManager.isKeyguardSecure()) {
                Toast.makeText(this, "没有开启锁屏密码", Toast.LENGTH_SHORT).show();
                return false;
            }

            Log(TAG, "已开启锁屏密码");

            //判断是否有指纹录入
            if(!manager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "没有录入指纹", Toast.LENGTH_SHORT).show();
                return false;
            }

            Log(TAG, "已录入指纹");

            return true;
        } else {
            return false;
        }
    }


        *//**该对象提供了取消操作的能力。创建该对象也很简单，使用 new CancellationSignal() 就可以了。**//*
        CancellationSignal mCancellationSignal = new CancellationSignal();


        *//**回调方法**//*
        FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager
                .AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                // 验证出错回调 指纹传感器会关闭一段时间,在下次调用authenticate时,会出现禁用期(时间依厂商不同30,1分都有)

                Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();
                showAuthenticationScreen();
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                // 验证帮助回调
                Toast.makeText(MainActivity.this, helpString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result)
            {  //验证成功

                Toast.makeText(MainActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                // 验证失败  指纹验证失败后,指纹传感器不会立即关闭指纹验证,系统会提供5次重试的机会,即调用5次onAuthenticationFailed后,
                // 才会调用onAuthenticationError

                Toast.makeText(MainActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
            }
        };


        *//**如果支持一系列的条件，可以认证回调，参数是加密对象**//*

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        //判断是否添加指纹识别权限
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) !=
                PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return;
        }
        *//**参数分别是:防止第三方恶意攻击的包装类,CancellationSignal对象,flags,回调对象,handle**//*
        manager.authenticate(cryptoObject, mCancellationSignal, 0, mSelfCancelled, null);
    }


    *//**
     * 如果识别失败次数过多,则转入输入解锁密码界面，
     *//*
    private void showAuthenticationScreen() {

        Intent intent = mKeyManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
        if(intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
            Log.e(tag, "进入解锁密码界面");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "识别成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Log(String tag, String msg) {
        Log.d(tag, msg);
    }*/
}
