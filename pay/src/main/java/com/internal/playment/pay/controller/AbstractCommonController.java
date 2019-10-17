package com.internal.playment.pay.controller;

import com.internal.playment.api.entity.enums.ParamTypeEnum;
import com.internal.playment.api.entity.inner.InnerPrintLogObject;
import com.internal.playment.api.entity.inner.ParamRule;
import com.internal.playment.api.entity.table.system.SystemOrderTrackTable;
import com.internal.playment.api.exception.PayException;
import com.internal.playment.api.tools.PayAssert;
import com.internal.playment.api.entity.enums.ResponseCodeEnum;
import com.internal.playment.api.tools.PayUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 上午9:42
 * Description:
 */
public abstract class AbstractCommonController implements PayAssert, PayUtils {


    private final int STRING = 1;//字符
    private final int AMOUNT = 2;//金额
    private final int IPv6 = 6;//IPv6
    private final int IPv4 = 4;//IPv4
    private final int URL = 5;//请求路径
    private final int PHONE = 7;//手机号

    /**
     *  风控前置表数据
     * @param request
     * @param reqParam
     * @param sotTable
     * @return
     * @throws PayException
     */
    protected void setSystemOrderTrack(HttpServletRequest request, String reqParam, SystemOrderTrackTable sotTable,String bussType) throws Exception {
        String reqUrl = request.getHeader(HttpHeaders.REFERER) == null ? request.getRequestURL().toString() : request.getHeader(HttpHeaders.REFERER);
        isNull(reqParam,
                ResponseCodeEnum.YKTC000000.getCode(),
                String.format("【%s】--->%s",bussType,ResponseCodeEnum.YKTC000000.getMsg()),
                ResponseCodeEnum.YKTC000000.getMsg());//请求参数为空
        String tradeInfoDecode = new String(Base64.decodeBase64(reqParam.getBytes()));
        reqParam = URLDecoder.decode(tradeInfoDecode, "UTF-8");
        sotTable.setReferUrl(reqUrl).setRequestMsg(reqParam).setTradeTime(new Date());
    }


    /**
     *   验证必要参数
     * @param mustParamMap
     * @param obj
     * @param clazz
     * @param ipo
     * @throws Exception
     */
    protected  void mustParameterValidation(Map<String, ParamRule> mustParamMap, Object obj, Class clazz, InnerPrintLogObject ipo) throws Exception {
        Set<String> mustParamList = mustParamMap.keySet();
        Field[] fields = clazz.getDeclaredFields();
        for(int i=0; i<fields.length;i++){
            String fieldName = fields[i].getName();
            if(mustParamList.contains(fieldName)){
                Class clz =  fields[i].getType();//获取参数类型
                String value = (String) fields[i].get(obj);//获取参数值
                isBlank(value,//为空则是缺少必要值
                        ResponseCodeEnum.YKTC000001.getCode(),
                        format("【】商户号：%s\t终端号：%s\t错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.YKTC000001.getMsg(),fields[i]),
                        format(" %s : %s",ResponseCodeEnum.YKTC000001.getMsg(),fields[i]));
                ParamRule pr =  mustParamMap.get(fieldName);
                switch (pr.getType()){
                    case STRING:
                        int strLenght = value.length();
                        isTrue( !(strLenght>pr.getMinLength() && strLenght<pr.getMaxLength()),
                                ResponseCodeEnum.YKTC000002.getCode(),
                                format("【】商户号：%s\t终端号：%s\t错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.YKTC000002.getMsg(),fields[i]),
                                format(" %s : %s，该值字符缺少或过长",ResponseCodeEnum.YKTC000002.getMsg(),fields[i]));
                        break;
                    case AMOUNT:
                        isTrue( !(value.matches(ParamTypeEnum.AMOUNT.getMatches())),
                                ResponseCodeEnum.YKTC000003.getCode(),
                                format("【】商户号：%s\t终端号：%s\t错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.YKTC000003.getMsg(),fields[i]),
                                format(" %s : %s,该值必须是金额格式",ResponseCodeEnum.YKTC000003.getMsg(),fields[i]));
                        break;
                    case IPv6:
                        isTrue( !(value.matches(ParamTypeEnum.IPv6.getMatches())),
                                ResponseCodeEnum.YKTC000003.getCode(),
                                format("【】商户号：%s\t终端号：%s\t错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.YKTC000003.getMsg(),fields[i]),
                                format(" %s : %s，该值必须是IPv6格式",ResponseCodeEnum.YKTC000003.getMsg(),fields[i]));
                        break;
                    case IPv4:
                        isTrue( !(value.matches(ParamTypeEnum.IPv4.getMatches())),
                                ResponseCodeEnum.YKTC000003.getCode(),
                                format("【】商户号：%s\t终端号：%s\t错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.YKTC000003.getMsg(),fields[i]),
                                format(" %s : %s，该值必须是IPv4格式",ResponseCodeEnum.YKTC000003.getMsg(),fields[i]));
                        break;
                    case URL:
                        isTrue( !(value.matches(ParamTypeEnum.URL.getMatches())),
                                ResponseCodeEnum.YKTC000003.getCode(),
                                format("【】商户号：%s\t终端号：%s\t错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.YKTC000003.getMsg(),fields[i]),
                                format(" %s : %s，该值必须是URL格式",ResponseCodeEnum.YKTC000003.getMsg(),fields[i]));
                        break;
                    case PHONE:
                        isTrue( !(value.matches(ParamTypeEnum.PHONE.getMatches())),
                                ResponseCodeEnum.YKTC000003.getCode(),
                                format("【】商户号：%s\t终端号：%s\t错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.YKTC000003.getMsg(),fields[i]),
                                format(" %s : %s，该值必须是手机号格式",ResponseCodeEnum.YKTC000003.getMsg(),fields[i]));
                        break;
                }
            }
        }
    }

}