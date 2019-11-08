package com.internal.playment.cross.impl.yacolpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiPayOrderCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.HttpClientUtils;
import com.internal.playment.common.inner.UUID;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.cross.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:28
 * Description:
 */
@Slf4j
@Service(version = "${application.version}", group = "yaColPay"  , timeout = 30000 )
public class YaColPayApiPayOrderCrossComponentImpl implements ApiPayOrderCrossComponent {
    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        PayOrderInfoTable payOrder = squareTrade.getPayOrderInfoTable();
        RegisterCollectTable merchantRegisterCollect = squareTrade.getRegisterCollectTable();
        RegisterInfoTable merchantRegisterInfo = squareTrade.getRegisterInfoTable();
        String  outTradeNo = payOrder.getPlatformOrderId();
        ChannelInfoTable channelInfo = squareTrade.getChannelInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String publicKey = others.getString("publicKey").trim();
        String signKey = others.getString("privateKey").trim();
        String publicCheckKey =  others.getString("publicCheckKey").trim();
        TreeMap param = new TreeMap();
        //公共参数
        param.put("service","mimer_bank_card_pay");//接口名称
        param.put("version","1.0");//版本号
        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
        param.put("partner_id",others.getString("partner_id"));//平台商户号
        param.put("_input_charset","utf-8");//字符集
        param.put("notify_url",others.get("notify_url"));
        //业务参数
        param.put("device_id",payOrder.getDeviceId());
        param.put("device_type",payOrder.getDeviceType());
        param.put("mac_address",payOrder.getMacAddr());
        param.put("out_trade_no",outTradeNo);//商户订单号
        param.put("summary","商品交易");//交易内容摘要
        param.put("payer_id",JSONObject.parseObject(merchantRegisterCollect.getChannelRespResult()).getString("mimer_member_id"));//小微商户系统用户ID
        param.put("payee_id",payOrder.getTerminalMerId());//收款方用户标识
        param.put("split_type","2");//分账类型,1-按固定金额；2-按固定比率；
        if (param.get("split_type").equals("1")){
            param.put("split_amount","2.00");//分账金额
        }else {
            param.put("split_ratio",payOrder.getPayFee().toString());//分账比率
        }
//        param.put("split_ratio","2.5");
        param.put("amount",payOrder.getAmount().setScale(2).toString());//金额
        param.put("bank_code",merchantRegisterCollect.getBankCode());//银行编码
        if (merchantRegisterInfo.getMerchantType().equals("00")){
            param.put("card_attribute","B");//卡属性
        }else {
            param.put("card_attribute","C");//卡属性
        }
        if (payOrder.getBankCardType()==1){
            param.put("card_type","DEBIT");//卡类型 借记
        }else{
            param.put("card_type","CREDIT");//卡类型 贷记
            byte[] validity_period_byte = null;
            byte[] verification_value_byte = null;
            validity_period_byte = YaColPayRSAUtil.encryptByPublicKey((payOrder.getValidDate().substring(0,2)+"/"+payOrder.getValidDate().substring(2,4)).getBytes("utf-8"),publicKey);
            verification_value_byte =YaColPayRSAUtil.encryptByPublicKey(payOrder.getSecurityCode().getBytes("utf-8"),publicKey);
            String validity_period_encrypt = YaColIPayBase64.encode(validity_period_byte);
            String verification_value_encrypt=YaColIPayBase64.encode(verification_value_byte);
            param.put("validity_period",validity_period_encrypt);
            param.put("verification_value",verification_value_encrypt);
        }
        param.put("cert_type","IC");//证件类型
        param.put("payer_ip",squareTrade.getIP());//IP地址
        param.put("settle_account_type","BXT_D0_SETTLE");//结算账户类型,BXT_BASIC基本账户，BXT_D0_SETTLE D0账户
        //敏感数据加密
        byte[] bank_card_no = null;
        byte[] account_name = null;
        byte[] cert_no = null;
        byte[] phone_no = null;


        try {
            bank_card_no = YaColPayRSAUtil.encryptByPublicKey(payOrder.getBankCardNum().getBytes("utf-8"), publicKey);
            account_name = YaColPayRSAUtil.encryptByPublicKey(merchantRegisterCollect.getCardHolderName().getBytes("utf-8"), publicKey);
            cert_no = YaColPayRSAUtil.encryptByPublicKey(merchantRegisterInfo.getIdentityNum().getBytes("utf-8"), publicKey);
            phone_no = YaColPayRSAUtil.encryptByPublicKey(payOrder.getBankCardPhone().getBytes("utf-8"), publicKey);
        }catch(Exception e){
            e.printStackTrace();
        }
        String bank_card_no_encrypt = YaColIPayBase64.encode(bank_card_no);
        String account_name_encrypt = YaColIPayBase64.encode(account_name);
        String cert_no_encrypt = YaColIPayBase64.encode(cert_no);
        String phone_no_encrypt = YaColIPayBase64.encode(phone_no);

        param.put("bank_card_no", bank_card_no_encrypt);//银行卡号
        param.put("account_name",account_name_encrypt);//持卡人姓名
        param.put("cert_no",cert_no_encrypt);//证件号码
        param.put("phone_no",phone_no_encrypt);//手机号
        param.put("sign", YacolPayUtil.getSign(param,signKey));
        param.put("sign_type","RSA");//加密方式

        String params = YaColIPayTools.createLinkString(param, true);
        log.info("雅酷支付申请param:"+param);
        try {
            String result = URLDecoder.decode(
                    CallServiceUtil.sendPost(others.getString("bankCardPayUrl"), params), "UTF-8");
            log.info("雅酷支付申请result:"+result);
            Map content = JSON.parseObject(result,Map.class);
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
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        String tradeStatus = content.get("trade_status").toString();
                        switch (tradeStatus){
                            case "WAIT_PAY" :
                                bankResult.setCrossResponseMsg("提交成功,等待付款");
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                break;
                            case "PAY_FINISHED" :
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("提交成功,已付款");
                                break;
                            case "TRADE_FAILED" :
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("交易失败");
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                break;
                            case "TRADE_FINISHED" :
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("提交成功,交易结束");
                                break;
                            case "TRADE_CLOSED" :
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("交易关闭");
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                break;
                        }
                        bankResult.setChannelOrderId(content.get("out_trade_no").toString());
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信发送失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:"+content.get("error_message"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败，系统错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        log.info("雅酷支付申请返回结果"+JSONObject.toJSONString(bankResult));
        return bankResult;
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        PayOrderInfoTable payOrder = squareTrade.getPayOrderInfoTable();
        JSONObject trxidJson = JSON.parseObject(payOrder.getChannelRespResult());
        ChannelInfoTable channelInfo = squareTrade.getChannelInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String signKey = others.getString("privateKey");
        String publicCheckKey = others.getString("publicCheckKey");
        TreeMap param = new TreeMap();
        //公共参数
        param.put("service","mimer_pay_sms");//接口名称
        param.put("version","1.0");//版本号
        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
        param.put("partner_id",others.getString("partner_id"));//平台商户号
        param.put("_input_charset","UTF-8");//字符集
        param.put("notify_url",others.get("notify_url"));
        //私有参数
        param.put("ticket",trxidJson.getString("ticket"));
        param.put("sign", URLEncoder.encode(YacolPayUtil.getSign(param,signKey),"utf-8"));
        param.put("sign_type","RSA");//加密方式
        log.info("雅酷支付短信重发param:"+param);
        try {
            String result = URLDecoder.decode(
                    HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(),others.getString("bankCardPayUrl"), param), "UTF-8");
            log.info("雅酷支付短信重发result:"+result);
            Map content = JSON.parseObject(result,Map.class);
            String sign_result = content.get("sign").toString();
            String sign_type_result = content.get("sign_type").toString();
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result.toString(), sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {
                String responseCode = content.get("response_code").toString();
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("短信发送成功");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信发送失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    case "PAY_SMS_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信重发失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:"+content.get("error_message"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败，系统错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        return bankResult;
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO squareTrade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        PayOrderInfoTable payOrder = squareTrade.getPayOrderInfoTable();
        JSONObject trxidJson = JSON.parseObject(payOrder.getChannelRespResult());
        ChannelInfoTable channelInfo = squareTrade.getChannelInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String signKey = others.getString("privateKey");
        String publicCheckKey = others.getString("publicCheckKey");
        TreeMap param = new TreeMap();
        //公共参数
        param.put("service","mimer_advance_pay");//接口名称
        param.put("version","1.0");//版本号
        param.put("request_time", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));//请求时间
        param.put("partner_id",others.getString("partner_id"));//平台商户号
        param.put("_input_charset","UTF-8");//字符集
        param.put("notify_url",others.get("notify_url"));
        //私有参数
        param.put("out_advance_no", UUID.createKey("pay_order"));
        param.put("ticket",trxidJson.getString("ticket"));
        param.put("validate_code",payOrder.getSmsCode());
        param.put("user_ip",squareTrade.getIP());
        param.put("sign", URLEncoder.encode(YacolPayUtil.getSign(param,signKey),"utf-8"));
        param.put("sign_type","RSA");//加密方式
        log.info("雅酷支付确认param:"+param);
        try {
            String result = URLDecoder.decode(
                    HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(),others.getString("bankCardPayUrl"), param), "UTF-8");
            log.info("雅酷支付确认result:"+result);
            Map content = JSON.parseObject(result,Map.class);
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
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易成功");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信发送失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:"+content.get("error_message"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败，系统错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        return bankResult;
    }
}
