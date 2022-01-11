package com.android.settings;

public class AboutPhoneData {
    int index;
    String codename = "Unknown";
    String cpu;
    String soc;
    String battery;
    String display;
    String camera;

    public AboutPhoneData(int index) {
        this.index = index;
    }

    public AboutPhoneData(int index, String codename, String cpu, String soc, String battery, String display, String camera) {
        this.index = index;
        this.codename = codename;
        this.cpu = cpu;
        this.soc = soc;
        this.battery = battery;
        this.display = display;
        this.camera = camera;
    }

    public int getIndex() {
        return index;
    }

    public String getCodename() {
        return getIndex() == 0 ? "Unknown" : codename;
    }

    public String getCpu() {
        return getIndex() == 0 ? "Unknown" : cpu;
    }

    public String getSoc() {
        return getIndex() == 0 ? "Unknown" : soc;
    }

    public String getBattery() {
        return getIndex() == 0 ? "Unknown" : battery + " mAh";
    }

    public String getDisplay() {
        return getIndex() == 0 ? "Unknown" : display;
    }

    public String getCamera() {
        return getIndex() == 0 ? "Unknown" : camera;
    }

}
