package com.internal.playment.cross.tools;

import com.internal.playment.common.inner.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class AllinPayUtils {


    public static String getMd5Sign(Map<String,Object> postData){
        StringBuffer sb = new StringBuffer();
        for (String key : postData.keySet()){
            String value = postData.get(key) != null ? postData.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"));
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }


    public static String getRandomSecretkey() {
        String val = "";
        Random random = new Random();
        //length为几位密码
        for (int i = 0; i < 8; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
