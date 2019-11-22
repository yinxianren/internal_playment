package com.internal.playment.cross.impl.allinpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.allinpay.ApiAllinPayOtherBusinessCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.HttpClientUtils;
import com.internal.playment.common.inner.StringUtils;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.cross.tools.AllinPayUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:35
 * Description:
 */
@Slf4j
@Service(version = "${application.version}", group = "allinPay" , timeout = 30000 )
public class ApiAllinPayOtherBusinessCrossComponentImpl implements ApiAllinPayOtherBusinessCrossComponent {

    @Override
    public CrossResponseMsgDTO queryByPayOrder(RequestCrossMsgDTO trade) throws ParseException {
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        String tradeResult = payOrder.getChannelRespResult();
        JSONObject bankInfo = JSON.parseObject(tradeResult);
        //公共参数
        String orgid = json.get("orgid").toString();//机构号
        String appid = json.get("appid").toString();//APPID
        String key = json.get("key").toString();
        String cusid = registerInfo.get("cusid").toString();//商户号
        String orderid = payOrder.getPlatformOrderId();//商户订单号
        Date bankTime = new Date();
        if (payOrder.getUpdateTime() != null){
            bankTime = payOrder.getUpdateTime();
        }
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat2.format(bankTime);
        String trxid = null;
        if (null != (bankInfo.get("trxid"))) trxid = bankInfo.get("trxid").toString();//银行流水号
        String version = "11";//版本号
        String randomstr = AllinPayUtils.getRandomSecretkey();//随机字符串

        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("version", version);
        treeMap.put("randomstr", randomstr);

        treeMap.put("cusid", cusid);
        treeMap.put("orderid", orderid);
        if (null != trxid)  treeMap.put("trxid", trxid);
        treeMap.put("date", date);
        treeMap.put("key", key);
        treeMap.put("sign", AllinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");


        log.info("快捷交易查询请求参数：{}",JSON.toJSONString(treeMap));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("queryUrl"), treeMap);
        log.info("快捷交易查询银行返回：{}" ,content);


        return checkQueryResult(content);
    }

    private CrossResponseMsgDTO checkQueryResult(String content) throws ParseException {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        if (StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if (retcode.equals("SUCCESS")) {
                // 判断交易状态
                String trxstatus = json.get("trxstatus").toString();
                TreeMap<String, Object> result = JSON.parseObject(content, TreeMap.class);
                switch (trxstatus) {
                    case "0000":
                        String trxid = result.get("trxid").toString();
                        String orderid = result.get("orderid").toString();
                        String trxamt = result.get("trxamt").toString();
                        BigDecimal amount= new BigDecimal(trxamt).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
                        String fintime = result.get("fintime").toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date banktime = dateFormat.parse(fintime);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                        bankResult.setCrossResponseMsg("交易成功,交易流程完成");
                        bankResult.setChannelResponseTime(banktime);
                        bankResult.setChannelOrderId(trxid);
                        bankResult.setChannelResponseMsg(content);
                        break;

                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "0003":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "3054":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("交易结果未知:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }
            } else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossResponseMsg("快捷交易查询失败:" + json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        } else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            bankResult.setCrossResponseMsg("快捷交易查询失败：支付返回结果为空！");
            bankResult.setChannelResponseMsg(content);
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        log.info("快捷交易查询返回payment: {}",JSON.toJSONString(bankResult));
        return bankResult;
    }

    @Override
    public CrossResponseMsgDTO queryByTransOrder(RequestCrossMsgDTO trade) {
        TreeMap<String, Object> params = getTradeParam(trade);
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        log.info("提现(付款)交易查询参数:{}",JSON.toJSONString(params));
//        log.info("提现(付款)交易查询请求地址"+json.getString("payWithdrawQueryUrl"));
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("payWithdrawQueryUrl"), params);
        log.info("提现(付款)交易查询返回参数{}",content);
        return checkResult(content);
    }

    private CrossResponseMsgDTO checkResult(String content) {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        bankResult.setChannelResponseTime(new Date());
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode.equals("SUCCESS")) {
                // 判断交易状态
                if (json.get("trxstatus") == null){
                    bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                    bankResult.setCrossResponseMsg("交易状态未知，请查询交易");
                    bankResult.setChannelResponseMsg(content);
                    return bankResult;
                }
                String trxstatus = json.get("trxstatus").toString();
                TreeMap<String,Object> resultTreeMap = JSON.parseObject(content,TreeMap.class);
                switch(trxstatus){
                    case "0000":
                        String trxid = resultTreeMap.get("trxid").toString();
                        bankResult.setChannelResponseTime(new Date());
                        bankResult.setChannelOrderId(trxid);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                        bankResult.setCrossResponseMsg("交易成功");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                        bankResult.setCrossResponseMsg("交易已受理");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "3035":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("交易不存在");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00008.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00008.getMsg());
                        break;
                    case "0003":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    case "3054":
                        bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                        bankResult.setCrossResponseMsg("交易处理中，请查询交易");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("交易失败:" + json.get("errmsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }

            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossResponseMsg("交易失败:" + json.get("retmsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            bankResult.setCrossResponseMsg("交易失败：支付返回结果为空！");
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
//        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
//        bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
//        bankResult.setCrossResponseMsg("交易成功");
//        bankResult.setChannelResponseMsg(content);
        return bankResult;
    }

    private TreeMap<String, Object> getTradeParam(RequestCrossMsgDTO trade) {
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        TransOrderInfoTable transOrder = trade.getTransOrderInfoTable();

        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);

        TreeMap<String, Object> treeMap = new TreeMap<>();
        String orgid = json.get("orgid").toString();//机构号
        String appid = json.get("appid").toString();//APPID
        String key = json.get("key").toString();
        String cusid = registerInfo.getString("cusid"); //商户号
        Date bankTime = transOrder.getCreateTime();
        if (null != transOrder.getUpdateTime()) bankTime = transOrder.getUpdateTime();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat2.format(bankTime);
        String orderid = transOrder.getPlatformOrderId();
        String version = "11";

        treeMap.put("orgid",orgid);
        treeMap.put("appid",appid);
        treeMap.put("version",version);
        treeMap.put("cusid",cusid);
        treeMap.put("date",date);
        treeMap.put("orderid",orderid);
        treeMap.put("randomstr", AllinPayUtils.getRandomSecretkey());
        if (!StringUtils.isBlank(transOrder.getChannelOrderId()))
            treeMap.put("trxid",transOrder.getChannelOrderId());
        treeMap.put("key",key);
        treeMap.put("sign",AllinPayUtils.getMd5Sign(treeMap));
        treeMap.remove("key");
        return treeMap;
    }

    @Override
    public CrossResponseMsgDTO unBondCard(RequestCrossMsgDTO trade) {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        Map<String, Object> getCodeParam = getUnBondCardParam(trade);
        log.info("解除绑卡请求参数：{}",getCodeParam);
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), param.getString("unBondCardUrl"), getCodeParam);
//        String content = "{\"agreeid\":\"201906111653355078\",\"appid\":\"6666678\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"0FED3B1A33F2DB99A89EE4938C84854F\",\"trxstatus\":\"0000\"}";
        log.info("解除绑卡请求返回结果：{}",content);
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");

        if ("SUCCESS".equals(resultCode)) {
            bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
            bankResult.setCrossResponseMsg("解绑成功");
            bankResult.setChannelResponseMsg(content);
            bankResult.setChannelResponseTime(new Date());
        } else {
            String eroMsg = result.getString("retmsg");
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("解绑失败");
            bankResult.setChannelResponseMsg(content);
        }
        log.info("解除绑卡返回结果{}",JSON.toJSONString(bankResult));
        return bankResult;
    }

    private Map<String, Object> getUnBondCardParam(RequestCrossMsgDTO trade) {
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        TreeMap<String, Object> postData = new TreeMap<>();
        String cardno = merchantCard.getBankCardNum();
        String cusid = registerInfo.getString("cusid");
        String bankKey = param.getString("key");
        String orgid = param.getString("orgid");
        String appid = param.getString("appid");
        String randomstr = AllinPayUtils.getRandomSecretkey();
        postData.put("appid", appid);
        postData.put("cardno", cardno);
        postData.put("cusid", cusid);
        postData.put("key", bankKey);
        postData.put("orgid", orgid);
        postData.put("randomstr", randomstr);
        postData.put("version", 11);
        postData.put("sign", AllinPayUtils.getMd5Sign(postData));
        postData.remove("key");
        return postData;
    }
}
