package com.internal.playment.inward.controller;

import com.internal.playment.common.enums.ParamTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.inner.*;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午2:41
 * Description:
 */
public abstract class NewAbstractCommonController implements NewPayAssert, PayUtil {

    private final int STRING = 1;//字符
    private final int AMOUNT = 2;//金额
    private final int IPv6 = 6;//IPv6
    private final int IPv4 = 4;//IPv4
    private final int URL = 5;//请求路径
    private final int PHONE = 7;//手机号





    protected  void  assertIsNull(Object object, ResponseCodeEnum responseCodeEnum, InnerPrintLogObject ipo) throws NewPayException {
        isNull(object,
                responseCodeEnum.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道ID获取通道信息",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),responseCodeEnum.getMsg()),
                format(" %s",responseCodeEnum.getMsg()));
    }
    /**
     * 获取风控前置表数据
     * @param request
     * @param param
     * @param bussType
     * @return
     * @throws Exception
     */
    protected SystemOrderTrackTable getSystemOrderTrackTable(HttpServletRequest request, String param, final String bussType) throws Exception {
        String reqUrl = request.getHeader(HttpHeaders.REFERER) == null ?
                request.getRequestURL().toString() :
                request.getHeader(HttpHeaders.REFERER);
        isNull(param, ResponseCodeEnum.RXH00013.getCode(),format("%s-->%s",bussType,ResponseCodeEnum.RXH00013.getMsg()),
                ResponseCodeEnum.RXH00013.getMsg());//请求参数为空
        String tradeInfoDecode = new String(Base64.decodeBase64(param.getBytes()));
        param = URLDecoder.decode(tradeInfoDecode, "UTF-8");
        String ip = IpUtils.getReallyIpForRequest(request);
        return new SystemOrderTrackTable()
                .setRequestMsg(param)
                .setReferPath(reqUrl)
                .setIp(ip)
                .setTradeTime(new Date());
    }

    /**
     *   验证必要参数
     * @param mustParamMap
     * @param obj
     * @param ipo
     * @throws Exception
     */
    protected  void verify(Map<String, ParamRule> mustParamMap, Object obj, InnerPrintLogObject ipo) throws Exception {
        Set<String> mustParamList = mustParamMap.keySet();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(int i=0; i<fields.length;i++){
            fields[i].setAccessible(true);
            String fieldName = fields[i].getName();
            if(mustParamList.contains(fieldName)){
//                Class clz =  fields[i].getType();//获取参数类型
                String value = (String) fields[i].get(obj);//获取参数值
                isBlank(value,//为空则是缺少必要值
                        ResponseCodeEnum.RXH00014.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00014.getMsg(),fieldName),
                        format(" %s : %s",ResponseCodeEnum.RXH00014.getMsg(),fieldName));
                ParamRule pr =  mustParamMap.get(fieldName);
                switch (pr.getType()){
                    case STRING:
                        int strLenght = value.length();
                        isTrue( (strLenght<pr.getMinLength() || strLenght>pr.getMaxLength()),
                                ResponseCodeEnum.RXH00015.getCode(),
                                format("%s-->商户号：%s；终端号：%s；错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00016.getMsg(),fieldName),
                                format(" %s : %s，该值字符缺少或过长",ResponseCodeEnum.RXH00015.getMsg(),fieldName));
                        break;
                    case AMOUNT:
                        isTrue( !(value.matches(ParamTypeEnum.AMOUNT.getMatches())),
                                ResponseCodeEnum.RXH00016.getCode(),
                                format("%s-->商户号：%s；终端号：%s；错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00016.getMsg(),fieldName),
                                format(" %s : %s,该值必须是金额格式",ResponseCodeEnum.RXH00016.getMsg(),fieldName));
                        break;
                    case IPv6:
                        isTrue( !(value.matches(ParamTypeEnum.IPv6.getMatches())),
                                ResponseCodeEnum.RXH00016.getCode(),
                                format("%s-->商户号：%s；终端号：%s；错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00016.getMsg(),fieldName),
                                format(" %s : %s，该值必须是IPv6格式",ResponseCodeEnum.RXH00016.getMsg(),fieldName));
                        break;
                    case IPv4:
                        isTrue( !(value.matches(ParamTypeEnum.IPv4.getMatches())),
                                ResponseCodeEnum.RXH00016.getCode(),
                                format("%s-->商户号：%s；终端号：%s；错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00016.getMsg(),fieldName),
                                format(" %s : %s，该值必须是IPv4格式",ResponseCodeEnum.RXH00016.getMsg(),fieldName));
                        break;
                    case URL:
                        isTrue( !(value.matches(ParamTypeEnum.URL.getMatches())),
                                ResponseCodeEnum.RXH00016.getCode(),
                                format("%s-->商户号：%s；终端号：%s；错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00016.getMsg(),fieldName),
                                format(" %s : %s，该值必须是URL格式",ResponseCodeEnum.RXH00016.getMsg(),fieldName));
                        break;
                    case PHONE:
                        isTrue( !(value.matches(ParamTypeEnum.PHONE.getMatches())),
                                ResponseCodeEnum.RXH00016.getCode(),
                                format("%s-->商户号：%s；终端号：%s；错误信息( %s : %s )",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00016.getMsg(),fieldName),
                                format(" %s : %s，该值必须是手机号格式",ResponseCodeEnum.RXH00016.getMsg(),fieldName));
                        break;
                }
            }
        }
    }

}
