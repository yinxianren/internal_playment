package com.internal.playment.cross.tools;


import com.internal.playment.common.inner.CryptoUtil;
import org.apache.commons.codec.binary.Base64;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;


public class SicEncype {

    public Map parseMap(String response){
        Map<String,String> map=new HashMap<String,String>();

        String [] resp=response.split("&");
        for(int i=0; i<resp.length; i++){
            String key=resp[i].split("=")[0];
            String value=resp[i].split("=")[1];
            map.put(key, value);
        }
        return map;
    }
    public String respDecryption(String response){
        Map respMap=this.parseMap(response);
        String xmlData = "";
        try {
//			final PrivateKey hzfPriKey = CryptoUtil.getRSAPrivateKeyByFileSuffix("F:/密钥/test/pkcs8_rsa_private_key_2048.pem", "pem", null, "RSA");
//			final PublicKey yhPubKey = CryptoUtil.getRSAPublicKeyByFileSuffix("F:/密钥/生成证书/rsa_public_key_2048.pem", "pem", "RSA");
//			final PublicKey yhPubKey = CryptoUtil.getRSAPublicKeyByFileSuffix("F:/key/test/test_rsa_public_key_2048.pem", "pem", "RSA");
//			final PrivateKey hzfPriKey = CryptoUtil.getRSAPrivateKeyByFileSuffix("F:/key/test/test_pkcs8_rsa_private_key_2048.pem", "pem", null, "RSA");

            PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
            PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
            byte[] encryptedBytes = Base64.decodeBase64(respMap.get("encryptKey").toString().getBytes("UTF-8"));
//			String encrtptKey = new String(encryptedBytes, "UTF-8");
            byte[] keyBytes = CryptoUtil.RSADecrypt(encryptedBytes, hzfPriKey, 2048, 11, "RSA/ECB/PKCS1Padding");
            byte[] plainBytes = Base64.decodeBase64(respMap.get("encryptData").toString().getBytes("UTF-8"));
            byte[] xmlBytes = CryptoUtil.AESDecrypt(plainBytes, keyBytes, "AES", "AES/ECB/PKCS5Padding", null);
            xmlData = new String(xmlBytes,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlData;
    }

}

