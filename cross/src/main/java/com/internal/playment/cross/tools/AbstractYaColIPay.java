package com.internal.playment.cross.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  panda
 * @date  20190703
 */
public abstract class AbstractYaColIPay {



    /**
     *  验证返回结果
     * @param
     */
    protected boolean verificationParam(Map<String,Object> content, String signKey){
        try {

            String sign_result=content.get("sign").toString();
            String sign_type_result=content.get("sign_type").toString();
            String _input_charset_result=content.get("_input_charset").toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");

            Map<String,String> contentStrMap=new HashMap<>(content.size());
            content.forEach((key,value)->contentStrMap.put(key,String.valueOf(value)));
            String like_result= YaColIPayTools.createLinkString(contentStrMap,false);
            return YaColIPaySignUtil.Check_sign(like_result.toString(),sign_result,sign_type_result, signKey,_input_charset_result);
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }

}
