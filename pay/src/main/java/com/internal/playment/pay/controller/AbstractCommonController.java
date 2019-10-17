package com.internal.playment.pay.controller;

import com.internal.playment.api.entity.table.system.SystemOrderTrackTable;
import com.internal.playment.api.exception.PayException;
import com.internal.playment.api.tools.PayAssert;
import com.internal.playment.api.tools.ResponseCodeEnum;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 上午9:42
 * Description:
 */
public abstract class AbstractCommonController implements PayAssert {


    /**
     *  获取风控前置表数据
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



}
