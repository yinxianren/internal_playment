package com.internal.playment.cross.impl.allinpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiPayOrderCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.HttpClientUtils;
import com.internal.playment.common.inner.StringUtils;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.cross.tools.AllinPayUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:28
 * Description:
 */
@Slf4j
@Service(version = "${application.version}", group = "allinPay" , timeout = 30000 )
public class AllinPayApiPayOrderCrossComponentImpl implements ApiPayOrderCrossComponent {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO trade) throws ParseException {
        TreeMap<String, Object> params = getPayApplyParam(trade);
        String other = trade.getChannelInfoTable().getChannelParam();
        log.info("收单请求参数：{}",JSON.toJSONString(params));
        JSONObject json = JSON.parseObject(other);
        log.info("请求URL："+ json.getString("payApplyUrl"));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("payApplyUrl"), params);
//		String content ="{\"appid\":\"6666678\",\"fintime\":\"20190628100547\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"B64DFBAD0AFEEC07EBF5F88D7AF04677\",\"trxid\":\"19060000001375\",\"trxstatus\":\"1999\"}";
        log.info("收单请求银行返回：{}",content);
        return checkResult1(content, trade);
    }

    /**
     * 获取请求参数
     *  trade
     *  merchantRegisterCollect
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getPayApplyParam(RequestCrossMsgDTO trade){
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);

        //公共参数
        String orgid = json.get("orgid").toString();//机构号
        String appid = json.get("appid").toString();//APPID
        String version = "11";//版本号
        String randomstr = AllinPayUtils.getRandomSecretkey();//随机字符串
        String key = json.get("key").toString();
        //私有参数
        String orderid = payOrder.getPlatformOrderId();//商户订单号
        String cusid = registerInfo.get("cusid").toString();//商户号
        String agreeid = trade.getMerchantCardTable().getCrossRespResult();//协议编号
        String trxcode = json.getString("trxcode");//交易类型
//		[{'trxcode':'QUICKPAY_OF_HP','feerate':'0.50'},  产品1	<=1000元/笔
//		{'trxcode':'QUICKPAY_OF_NP','feerate':'0.45'},   产品2	<=50000元/笔	city、mccid不可上送
//		{'trxcode':'TRX_PAY','feerate':'2'}   代付	未定	上传不是费率，按笔收费
        String amount = payOrder.getAmount().toString();//金额单位元
        String currency = "CNY";//币种
        String subject = "支付订单";//订单内容
        String  notifyurl = json.get("notifyurl").toString();//回调地址
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("version", version);
        treeMap.put("randomstr", randomstr);
        treeMap.put("cusid", cusid);
        treeMap.put("orderid", orderid);
        treeMap.put("agreeid", agreeid);
        treeMap.put("trxcode", trxcode);

        String amountStr=new BigDecimal(amount).multiply(new BigDecimal(100)).toString();
        treeMap.put("amount",amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr);
//    	treeMap.put("amount", amount.multiply(new BigDecimal(100)));//元转分

        treeMap.put("currency", currency);
        treeMap.put("subject", subject);
        if(trade.getRegisterInfoTable()!=null){
            RegisterInfoTable tradeObjectSquare = trade.getRegisterInfoTable();
            if(StringUtils.isNotBlank(tradeObjectSquare.getCity()) && payOrder.getAmount().compareTo(new BigDecimal(1000)) == -1){
                treeMap.put("city", tradeObjectSquare.getCity());
            }
        }
        treeMap.put("notifyurl", notifyurl);
        treeMap.put("key", key);
        treeMap.put("sign", AllinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");
        return treeMap;
    }

    /**
     * 交易结果处理
     * @param content
     * @param trade
     * @return
     */
    @SuppressWarnings("unchecked")
    private CrossResponseMsgDTO checkResult1(String content, RequestCrossMsgDTO trade) throws ParseException {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode.equals("SUCCESS")) {
                // 判断交易状态
                String trxstatus = json.get("trxstatus").toString();
                TreeMap<String,Object> result = JSON.parseObject(content, TreeMap.class);
                switch(trxstatus){
                    case "0000":
                        String resultSign = result.get("sign").toString();
                        result.remove("sign");
                        String other = trade.getChannelInfoTable().getChannelParam();
                        JSONObject param = JSON.parseObject(other);
                        result.put("key", param.getString("key"));
                        String sign = AllinPayUtils.getMd5Sign(result);
                        if(sign.equalsIgnoreCase(resultSign)) {
                            String trxid = result.get("trxid").toString();
                            String fintime = result.get("fintime").toString();
                            Date banktime = dateFormat.parse(fintime);
                            bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                            bankResult.setCrossResponseMsg("交易成功");
                            bankResult.setChannelResponseTime(banktime);
                            bankResult.setChannelOrderId(trxid);
                            bankResult.setChannelResponseMsg(content);
                        }else {
                            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                            bankResult.setCrossResponseMsg("交易异常:签名验证不一致");
                            bankResult.setChannelResponseMsg(content);
                            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        }
                        break;
                    case "1999":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("需获取短信验证码,进行下一步确认操作");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易已受理");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "3059":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信验证码发送失败");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    case "3057":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("请重新获取验证码");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00003.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00003.getMsg());
                        break;
                    case "3058":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信验证码错误");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00004.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00004.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败:" + json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败：支付返回结果为空！");
            bankResult.setChannelResponseMsg(content);
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        log.info("收单请求返回payment：{}",JSON.toJSONString(bankResult));
        return bankResult;
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO trade) {
        TreeMap<String, Object> params = getPayCodeParam(trade);
        String other = trade.getChannelInfoTable().getChannelParam();
        log.info("收单短信请求参数：{}",JSON.toJSONString(params));
        JSONObject json = JSON.parseObject(other);
        log.info("请求URL："+ json.getString("paySms"));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("paySms"), params);
//		String content = "{\"agreeid\":\"201906111653355078\",\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"0FED3B1A33F2DB99A89EE4938C84854F\",\"trxstatus\":\"0000\"}";
        log.info("收单短信请求银行返回：{}", content);
        return checkResult2(content);
    }

    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getPayCodeParam(RequestCrossMsgDTO trade) {
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        JSONObject trxidJson = JSON.parseObject(payOrder.getChannelRespResult());
        //公共参数
        String orgid = json.get("orgid").toString();//机构号
        String appid = json.get("appid").toString();//APPID
        String version = "11";//版本号
        String randomstr = AllinPayUtils.getRandomSecretkey();//随机字符串
        String key = json.get("key").toString();
        //私有参数
        String cusid = registerInfo.get("cusid").toString();//商户号
        String trxid = trxidJson.getString("trxid");//平台交易流水号
        String agreeid = trade.getMerchantCardTable().getCrossRespResult();//协议编号
        //非必须参数
        String thpinfo = "";
        if (trxidJson.get("thpinfo") != null){
            thpinfo = trxidJson.get("thpinfo").toString();//交易透传信息
        }
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("version", version);
        treeMap.put("randomstr", randomstr);
        treeMap.put("cusid", cusid);
        treeMap.put("trxid", trxid);
        treeMap.put("agreeid", agreeid);
        treeMap.put("key", key);
        if(StringUtils.isNotBlank(thpinfo)) {
            treeMap.put("thpinfo", thpinfo);
        }
        treeMap.put("sign", AllinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");
        return treeMap;
    }

    /**
     * 请求结果处理
     * @param content
     * @return
     */
    private CrossResponseMsgDTO checkResult2(String content) {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode .equals("SUCCESS")) {
                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                bankResult.setCrossResponseMsg("短信发送成功");
                bankResult.setChannelResponseMsg(content);
            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("短信发送失败！"+json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }

        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("短信发送请求失败：请求返回结果为空！");
            bankResult.setChannelResponseMsg(content);
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        return bankResult;
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO trade) throws ParseException {
        TreeMap<String, Object> params = getConfirmPayParam(trade);
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        log.info("交易确认请求参数:{}",JSON.toJSONString(params));
        log.info("交易确认请求地址"+json.getString("payConfirmUrl"));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("payConfirmUrl"), params);
//        String content = "{\"appid\":\"6666678\",\"fintime\":\"20190628100547\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"B64DFBAD0AFEEC07EBF5F88D7AF04677\",\"trxid\":\"19060000001375\",\"trxstatus\":\"0000\"}";
        log.info("交易确认银行返回参数{}",content);
        return checkResult3(content);
    }

    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getConfirmPayParam(RequestCrossMsgDTO trade){
        log.info("通联快捷支付交易确认:{}",JSON.toJSONString(trade));
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        String tradeResult = payOrder.getChannelRespResult();
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject date = JSON.parseObject(other);
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        //公共参数
        String orgid = date.get("orgid").toString();//机构号
        String appid = date.get("appid").toString();//APPID
        String cusid = registerInfo.get("cusid").toString();//商户号
        String key =   date.get("key").toString();
        String randomstr = AllinPayUtils.getRandomSecretkey();//随机字符串

        //私有参数
        JSONObject agreeid = JSONObject.parseObject(merchantCard.getChannelRespResult());//协议编号
        //非必须参数
        String smscode = merchantCard.getSmsCode();//短信验证码
        JSONObject bankParam = JSON.parseObject(tradeResult);
        String trxid = bankParam.getString("trxid");//平台交易流水号
        String thpinfo = bankParam.getString("thpinfo");//交易透传信息
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("cusid", cusid);
        treeMap.put("trxid", trxid);
        treeMap.put("agreeid", agreeid.getString("agreeid"));
        treeMap.put("smscode", smscode);
        if(StringUtils.isNotBlank(thpinfo)) {
            treeMap.put("thpinfo", thpinfo);
        }
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("randomstr", randomstr);
        treeMap.put("key", key);
        treeMap.put("sign", AllinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");
        return treeMap;
    }

    /**
     * 交易结果处理
     * @param content
     * @return
     */
    @SuppressWarnings("unchecked")
    private CrossResponseMsgDTO checkResult3(String content) throws ParseException {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode .equals("SUCCESS")) {
                // 判断交易状态
                String trxstatus = json.get("trxstatus").toString();
                switch(trxstatus){
                    case "0000":
                        String fintime = json.getString("fintime");
                        String trxid = json.getString("trxid");
                        bankResult.setChannelResponseTime(dateFormat.parse(fintime));
                        bankResult.setChannelOrderId(trxid);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易成功");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "1999":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("需获取短信验证码,进行下一步确认操作");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("交易已受理");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "3059":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信验证码发送失败");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00005.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00005.getMsg());
                        break;
                    case "3057":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("请重新获取验证码");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00003.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00003.getMsg());
                        break;
                    case "3058":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信验证码错误");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00004.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00004.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易失败:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }

            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossResponseMsg("交易失败:" + json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("交易失败：支付返回结果为空！");
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        return bankResult;
    }
}
