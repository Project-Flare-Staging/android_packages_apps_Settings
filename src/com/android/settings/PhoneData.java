package com.android.settings;

import android.os.SystemProperties;
import java.util.ArrayList;
import java.util.List;

public final class PhoneData {

    // --- Property Keys ---
    private static final String PROP_CODENAME = "ro.lineage.device.codename";
    private static final String PROP_CPU      = "ro.lineage.device.cpu";
    private static final String PROP_SOC      = "ro.lineage.device.soc";
    private static final String PROP_BATTERY  = "ro.lineage.device.battery";
    private static final String PROP_DISPLAY  = "ro.lineage.device.display";
    private static final String PROP_CAMERA   = "ro.lineage.device.camera";

    public static List<AboutPhoneData> getData() {
        List<AboutPhoneData> data = new ArrayList<>();

        // Always add index 0 as the fallback/header
        data.add(new AboutPhoneData(0));

        // Fetch values from build.prop
        String codename = SystemProperties.get(PROP_CODENAME, "unknown");
        String cpu      = SystemProperties.get(PROP_CPU,      "Unknown CPU");
        String soc      = SystemProperties.get(PROP_SOC,      "Unknown SoC");
        String battery  = SystemProperties.get(PROP_BATTERY,  "Unknown Battery");
        String display  = SystemProperties.get(PROP_DISPLAY,  "Unknown Display");
        String camera   = SystemProperties.get(PROP_CAMERA,   "Unknown Camera");

        // Add the dynamic entry at index 1
        data.add(new AboutPhoneData(1, codename, cpu, soc, battery, display, camera));

        return data;
    }
}
