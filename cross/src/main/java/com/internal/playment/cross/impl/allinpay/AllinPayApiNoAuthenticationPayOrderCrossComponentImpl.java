package com.internal.playment.cross.impl.allinpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.allinpay.ApiNoAuthenticationPayOrderCrossComponent;
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
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:40
 * Description:
 */
@Slf4j
@Service(version = "${application.version}" , group = "allinPay" , timeout = 30000 )
public class AllinPayApiNoAuthenticationPayOrderCrossComponentImpl implements ApiNoAuthenticationPayOrderCrossComponent {

    @Override
    public CrossResponseMsgDTO exemptCodePay(RequestCrossMsgDTO trade) {
        TreeMap<String, Object> params = getTradeParam(trade);
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        log.info("通联免短信支付请求参数{}",JSONObject.toJSONString(params));
//        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("freeUrl"), params);
		String content = "{\"appid\":\"6666678\",\"fintime\":\""+new Date().getTime()+"\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"B64DFBAD0AFEEC07EBF5F88D7AF04677\",\"trxid\":\""+trade.getPayOrderInfoTable().getPlatformOrderId()+"\",\"trxstatus\":\"0000\"}";
        log.info("通联免短信支付返回结果：{}",content);
        return checkResult(content, trade);
    }

    /**
     * 获取请求参数
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    private TreeMap<String, Object> getTradeParam(RequestCrossMsgDTO trade) {
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        String other = trade.getChannelInfoTable().getChannelParam();
        JSONObject json = JSON.parseObject(other);
        RegisterCollectTable merchantRegisterInfo = trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterInfo.getChannelRespResult());
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        JSONObject data = JSONObject.parseObject(merchantCard.getChannelRespResult());
//    	//公共参数
        String orgid = json.get("orgid").toString();//机构号
        String appid = registerInfo.get("appid").toString();//APPID
        String version = "11";//版本号
        String randomstr = AllinPayUtils.getRandomSecretkey();//随机字符串
        String key = json.get("key").toString();
//    	//私有参数
//    	String cusid = json.get("cusid").toString();//商户号
        String cusid =registerInfo.get("cusid").toString();

        String orderid = payOrder.getPlatformOrderId();//平台订单号
        String agreeid = data.get("agreeid").toString();//协议编号
        String amount = payOrder.getAmount().toString();//金额单位元
        String currency = "CNY";//币种
        String subject = "小额免短信支付";//订单内容
        Properties prop = new Properties();
        try {
            ClassPathResource classPathResource = new ClassPathResource("properties/acquirer-config.properties");   //这里的填写的参数是配置文件的相对路径
            prop.load(new InputStreamReader(classPathResource.getInputStream(),"utf-8"));     //文件流的编码方式
        }catch (IOException e){
            e.printStackTrace();
        }
        String notifyurl = json.get("notifyurl").toString();//回调地址

        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.put("orgid", orgid);
        treeMap.put("appid", appid);
        treeMap.put("version", version);
        treeMap.put("randomstr", randomstr);
        treeMap.put("cusid", cusid);
        treeMap.put("orderid", orderid);
        treeMap.put("agreeid", agreeid);

        String amountStr=new BigDecimal(amount).multiply(new BigDecimal(100)).toString();
        treeMap.put("amount",amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr);
//    	treeMap.put("amount", amount.multiply(new BigDecimal(100)));//元转分

        treeMap.put("currency", currency);
        treeMap.put("subject", subject);
        if(trade.getRegisterInfoTable()!=null){
            RegisterInfoTable tradeObjectSquare = trade.getRegisterInfoTable();
            if(StringUtils.isNotBlank(tradeObjectSquare.getCity())){
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
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    private CrossResponseMsgDTO checkResult(String content, RequestCrossMsgDTO trade){
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        bankResult.setChannelResponseTime(new Date());
        if(StringUtils.isNotBlank(content)) {
            JSONObject json = JSON.parseObject(content);
            String retcode = json.getString("retcode");
            if(retcode .equals("SUCCESS")) {
                // 判断交易状态
                String trxstatus = json.get("trxstatus").toString();
                switch(trxstatus){
                    case "0000":
                        TreeMap<String,Object> result = JSON.parseObject(content, TreeMap.class);
                        String resultSign = result.get("sign").toString();
                        result.remove("sign");
                        String other = trade.getChannelInfoTable().getChannelParam();
                        JSONObject param = JSON.parseObject(other);
                        result.put("key", param.getString("key"));
                        String sign = AllinPayUtils.getMd5Sign(result);
                        if(sign.equalsIgnoreCase(sign)) {
                            bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                            bankResult.setCrossResponseMsg("交易成功");
                            bankResult.setChannelResponseMsg(content);
                            bankResult.setChannelOrderId(result.get("trxid").toString());
                        }else {
                            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                            bankResult.setCrossResponseMsg("交易异常:签名验证失败");
                            bankResult.setChannelResponseMsg(content);
                            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        }
                        break;
                    case "2000":
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossResponseMsg("支付交易请求已接受,等待交易结果通知。");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("交易异常:" + json.get("errmsg"));
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
        return bankResult;
    }
}
