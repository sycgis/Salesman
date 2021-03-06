package com.studio.jframework.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * need to be tested
 * <p/>
 * NetworkUtils is used to operate network relative stuff
 * Such as GPS, Wifi, Mobile network, Bluetooth
 *
 * @author Jason
 */
public class NetworkUtils {

    public static enum Provider {
        NON, CMCC, CUCC, CTCC
    }

    public static enum NetType {
        NON, WIFI, GPRS, WCDMA, LTE
    }

    /**
     * Check if the network is available
     *
     * @param context Context
     * @return True if the network is available, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            return isWifiNetworkEnable(context) || isMobileNetworkEnable(context);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if the mobile network is available
     *
     * @param context Context
     * @return True if network connectivity exists or is in the process of being established, false otherwise
     * @throws Exception
     */
    public static boolean isMobileNetworkEnable(Context context) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable;
        isMobileDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return isMobileDataEnable;
    }

    /**
     * Check if the wifi network is available
     *
     * @param context Context
     * @return True if network connectivity exists or is in the process of being established, false otherwise
     * @throws Exception
     */
    public static boolean isWifiNetworkEnable(Context context) throws Exception {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
    }

    /**
     * Check if the GPS is enable
     *
     * @param context Context
     * @return True if the GPS is enable, false otherwise
     */
    public static boolean isGpsEnable(Context context) {
        LocationManager locationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Get the name of the provider
     *
     * @param context Context
     * @return The provider, will return NON if no Sim card detected
     */
    public static Provider getProvider(Context context) {
        String IMSI;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        IMSI = telephonyManager.getSubscriberId();
        // The first 3 IMSI nums 460 stand for nation code China
        // 00,02 stands for China Mobile, 01 stands for China Unicom, 03 stands for China Telecom
        if (IMSI == null)
            return Provider.NON;
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            return Provider.CMCC;
        } else if (IMSI.startsWith("46001")) {
            return Provider.CUCC;
        } else if (IMSI.startsWith("46003")) {
            return Provider.CTCC;
        } else {
            return Provider.NON;
        }
    }

    /**
     * Get the network type, will return wifi, 2G, 3G, LTE or null if the system can't judge the type
     *
     * @param context Context
     * @return The network type now you are using
     */
    public static NetType getNetworkType(Context context) {
        NetType type = null;
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null) {
            type = NetType.NON;
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = NetType.WIFI;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = NetType.GPRS;
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = NetType.WCDMA;
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
                type = NetType.LTE;
            }
        }
        return type;
    }

    /**
     * Get the network type, will return wifi, 2G, 3G, LTE or null if the system can't judge the type
     *
     * @param context Context
     * @return The network type now you are using
     */
    public static String getNetworkTypeString(Context context) {
        String type = "none";
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null) {
            type = "none";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "gprs";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
                type = "4g";
            }
        }
        return type;
    }
}
