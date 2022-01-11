package com.android.settings;

import android.os.SystemProperties;
import java.util.ArrayList;
import java.util.List;

public final class PhoneData {

    private static final String PROP_CODENAME = "ro.lineage.device.codename";
    private static final String PROP_CPU      = "ro.lineage.device.cpu";
    private static final String PROP_SOC      = "ro.lineage.device.soc";
    private static final String PROP_BATTERY  = "ro.lineage.device.battery";
    private static final String PROP_DISPLAY  = "ro.lineage.device.display";
    private static final String PROP_CAMERA   = "ro.lineage.device.camera";

    public static List<AboutPhoneData> getData() {
        List<AboutPhoneData> data = new ArrayList<>();
        data.add(new AboutPhoneData(0)); // header/default entry

        String codename = SystemProperties.get(PROP_CODENAME, "");
        String cpu      = SystemProperties.get(PROP_CPU,      "");
        String soc      = SystemProperties.get(PROP_SOC,      "");
        String battery  = SystemProperties.get(PROP_BATTERY,  "");
        String display  = SystemProperties.get(PROP_DISPLAY,  "");
        String camera   = SystemProperties.get(PROP_CAMERA,   "");

        // Only add if at least codename is set
        if (!codename.isEmpty()) {
            data.add(new AboutPhoneData(1,
                codename, cpu, soc, battery, display, camera
            ));
        }

        return data;
    }
}
