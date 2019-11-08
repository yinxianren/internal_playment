package com.internal.playment.cross.tools;

import com.internal.playment.common.inner.CryptoUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

public class SicPayRSAUtils {
    public static String interface_url = "https://wg.sicpay.com/interfaceWeb/";

    public static String merchantId = "574034451110001";
    public static String terminalId = "20003971";
    public static String childMerchantId = "00003351";




    public static  String yhPubKeyPath;
    public static  String hzfPriKeyPath;

    public static PublicKey getPublicKey() throws Exception {
        String a = getYhPubKeyPath();
        return CryptoUtil.getRSAPublicKeyByFileSuffix(yhPubKeyPath, "pem", "RSA");
    }

    public static PrivateKey getPrivateKey() throws Exception {
        return CryptoUtil.getRSAPrivateKeyByFileSuffix(hzfPriKeyPath, "pem", null, "RSA");
    }

    public static String getYhPubKeyPath() {
        return yhPubKeyPath;
    }

    public static void setYhPubKeyPath(String yhPubKeyPath) {
        SicPayRSAUtils.yhPubKeyPath = yhPubKeyPath;
    }

    public static String getHzfPriKeyPath() {
        return hzfPriKeyPath;
    }

    public static void setHzfPriKeyPath(String hzfPriKeyPath) {
        SicPayRSAUtils.hzfPriKeyPath = hzfPriKeyPath;
    }
}

