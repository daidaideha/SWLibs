/**
 * Cobub Razor
 * <p>
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2015, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @filesource
 * @since Version 0.1
 */
package com.zjwh.sw.customize.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * @author apple
 */
@SuppressLint("HardwareIds")
public class DeviceInfo {

    public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    private static final String tag = "DeviceInfo";
    private static Context context;
    private static TelephonyManager telephonyManager;
    private static BluetoothAdapter bluetoothAdapter;
    private static SensorManager sensorManager;

    public static void init(Context context) {
        DeviceInfo.context = context;
        try {
            telephonyManager = (TelephonyManager) (context.getSystemService(Context.TELEPHONY_SERVICE));
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getBluetoothAvailable() {
        return bluetoothAdapter != null;
    }

    public static String getOsVersion() {
        String result = Build.VERSION.RELEASE;
        if (result == null)
            return "";
        return result;
    }

    /**
     * get IMSI for GSM phone, return "" if it is unavailable.
     *
     * @return IMSI string
     */
    public static String getIMSI() {
        String result = "";
        try {
            if (!checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                return "";
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(context)
                        .setTitle("获取电话权限失败")
                        .setMessage("您已拒绝开启电话权限，如需开启请点击设置－权限－开启权限")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                context.startActivity(intent);
                            }
                        }).show();
                return "";
            }
            result = telephonyManager.getSubscriberId();
            if (result == null)
                return "";
            return result;

        } catch (Exception e) {
        }

        return result;
    }

    public static String getWifiMac() {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wi = wifiManager.getConnectionInfo();
                String result = wi.getMacAddress();
                if (result == null) result = "";
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return DEFAULT_MAC_ADDRESS;
    }

    public static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if (manufacturer == null)
                manufacturer = "";
            String model = Build.MODEL;
            if (model == null)
                model = "";

            if (model.startsWith(manufacturer)) {
                return capitalize(model).trim();
            } else {
                return (capitalize(manufacturer) + " " + model).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressLint({"MissingPermission"})
    public static String getDeviceIMEI() {
        String result = "";
        try {
            if (!checkPermissions(context, Manifest.permission.READ_PHONE_STATE)) {
                return "";
            }
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                new AlertDialog.Builder(context)
                        .setTitle("获取电话权限失败")
                        .setMessage("您已拒绝开启电话权限，如需开启请点击设置－权限－开启权限")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                context.startActivity(intent);
                            }
                        }).show();
                return "";
            }
            result = telephonyManager.getDeviceId();
            if (result == null)
                result = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Capitalize the first letter
     *
     * @param s model,manufacturer
     * @return Capitalize the first letter
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }

    }

    /**
     * checkPermissions
     *
     * @param context
     * @param permission
     * @return true or false
     */
    private static boolean checkPermissions(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        return pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }
}
