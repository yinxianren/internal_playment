package com.internal.playment.cross.impl.yacolpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiTransOrderCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.cross.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:31
 * Description:
 */
@Slf4j
@Service(version = "${application.version}" , group = "yaColPay" , timeout = 30000 )
public class YaColPayApiTransOrderCrossComponentImpl implements ApiTransOrderCrossComponent {
    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO trade) {
        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        TransOrderInfoTable transOrder = trade.getTransOrderInfoTable();
        JSONObject others = JSONObject.parseObject(channelInfo.getChannelParam());
        String publicKey = others.getString("publicKey").trim();;
        String signKey = others.getString("privateKey").trim();
        String publicCheckKey =others.getString("publicCheckKey".trim());

        //基本参数
        String service = "mimer_single_pay2bank";//接口
        String version = "1.0";//接口版本
        String request_time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");//请求时间
        String  partner_id = others.getString("partner_id"); //合作者Id
        String _input_charset = "UTF-8";//字符编码集
        String sign_type="RSA";//签名类型
        String notify_url =others.getString("notify_url"); ;

        //请求参数
        String out_trade_no = transOrder.getPlatformOrderId();//商户订单号
        String identity_id = transOrder.getTerminalMerId();//收款方用户标识
        String bank_account_num = transOrder.getBankCardNum();//收款方银行账号
        String phone_no = transOrder.getBankCardPhone();//银行预留手机号
        String bank_name = transOrder.getBankName();//收款方开户银行
        String bank_code = transOrder.getBankCode();//银行代码
//        String province  = merchantBankCardBinding.getProvince();//省份
//        String city =merchantBankCardBinding.getCity();//城市
//        String bank_branch = tradeObjectSquare.getBankBranchName();//支行名称
        String amount = transOrder.getAmount().setScale(2).toString();
        String backFee = transOrder.getBackFee().setScale(2).toString();

        Map postData = new TreeMap();
        String split_type = "1";//分账类型1-按固定金额；2-按固定比率；
        if (transOrder.getBankAccountProp() == 0){
            postData.put("card_attribute","B");//卡属性
        }else {
            postData.put("card_attribute","C");//卡属性
        }
        if (transOrder.getBankCardType()==1){
            postData.put("card_type","DEBIT");//卡类型 借记
        }else{
            postData.put("card_type","CREDIT");//卡类型 贷记
        }
        String account_type = "BXT_D0_SETTLE";//账户类型

        byte[] bank_account_num_byte = null;
        byte[] phone_no_byte = null;
        try {
            bank_account_num_byte = YaColPayRSAUtil.encryptByPublicKey(bank_account_num.getBytes("UTF-8"), publicKey);
            phone_no_byte = YaColPayRSAUtil.encryptByPublicKey(phone_no.getBytes("UTF-8"), publicKey);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String bank_account_num_encrypt = YaColIPayBase64.encode(bank_account_num_byte);
        String phone_no_encrypt = YaColIPayBase64.encode(phone_no_byte);
        postData.put("service",service);
        postData.put("version",version);
        postData.put("request_time", request_time);
        postData.put("partner_id",partner_id);
        postData.put("_input_charset",_input_charset);
        postData.put("notify_url",notify_url);
        postData.put("out_trade_no",out_trade_no);  //商户订单号
        postData.put("identity_id",identity_id); //收款方用户标识
        postData.put("bank_name",bank_name);//收款方开户银行
        postData.put("bank_code",bank_code);//银行代码
        postData.put("amount",amount);//金额
        postData.put("split_amount",backFee);    //分账金额
        postData.put("split_type",split_type); //分账类型
        postData.put("account_type",account_type);//账户类型
//        postData.put("province",province);//省份
//        postData.put("city",city);//城市
//        postData.put("bank_branch",bank_branch);//支行名称
        postData.put("bank_account_num",bank_account_num_encrypt);//收款方银行账号
        postData.put("phone_no",phone_no_encrypt);//银行预留手机号

        String contents = YaColIPayTools.createLinkString(postData,false);
        log.info("雅酷代付申请签名前postDatas:{}",postData.toString());
        //签名
        try {
            postData.put("sign", YaColPayRSAUtil.sign(contents,signKey,"utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        postData.put("sign_type",sign_type);

        String postDatas = YaColIPayTools.createLinkString(postData, true);
        log.info("雅酷代付申请postDatas:{}",postDatas);
        try {
            String result = URLDecoder.decode(
                    CallServiceUtil.sendPost(others.getString("masRequestUrl"), postDatas),"UTF-8");
            log.info("雅酷代付申请result:{}",result);
            Map<String, String> content = JSON.parseObject(result,Map.class);
            String sign_result = content.get("sign");
            String sign_type_result = content.get("sign_type");
            String _input_charset_result = content.get("_input_charset")
                    .toString();
            content.remove("sign");
            content.remove("sign_type");
            content.remove("sign_version");
            String like_result = YaColIPayTools.createLinkString(content, false);
            if (YaColIPaySignUtil.Check_sign(like_result, sign_result,
                    sign_type_result, publicCheckKey, _input_charset_result)) {

                String responseCode = content.get("response_code");
                switch (responseCode){
                    case "APPLY_SUCCESS" :
                        String tradeStatus = content.get("withdraw_status");
                        switch (tradeStatus){
                            case "SUCCESS" :
                                bankResult.setCrossResponseMsg("成功");
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                                break;
                            case "FAILED" :
                                bankResult.setCrossResponseMsg("失败");
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                break;
                            case "PROCESSING" :
                                bankResult.setCrossResponseMsg("处理中");
                                bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                                bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                break;
                            case "RETURNT_TICKET" :
                                bankResult.setCrossResponseMsg("银行退票");
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                break;
                        }

                        bankResult.setChannelOrderId(content.get("out_trade_no"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        break;
                    case "ADVANCE_FAILED" :
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("短信发送失败");
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("交易失败"+content.get("response_message"));
                        bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossResponseMsg("交易失败,验签不通过");
                bankResult.setChannelResponseMsg(JSONObject.toJSONString(content));
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            bankResult.setCrossResponseMsg("交易失败，系统错误");
            bankResult.setChannelResponseMsg(e.getMessage());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        return bankResult;
    }
}
