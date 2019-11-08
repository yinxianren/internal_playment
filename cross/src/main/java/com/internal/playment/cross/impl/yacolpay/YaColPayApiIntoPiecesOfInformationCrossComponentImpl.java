package com.internal.playment.cross.impl.yacolpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiIntoPiecesOfInformationCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.cross.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:21
 * Description:
 *
 *
 */
@Slf4j
@Service(version = "${application.version}", group = "yaColPay"  , timeout = 30000 )
public class YaColPayApiIntoPiecesOfInformationCrossComponentImpl implements ApiIntoPiecesOfInformationCrossComponent {
    @Override
    public CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO requestCrossMsgDTO) {
        return null;
    }

    @Override
    public CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        ChannelExtraInfoTable exChannelInfo = squareTrade.getChannelExtraInfoTable();
        JSONObject others = JSONObject.parseObject(exChannelInfo.getChannelParam());
        String publicCheckKey = others.getString("publicCheckKey");
        Map<String, String> map = getValidate(squareTrade);
        String params = YaColIPayTools.createLinkString(map, true);
        log.info("雅酷支付小微商户注册请求参数: "+params);
        String result = URLDecoder.decode(CallServiceUtil.sendPost(others.getString("url"),params), "UTF-8");
//        String result = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "http://test.gate.yacolpay.com/mgs/gateway.do", new TreeMap<>(map));
        log.info("雅酷支付小微商户注册返回参数: "+result);
        Map content = JSON.parseObject(result, Map.class);
        String sign_result = content.get("sign").toString();
        String sign_type_result = content.get("sign_type").toString();
        String _input_charset_result = content.get("_input_charset")
                .toString();
        content.remove("sign");
        content.remove("sign_type");
        content.remove("sign_version");
        String like_result = YaColIPayTools.createLinkString(content, false);
        if (YaColIPaySignUtil.Check_sign(like_result, sign_result,
                sign_type_result, publicCheckKey, _input_charset_result)) {
            String responseCode = content.get("response_code").toString();
            String responseMessage = content.get("response_message").toString();
            if (responseCode.equals("APPLY_SUCCESS")){
                bankResult.setChannelResponseMsg("进件成功");
                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
            }else {
                bankResult.setChannelResponseMsg("进件失败："+responseMessage);
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            }
            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setChannelResponseMsg("进件失败,验签不通过");
            bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
        }
        return bankResult;
    }

    private Map<String,String> getValidate(RequestCrossMsgDTO squareTrade) throws Exception {
//        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        ChannelExtraInfoTable extraChannelInfo = squareTrade.getChannelExtraInfoTable();
        JSONObject others = JSONObject.parseObject(extraChannelInfo.getChannelParam());
        String publicKey = others.getString("publicKey");
        String privateKey = others.getString("privateKey");
        RegisterCollectTable merchantRegisterCollect = squareTrade.getRegisterCollectTable();
        RegisterInfoTable merchantRegisterInfo = squareTrade.getRegisterInfoTable();
        String merName = merchantRegisterInfo.getUserName();
        String merCert = merchantRegisterInfo.getIdentityNum();
        String merPhone = merchantRegisterCollect.getBankCardPhone();
        String mimer_cert_pic1 = new String(merchantRegisterCollect.getMiMerCertPic1());
        String mimer_cert_pic2 = new String(merchantRegisterCollect.getMiMerCertPic2());
        byte[] mimer_name_byte = null;
        byte[] mimer_cert_no_byte = null;
        byte[] mimer_phone_byte = null;
        try {
            mimer_name_byte = YaColPayRSAUtil.encryptByPublicKey(merName.getBytes("utf-8"), publicKey);
            mimer_cert_no_byte = YaColPayRSAUtil.encryptByPublicKey(merCert.getBytes("utf-8"), publicKey);
            mimer_phone_byte = YaColPayRSAUtil.encryptByPublicKey(merPhone.getBytes("utf-8"), publicKey);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String mimer_name_encrypt = YaColIPayBase64.encode(mimer_name_byte);
        String mimer_cert_no_encrypt = YaColIPayBase64.encode(mimer_cert_no_byte);
        String mimer_phone_encrypt = YaColIPayBase64.encode(mimer_phone_byte);

        Map<String,String> params = new HashMap<>();
        params.put("service", "mimer_register_merchant");
        params.put("version", "1.0");
        params.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        params.put("partner_id", others.getString("partner_id"));
        params.put("_input_charset","UTF-8");

        params.put("request_no", merchantRegisterCollect.getMerOrderId());
        params.put("identity_id", merchantRegisterCollect.getTerminalMerId());
        params.put("mimer_name",mimer_name_encrypt);
        params.put("mimer_cert_no",mimer_cert_no_encrypt);
        params.put("mimer_phone", mimer_phone_encrypt);
        params.put("mimer_cert_pic1", mimer_cert_pic1);
        params.put("mimer_cert_pic2", mimer_cert_pic2);

        String content = YaColIPayTools.createLinkString(params, false);//排序后的待签名字符串
//        params.put("sign", YacolPayUtil.getSign(params,privateKey));
        String sign = YaColIPaySignUtil.sign(content, "RSA", privateKey,
                "UTF-8");
//        sign =  URLEncoder.encode(sign,"UTF-8");
        params.put("sign",sign);
        params.put("sign_type","RSA");
        return params;
    }
}
