package com.internal.playment.cross.tools;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * <p>RSA签名,加解密处理核心文件，注意：密钥长度1024</p>
 * @author leelun
 * @version $Id: RSA.java, v 0.1 2013-11-15 下午2:33:53 lilun Exp $
 */
public class YaColPayRSAUtil {

    /**
     * 签名算法
     */
    public static final String  SIGNATURE_ALGORITHM = "SHA1withRSA";
    /**
     * 加密算法RSA
     */
    public static final String  KEY_ALGORITHM       = "RSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int    MAX_ENCRYPT_BLOCK   = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int    MAX_DECRYPT_BLOCK   = 128;

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY          = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY         = "RSAPrivateKey";

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;

    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> genKeyPair = genKeyPair();

        String base64publicKey = getPublicKey(genKeyPair);
        System.out.println("公钥 \n" + base64publicKey);
        String base64privateKey = getPrivateKey(genKeyPair);
        System.out.println("私钥\n" + base64privateKey);

        String passwd = "cat123113";
        String charsetName = "utf-8";

        String encryptByPublicKey = Base64.encodeBase64String((encryptByPublicKey(
                passwd.getBytes(charsetName), base64publicKey)));
        System.out.println("加密\n" + encryptByPublicKey);

        byte[] decryptByPrivateKey = decryptByPrivateKey(Base64.decodeBase64("Hdh0WNtZWyRZuDilPkDZv0HmREEgdMjfEkGi1Kvm68J45Ax592N6LnaieA9BQJ5pMS1ayIu2oJhvHhpt/iAWOU6k/mdnIjjdGFmZyYJTV9WETPOnu/xuSwcUOemxQ95QKKbv5MDQdko7/WJRTnC9tyMXmwJspj10XTn9BvFsxSM="),
                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMFTKy0DE/nI9uzuQrTKh3FFk4h6pA6JJzCAYyLSf0NzTi+5i47RMbcxMeItNvW3OWj8+LAmRMmxjX4M118VoB3em6nTU0A0kyfwtqX8dE+4/C35/Y9fiH8orPSO/CF6oiRoCvSRP3C4wjs4fJLIPcv+0BBfZn+WIw0NsDW2OQ1lAgMBAAECgYA/8CzIb0stAp1AETD4sD8JZHR93+ngcNYIQX4IJ0w1163VNO2GJ4PkzZ1s631Q2O9g3MG3KID5oAJm3QJiDTnt88bnG6pP76X0Wn+E2wiFj3HJwMuT+b7i2eivjLSTpv597l9saIM6x3mbvXUF+Xhi2PUMY5sYRpFDEua4ue0LoQJBAOguHAMavFVTr0WkJ+O1Zkbr+HQDFQa5jn1eFgvsv5gz/BWv8vtlhyXDhULkxHQIhE5fkR8ItXYCTX0giwWD770CQQDVKJR60ssFYu1vy41us1qYBO1pSTYxzHz8AVZgJU+VL4mM1q4baZmDEcJWGig6LDqra8rSP4gVUI0PQi4jJTrJAkAY/nW3g9ZIXTTeC1jb83gqJFbfrkFCMxF6v3kiGX9alCYL85/1ni1ZTF35IIVhdFVB1pnZvGdEZ+UNlkZA9r4FAkBNCQZoQSg4QSF4ZsMtf8o86IL4qwnYA4Qj+0PBKZrSWsTGTovLwmVFdjSas4dYRsXJUAKT63v94AeqvQs5jmnpAkEAn3UpqlKwI5qatVd3vG7LpvxZGf667sZVYep4SoF1JNyA6L6VWZtot3GNpQvHbUWpvdlv27uIbQjgr5Y+5i+8OQ==");
        String string = new String(decryptByPrivateKey, "utf-8");
        System.out.println("解密后\n" + string);


        String text="_input_charset=UTF-8&deposit_amount=361.00&deposit_status=SUCCESS&inner_trade_no=121462435441575330216&notify_id=201605050006405601&notify_time=20160505160420&notify_type=deposit_status_sync&outer_trade_no=052016050516061149749497&version=1.0";
        String sign="S3uVxluuqjjp8hxKuQlaqWe4YFjRKm8t/G83uc7zvhSqgzFH9f65IIZDV7c3PGnP0w9d0UoqlGq4KVGimf5Q8eGHPhqtqs45zWBPdZ7gyBlc0H2bmqPJrZmhSXcw/YlZMiC7lmtFOjXv42jxSsMM/bYD9lVFqK/FmCzCrtbBdXg=";
        String charset="UTF-8";
        base64publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB";
        if(verify(text, sign, base64publicKey,charset ))
        {
            System.out.println("true");
        }
        else
        {
            System.out.println("false");
        }

    }

    /**
     * 签名字符串
     *
     * @param text
     *            需要签名的字符串
     * @param privateKey 私钥(BASE64编码)
     *
     * @param charset
     *            编码格式
     * @return 签名结果(BASE64编码)
     */
    public static String sign(String text, String privateKey, String charset) throws Exception {

        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(getContentBytes(text, charset));
        byte[] result = signature.sign();
        return Base64.encodeBase64String(result);

    }

    public static String sign(String text, PrivateKey privateKey, String charset)
            throws SignatureException,
            InvalidKeyException {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(getContentBytes(text, charset));
            byte[] result = signature.sign();
            return Base64.encodeBase64String(result);
        } catch (NoSuchAlgorithmException e) {
            //不可能发生，
            return null;
        }
    }

    /**
     * 签名字符串
     *
     * @param text
     *            需要签名的字符串
     * @param sign
     *            客户签名结果
     * @param publicKey
     *            公钥(BASE64编码)
     * @param charset
     *            编码格式
     * @return 验签结果
     */
    public static boolean verify(String text, String sign, String publicKey, String charset)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(getContentBytes(text, charset));
        return signature.verify(Base64.decodeBase64(sign));

    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;

    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;

    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;

    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param cert 证书
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, Certificate cert) throws Exception {

        // 对数据加密
        PublicKey uk = cert.getPublicKey();
        Cipher cipher = Cipher.getInstance(uk.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, uk);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;

    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;

    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        byte[] value =data.getBytes("utf-8");
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = value.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(value, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(value, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return YaColIPayBase64.encode(encryptedData);

    }

}

