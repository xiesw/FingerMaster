package com.welab.fingermaster.soter;

/**
 * Created by pain.xie on 2017/8/30
 */

public class Constants {
    public static int SCENE_LOGIN = 0;
    public static int SCENE_CHECK = 1;

    // 是否指纹识别可用/ps:不可用条件:1. 用户未开启或者关闭,2, 用户新添加了指纹
    public static boolean canUseFingerPrint = false;
    // 是否支持 腾讯 soter/ ps 先默认true
    public static boolean supportSoter = true;

    // 用户是否允许指纹解锁
    public static final String ENABLE_FINGER_PRINT = "enableFingerPrint";
}
