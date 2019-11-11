package com.internal.playment.cross.impl.yacolpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiBondCardCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.cross.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:24
 * Description:
 */

@Slf4j
@Service(version = "${application.version}" , group = "yaColPay" , timeout = 30000 )
public class YaColPayApiBondCardCrossComponentImpl extends AbstractYaColIPay implements ApiBondCardCrossComponent {
    @Override
    public CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO squareTrade) {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        try {
            ChannelExtraInfoTable extraChannelInfo = squareTrade.getChannelExtraInfoTable();
            JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
            Map<String, String> bondParam = getBondParam(squareTrade);
            if(null == bondParam){
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("cross 工程封装参数异常");
                bankResult.setChannelResponseMsg(null);
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                log.info("【酷宝支付-绑定银行卡】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
                return bankResult;
            }
            String  publicCheckKey=(String)param.get("publicCheckKey");

//            String result = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), param.getString(""), bondParam);
//            String result = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "http://test.gate.yacolpay.com/mgs/gateway.do", bondParam);
//            result = URLDecoder.decode(result,"UTF-8");
            String params = YaColIPayTools.createLinkString(bondParam, true);
            log.info("【酷宝支付-绑定银行卡】绑卡请求参数：{}",params);
            //发送请求
            String result = URLDecoder.decode(CallServiceUtil.sendPost(param.getString("url"), params), "UTF-8");

            log.info("【酷宝支付-绑定银行卡】返回信息：{}",result);
            Map<String,Object> content=JsonUtils.jsonToMap(result);
            if(verificationParam(content,publicCheckKey)){
                if("APPLY_SUCCESS".equalsIgnoreCase(content.get("response_code").toString())){
                    if (content.get("card_id") == null){
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    }else {
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                    }
                    bankResult.setCrossResponseMsg("绑卡申请提交成功");
                    bankResult.setChannelResponseMsg(result);
                    log.info("【酷宝支付-绑定银行卡】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                    return bankResult;
                }else {
                    bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                    bankResult.setCrossResponseMsg("绑卡失败："+content.get("error_message")==null?content.get("errorMessage").toString():content.get("error_message").toString());
                    bankResult.setChannelResponseMsg(result);
                    bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                    bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                    bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    log.info("【酷宝支付-绑定银行卡】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                    return bankResult;
                }
            }else{
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("绑卡失败：验证酷宝返回参数不通过");
                bankResult.setChannelResponseMsg(result);
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                log.info("【酷宝支付-绑定银行卡】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                return bankResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("绑卡失败：请求过程中发生异常");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            log.info("【酷宝支付-绑定银行卡】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
            return bankResult;
        }
    }

    /**
     *  封装发送请求参数
     * @param trade
     * @return
     */
    private Map<String, String> getBondParam(RequestCrossMsgDTO trade){
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject extraChannelInfoData = JSON.parseObject(extraChannelInfo.getChannelParam());
        RegisterInfoTable merchantRegisterInfo = trade.getRegisterInfoTable();
//        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getBackData());
//        Integer bankCardType = Integer.valueOf(merchantCard.getCardType());


        String publicKey = extraChannelInfoData.getString("publicKey");
        String privateKeyRSA =extraChannelInfoData.getString("privateKey");
        /************************基本参数************************/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String service="fastpay_binding_bank_card";//服务名称
        String version="1.0";//接口版本
        String request_time=formatter.format(new Date());//请求时间
        String _input_charset="utf-8";//字符集编码
        String sign_type="RSA";//签名类型
        String sign = "";//签名
        //签约合作方的酷宝唯一商户号
        String partner_id=extraChannelInfoData.getString("partner_id");
        /*******************业务参数**********************************/
        //请求流水号
        String  request_no=merchantCard.getMerOrderId();
        //商户系统用户ID(字母或数字)
        String identity_id=merchantRegisterInfo.getIdentityNum();
        //银行编号
        String bank_code=merchantCard.getBankCode();
        //银行卡号	String	密文，使用酷宝RSA公钥加密。明文长度：50
        String bank_account_no=merchantCard.getBankCardNum();
        //持卡人姓名	String	密文，使用酷宝RSA公钥加密。明文长度：64
        String account_name=merchantCard.getCardHolderName();
        // DEBIT-借记   CREDIT-贷记（信用卡）
        //        1借记卡  2信用卡
        String card_type=merchantCard.getBankCardType().toString();
        //有效期	String	密文，使用酷宝RSA公钥加密。明文长度：10；信用卡专用，有效期(10/13)，（月份/年份）	可空	XAIDFJAASDF
        String validity_period=null;
        // 	CVV2	String	密文，使用酷宝RSA公钥加密。明文长度：10；信用卡专用	可空	XAIDFJAASDF
        String verification_value=null;
        if(card_type.equals("1")) {
            card_type="DEBIT";
        }
        else if(card_type.equals("2")) {
            card_type="CREDIT";
            //有效期	String	密文，使用酷宝RSA公钥加密。明文长度：10；信用卡专用，有效期(10/13)，（月份/年份）	可空	XAIDFJAASDF
            validity_period=merchantCard.getValidDate().substring(0,2)+"/"+merchantCard.getValidDate().substring(2,4);
            // 	CVV2	String	密文，使用酷宝RSA公钥加密。明文长度：10；信用卡专用	可空	XAIDFJAASDF
            verification_value=merchantCard.getSecurityCode();
        }
        // C	对私   B	对公
        //卡属性	String(10)	见附录
        String card_attribute= merchantRegisterCollect.getBankAccountProp().toString();//账户属性0：私人，1：公司
        if(card_attribute.equals("0")) card_attribute="C";
        else if(card_attribute.equals("1")) card_attribute="B";
        //证件类型	String	见附录，目前只支持身份证
        //1身份证、2护照、3港澳回乡证、4台胞证、5军官证、
        Integer certType=merchantRegisterInfo.getIdentityType();
        String cert_type="";
        if(certType==1) cert_type="IC";
        else if(certType==2) cert_type="";
        else if(certType==3) cert_type="";
        else if(certType==4) cert_type="";
        else if(certType==5) cert_type="";
        //证件号码	String	密文，使用酷宝RSA公钥加密。明文长度：50
        String cert_no=  merchantCard.getIdentityNum();
        //银行预留手机号	String	密文，使用酷宝RSA公钥加密。明文长度：16。如认证方式不为空，则要求此信息也不能为空。
        String phone_no=merchantCard.getBankCardPhone();
        //请求者IP	String(50)	用户在商户平台操作时候的IP地址，公网IP，不是内网IP 用于风控校验，请填写用户真实IP，否则容易风控拦截	非空	127.0.0.1
        String client_ip=trade.getIP();
        //一下参数可空
        //省份	String(20)	省份	可空	上海市，江苏省
        String province=merchantRegisterInfo.getProvince();
        //城市	String(20)	城市	可空	上海市，南京市
        String city=merchantRegisterInfo.getCity();
        // 支行名称	String(60)	银行支行名称	可空	中国农业银行深圳南山支行
        String bank_branch=merchantCard.getSubBankName();
        //扩展信息	String(200)	业务扩展信息， 参数格式：参数名1^参数值1|参数名2^参数值2|……	可空	test^true|notify_type^sync
        String extend_param="";

        //敏感数据加密
        byte[] bank_account_no_byte = null;
        byte[] phone_no_byte = null;
        byte[] cert_no_byte = null;
        byte[] account_name_byte = null;
        byte[] validity_period_byte = null;
        byte[] verification_value_byte = null;
        String validity_period_encrypt =null;
        String verification_value_encrypt=null;
        try {
            bank_account_no_byte = YaColPayRSAUtil.encryptByPublicKey(bank_account_no.getBytes("utf-8"), publicKey);
            phone_no_byte = YaColPayRSAUtil.encryptByPublicKey(phone_no.getBytes("utf-8"), publicKey);
            cert_no_byte = YaColPayRSAUtil.encryptByPublicKey(cert_no.getBytes("utf-8"), publicKey);
            account_name_byte = YaColPayRSAUtil.encryptByPublicKey(account_name.getBytes("utf-8"), publicKey);
            if(!StringUtils.isBlank(validity_period))
                validity_period_byte = YaColPayRSAUtil.encryptByPublicKey(validity_period.getBytes("utf-8"),publicKey);
            if(!StringUtils.isBlank(verification_value))
                verification_value_byte =YaColPayRSAUtil.encryptByPublicKey(verification_value.getBytes("utf-8"),publicKey);

            String bank_account_no_encrypt = YaColIPayBase64.encode(bank_account_no_byte);
            String phone_no_encrypt = YaColIPayBase64.encode(phone_no_byte);
            String cert_no_encrypt = YaColIPayBase64.encode(cert_no_byte);
            String account_name_encrypt = YaColIPayBase64.encode(account_name_byte);
            if(null != validity_period_byte)
                validity_period_encrypt = YaColIPayBase64.encode(validity_period_byte);
            if(null != verification_value_byte)
                verification_value_encrypt=YaColIPayBase64.encode(verification_value_byte);
            //数据排序
            Map<String, String> params = new HashMap<>();
            params.put("service", service);
            params.put("version", version);
            params.put("request_time", request_time);
            params.put("partner_id", partner_id);
            params.put("_input_charset", _input_charset);
            params.put("identity_id", identity_id);
            params.put("request_no", request_no);
            params.put("bank_code", bank_code);
            params.put("bank_account_no",bank_account_no_encrypt);
            params.put("card_type", card_type);
            params.put("card_attribute", card_attribute);
            params.put("client_ip",client_ip);
            params.put("cert_type",cert_type);
            params.put("cert_no",cert_no_encrypt);
            params.put("phone_no",phone_no_encrypt);
            params.put("account_name", account_name_encrypt);

            if(!StringUtils.isBlank(province))
                params.put("province", province);
            if(!StringUtils.isBlank(city))
                params.put("city", city);
            if(!StringUtils.isBlank(bank_branch))
                params.put("bank_branch", bank_branch);
            if(!StringUtils.isBlank(extend_param))
                params.put("extend_param", extend_param);
            if(null != validity_period_encrypt)
                params.put("validity_period", validity_period_encrypt);
            if(null != verification_value_encrypt)
                params.put("verification_value", verification_value_encrypt);

            //签名拼接参数
            String content = YaColIPayTools.createLinkString(params, false);
            sign = YaColIPaySignUtil.sign(content, sign_type, privateKeyRSA,_input_charset);
            params.put("sign",sign);
            params.put("sign_type",sign_type);
            return params;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO squareTrade) {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        try{
            ChannelExtraInfoTable extraChannelInfo = squareTrade.getChannelExtraInfoTable();
            JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
            Map<String, String> bondParam = getSendSMSParam(squareTrade);
            if(null == bondParam){
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("cross 工程封装参数异常");
                bankResult.setChannelResponseMsg(null);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                log.info("【酷宝支付-绑卡短信重发】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
                return bankResult;
            }
            String  publicCheckKey=param.getString("publicCheckKey");
            String params = YaColIPayTools.createLinkString(bondParam, true);
            log.info("【酷宝支付-绑卡短信重发】短信确认请求参数：{}",params);
            //发送请求
            String result = URLDecoder.decode(CallServiceUtil.sendPost(param.getString("url"), params), "UTF-8");
            log.info("【酷宝支付-绑卡短信重发】返回信息：{}",result);
            Map<String,Object> content=JsonUtils.jsonToMap(result);
            if(verificationParam(content,publicCheckKey)){
                if("APPLY_SUCCESS".equalsIgnoreCase(content.get("response_code").toString())){
//                    String card_id= content.get("card_id").toString();
                    String ticket=content.get("ticket").toString();
                    bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                    bankResult.setCrossResponseMsg(content.get("response_message").toString());
                    bankResult.setChannelResponseMsg(result);
                    bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
//                    String bankData="{\"card_id\":\""+card_id+"\"}";
                    log.info("【酷宝支付-绑卡短信重发】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
                    return bankResult;
                }else {
                    bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                    bankResult.setCrossResponseMsg(content.get("error_message")==null?content.get("errorMessage").toString():content.get("error_message").toString());
                    bankResult.setChannelResponseMsg(result);
                    log.info("【酷宝支付-绑卡短信重发】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                    bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                    bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                    bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    return bankResult;
                }
            }else{
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossResponseMsg("验证酷宝返回参数不通过");
                bankResult.setChannelResponseMsg(result);
                log.info("【酷宝支付-绑卡短信重发】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                return bankResult;
            }

        }catch (Exception e){
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            bankResult.setCrossResponseMsg("cross 工程处理请求过程中发生异常");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            log.info("【酷宝支付-绑定银行卡短信重发】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
            return bankResult;
        }
    }

    /**
     *  封装请求信息
     * @param trade
     * @return
     */
    private Map<String, String> getSendSMSParam(RequestCrossMsgDTO trade){
        try{

            ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
            JSONObject extraChannelInfoData = JSON.parseObject(extraChannelInfo.getChannelParam());
            MerchantCardTable merchantCard = trade.getMerchantCardTable();
            JSONObject  banckData=JSON.parseObject(merchantCard.getChannelRespResult());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String privateKeyRSA =extraChannelInfoData.getString("privateKey");
            /************************基本参数************************/
            String service="fastpay_binding_bank_card_sms";//服务名称
            String version="1.0";//接口版本
            String request_time=formatter.format(new Date());//请求时间
            String _input_charset="utf-8";//字符集编码
            String sign_type="RSA";//签名类型
            String sign = "";//签名
            String partner_id=extraChannelInfoData.getString("partner_id");//签约合作方的酷宝唯一商户号

            /*******************业务参数**********************************/
            String ticket=banckData.getString("ticket");//返回的ticket
            String client_ip = trade.getIP();;//IP

            Map<String,String> params=new HashMap<String, String>();
            params.put("service",service);
            params.put("version",version);
            params.put("request_time",request_time);
            params.put("partner_id",partner_id);
            params.put("_input_charset",_input_charset);
            params.put("ticket",ticket);
//            params.put("valid_code", valid_code);
            params.put("client_ip",client_ip);
            String content= YaColIPayTools.createLinkString(params, false);
            //签名
            sign= YaColIPaySignUtil.sign(content, sign_type, privateKeyRSA, _input_charset);
            //拼接发送参数
            params.put("sign_type",sign_type);
            params.put("sign",sign);
            return params;
        }catch (Exception e){
            e.getStackTrace();
            return null;
        }
    }

    @Override
    public CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO squareTrade) {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        try{
            ChannelExtraInfoTable extraChannelInfo = squareTrade.getChannelExtraInfoTable();
            JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
            Map<String,String> bondParam = getSendSMSParam(squareTrade);
            if(null == bondParam){
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossResponseMsg("cross 工程封装参数异常");
                bankResult.setChannelResponseMsg(null);
                log.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
                return bankResult;
            }
            String  publicCheckKey=(String)param.get("publicCheckKey");

            String params = YaColIPayTools.createLinkString(bondParam, true);
            log.info("【酷宝支付-绑定银行卡短信验证】短信确认请求参数：{}",params);
            //发送请求
            String result = URLDecoder.decode(CallServiceUtil.sendPost(param.getString("url"), params), "UTF-8");


            log.info("【酷宝支付-绑定银行卡短信验证】返回信息：{}",result);
            Map<String,Object> content= JsonUtils.jsonToMap(result);
            if(verificationParam(content,publicCheckKey)){
                if("APPLY_SUCCESS".equalsIgnoreCase(content.get("response_code").toString())){
                    String card_id= content.get("card_id").toString();
                    bankResult.setCrossStatusCode(StatusEnum._0.hashCode());
                    bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                    bankResult.setCrossResponseMsg("绑卡成功");
                    bankResult.setChannelResponseMsg(result);
                    log.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                    return bankResult;
                }else {
                    bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                    bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    bankResult.setCrossResponseMsg(content.get("error_message")==null?content.get("errorMessage").toString():content.get("error_message").toString());
                    bankResult.setChannelResponseMsg(result);
                    log.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                    return bankResult;
                }
            }else{
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossResponseMsg("验证酷宝返回参数不通过");
                bankResult.setChannelResponseMsg(result);
                log.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                return bankResult;
            }

        }catch (Exception e){
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            bankResult.setCrossResponseMsg("cross 工程处理请求过程中发生异常");
            bankResult.setChannelResponseMsg(e.getMessage());
            log.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
            return bankResult;
        }
    }
}
