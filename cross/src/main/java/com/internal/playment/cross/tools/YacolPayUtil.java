package com.internal.playment.cross.tools;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class YacolPayUtil {

    private final static Logger logger = LoggerFactory.getLogger(YacolPayUtil.class);

    public static String getSign(TreeMap map,String privateKey) throws Exception {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String,Object>> set = map.entrySet();
        for (Map.Entry<String,Object> entry : set){
            if (StringUtils.isBlank(String.valueOf(entry.getValue()))){
                continue;
            }
            if(entry.getKey().equals(map.lastKey())){
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }else{
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        logger.info("待签名字符串："+sb.toString());
        String sign = YaColPayRSAUtil.sign(sb.toString(),privateKey,"UTF-8");
        return sign;
    }
}
