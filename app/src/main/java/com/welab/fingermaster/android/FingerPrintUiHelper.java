package com.welab.fingermaster.android;

import android.app.Activity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

/**
 * Created by pain.xie on 2017/8/29
 */

class FingerPrintUiHelper {

    private CancellationSignal signal;
    private FingerprintManagerCompat fingerprintManager;

    public FingerPrintUiHelper(Activity activity) {
        signal = new CancellationSignal();
        fingerprintManager = FingerprintManagerCompat.from(activity);
    }

    public void startFingerPrintListen(FingerprintManagerCompat.AuthenticationCallback callback) {
        fingerprintManager.authenticate(null, 0, signal, callback, null);
    }

    public void stopsFingerPrintListen() {
        if(signal != null) {
            signal.cancel();
            signal = null;
        }
    }

}
