package com.android.settings.deviceinfo;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.android.settings.core.BasePreferenceController;

public class RadioInfoPreferenceController extends BasePreferenceController {

    public RadioInfoPreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        TelephonyManager tm = mContext.getSystemService(TelephonyManager.class);
        return (tm != null && tm.isVoiceCapable()) ? AVAILABLE : UNSUPPORTED_ON_DEVICE;
    }
}
