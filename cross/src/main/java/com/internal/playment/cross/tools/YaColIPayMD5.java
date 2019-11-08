package com.internal.playment.cross.tools;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.SignatureException;

/**
 * MD5签名处理核心文件
 * */

public class YaColIPayMD5 {


    public static String sign(String text, String key, String charset) throws Exception {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, charset));
    }

    public static String sign(String text, PrivateKey key, String charset) throws Exception {
        throw new UnsupportedOperationException();
    }

    public static boolean verify(String text, String sign, String key, String charset)
                                                                                      throws Exception {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

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

}
