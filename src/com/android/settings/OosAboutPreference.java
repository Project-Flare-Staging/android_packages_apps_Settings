package com.android.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceScreen;

import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.widget.LayoutPreference;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class OosAboutPreference extends BasePreferenceController implements View.OnTouchListener {

    private final Context context;
    private final List<AboutPhoneData> data = PhoneData.getData();
    private final long[] mHits = new long[3];

    public OosAboutPreference(Context context, String key) {
        super(context, key);
        this.context = context;
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        LayoutPreference mPreference = screen.findPreference("pref_layout_flare_about");
        if (mPreference != null) {
            mPreference.setSelectable(false);
            onBindItems(mPreference.findViewById(R.id.oos_about_root));
        }
    }

    private int findIndex(String name) {
        for (AboutPhoneData phoneData : data) {
            if (phoneData.getCodename().equals(name)) {
                return phoneData.getIndex();
            }
        }
        return 0;
    }

    public static void setInfo(String prop, TextView textview, String defaultVal) {
        String value = SystemProperties.get(prop, defaultVal);
        if (textview != null) {
            textview.setText(value);
        }
    }

    public void onBindItems(View root) {
        if (root == null) return;

        // --- Hardware & Storage ---
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        final double totalInt = (statFs.getBlockSizeLong() * statFs.getBlockCountLong() / Math.pow(1024, 3));
        int total = 0;
        if (totalInt > 0 && totalInt < 17) total = 16;
        else if (totalInt > 16 && totalInt < 33) total = 32;
        else if (totalInt > 32 && totalInt < 65) total = 64;
        else if (totalInt > 64 && totalInt < 129) total = 128;
        else if (totalInt > 128 && totalInt < 257) total = 256;
        else if (totalInt > 256 && totalInt < 513) total = 512;

        int index = findIndex(SystemProperties.get("ro.product.device"));

        // --- Views ---
        TextView rom = root.findViewById(R.id.rom_about);
        TextView device = root.findViewById(R.id.device_name);
        TextView deviceSec = root.findViewById(R.id.security_update);
        TextView kernel = root.findViewById(R.id.kernel_version);
        TextView maintainer = root.findViewById(R.id.flare_maintainer);
        TextView flareVer = root.findViewById(R.id.flareVer);
        TextView flareStatus = root.findViewById(R.id.flareStatus);
        
        View leftMini = root.findViewById(R.id.leftFinalDetail);
        View rightMini = root.findViewById(R.id.righFinalDetail);
        ImageView androidVersionImg = root.findViewById(R.id.android_version);

        // --- Branding & Props ---
        String buildVersion = SystemProperties.get("ro.lineage.build.version", "2.0");
        if (flareVer != null) {
            flareVer.setText("Project Flare " + buildVersion);
        }

        String releaseType = SystemProperties.get("ro.lineage.releasetype", "UNOFFICIAL");
        if (flareStatus != null) {
            flareStatus.setText("| " + releaseType.toUpperCase());
        }

        // --- Static Mapping ---
        int[] ids = {R.id.display_about, R.id.cpu_about, R.id.battery_about, R.id.soc_about, R.id.camera_about};
        String[] texts = {
                data.get(index).getDisplay(), data.get(index).getCpu(),
                data.get(index).getBattery(), data.get(index).getSoc(),
                data.get(index).getCamera()
        };

        for (int i = 0; i < ids.length; i++) {
            TextView tv = root.findViewById(ids[i]);
            if (tv != null) {
                if (ids[i] == R.id.camera_about && texts[i].length() > 16) {
                    tv.setTextSize(11);
                }
                tv.setText(!texts[i].equals("0") ? texts[i] : "Unknown");
            }
        }

        // --- System Info ---
        setInfo("ro.lineage.maintainer", maintainer, "Unknown");
        if (deviceSec != null) deviceSec.setText(DeviceInfoUtils.getSecurityPatch());
        if (kernel != null) kernel.setText(DeviceInfoUtils.getFormattedKernelVersion(context));
        if (device != null) device.setText(Build.MODEL);

        // --- RAM/ROM ---
        if (rom != null) {
            if (index != 0) {
                String ramSize = String.valueOf(Math.round(Float.parseFloat(getMem()) / Math.pow(1000, 2)));
                rom.setText(String.format(Locale.ENGLISH, "%sGB RAM + %dGB ROM", ramSize, total));
                if (rom.getText().length() >= 14) rom.setTextSize(11);
            } else {
                rom.setText("Hardware info unavailable");
            }
        }

        // --- Listeners ---
        if (leftMini != null) leftMini.setOnTouchListener(this);
        if (rightMini != null) rightMini.setOnTouchListener(this);

        if (androidVersionImg != null) {
            final Intent easterEggIntent = new Intent(Intent.ACTION_MAIN)
                    .setClassName("android", "com.android.internal.app.PlatLogoActivity");

            androidVersionImg.setOnClickListener(v -> {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    launchIntent(easterEggIntent);
                }
            });

            androidVersionImg.setOnLongClickListener(v -> {
                launchIntent(easterEggIntent);
                return true;
            });
        }
    }

    private void launchIntent(Intent intent) {
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("OosAboutPreference", "Easter Egg launch failed", e);
        }
    }

    private String getMem() {
        Map<String, String> memMap = getMemInfoMap();
        if (memMap.containsKey("MemTotal")) {
            return memMap.get("MemTotal").split(" ")[0];
        }
        return "0";
    }

    private Map<String, String> getMemInfoMap() {
        Map<String, String> map = new HashMap<>();
        try (Scanner s = new Scanner(new File("/proc/meminfo"))) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] vals = line.split(": ");
                if (vals.length > 1) {
                    map.put(vals[0].trim(), vals[1].trim());
                }
            }
        } catch (Exception e) {
            Log.e("getMemInfoMap", "Failed to read /proc/meminfo", e);
        }
        return map;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            v.animate().scaleX(.97f).scaleY(.97f).setDuration(200).start();
            return true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            v.animate().scaleX(1f).scaleY(1f).setDuration(400).start();
            if (action == MotionEvent.ACTION_UP) v.performClick();
            return true;
        }
        return false;
    }
}
