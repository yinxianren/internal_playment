package com.internal.playment.pay.component;


import com.alibaba.fastjson.JSONObject;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@Component
public class Md5Component {



    public  String getMd5Sign(Map<String,Object> param)  {
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()){
            String value = param.get(key) != null ? param.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"));
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }

    public  String getMd5SignWithKey(Map<String,Object> param,String md5Key)  {
        StringBuffer sb = new StringBuffer();
        for (String key : param.keySet()){
            String value = param.get(key) != null ? param.get(key).toString() : null;
            if(!StringUtils.isEmpty(value)){
                sb.append(key+"="+value+"&");
            }
        }
        String sign = sb.substring(0,sb.lastIndexOf("&"))+md5Key;
        String md5Sign=  DigestUtils.md5Hex(sign.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return md5Sign;
    }


    public boolean checkMd5(String tradeInfo, String key, InnerPrintLogObject ipo) throws NewPayException {
        TreeMap<String, Object> param = JSONObject.parseObject(tradeInfo,TreeMap.class);
        String signMsg = param.get("signMsg").toString();
        param.remove("signMsg");
        TreeMap<String, Object> map = new TreeMap<>();
        map.putAll(param);
        String md5Info = getMd5SignWithKey(map,key).toUpperCase();
        if (!md5Info.equals(signMsg.toUpperCase()))
             throw new NewPayException( ResponseCodeEnum.RXH00018.getCode(),
                     String.format("%s-->商户号：%s；终端号：%s；错误信息:%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00018.getMsg()),
                     String.format(" %s", ResponseCodeEnum.RXH00018.getMsg()));
        return true;
    };


}
