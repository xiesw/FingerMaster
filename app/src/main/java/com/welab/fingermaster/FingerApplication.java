package com.welab.fingermaster;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.soter.wrapper.SoterWrapperApi;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessCallback;
import com.tencent.soter.wrapper.wrap_callback.SoterProcessNoExtResult;
import com.tencent.soter.wrapper.wrap_task.InitializeParam;
import com.welab.fingermaster.soter.Constants;


/**
 * Created by pain.xie on 2017/8/29
 */

public class FingerApplication extends Application {

    public static final String TAG = "xieshangwu";

    @Override
    public void onCreate() {
        super.onCreate();
        initSoter();
    }

    private void initSoter() {
        InitializeParam param = new InitializeParam.InitializeParamBuilder().setScenes(Constants.SCENE_LOGIN,Constants.SCENE_CHECK)
                .build();
        SoterWrapperApi.init(this, new SoterProcessCallback<SoterProcessNoExtResult>() {
            @Override
            public void onResult(@NonNull SoterProcessNoExtResult result) {
                Log.e(TAG, "onResult: application init" + result.toString() );
            }
        }, param);
    }

}