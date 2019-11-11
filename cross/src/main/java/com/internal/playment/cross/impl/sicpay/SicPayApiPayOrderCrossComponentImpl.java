package com.internal.playment.cross.impl.sicpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiPayOrderCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.CryptoUtil;
import com.internal.playment.common.inner.HttpClient4Util;
import com.internal.playment.common.inner.StringUtils;
import com.internal.playment.common.inner.UUID;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.cross.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:28
 * Description:
 */
@Slf4j
@Service(version = "${application.version}", group = "sicPay"  , timeout = 30000 )
public class SicPayApiPayOrderCrossComponentImpl implements ApiPayOrderCrossComponent {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String[] type = new String[]{"1","2","4"};
    
    @Override
    public CrossResponseMsgDTO payApply(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        JSONObject param = JSON.parseObject(channelInfo.getChannelParam());
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        String amountStr = payOrder.getAmount().multiply(new BigDecimal(100)).toString();
        amountStr = amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr;
        String basicInfoBackData = trade.getRegisterCollectTable().getChannelRespResult();
//        Map<String, String> headMap = new HashMap<String, String>();
//        Map<String, String> bodyMap = new HashMap<String, String>();
        String childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP004</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + payOrder.getPlatformOrderId() + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<terminalId>" + param.getString("terminalId") + "</terminalId>");//商户终端号
        sBuilder.append("<userId>"+merchantCard.getBankCardNum()+"</userId>");//商户用户 标识
        sBuilder.append("<bindId>" + JSONObject.parseObject(merchantCard.getChannelRespResult()).getString("bindId")+ "</bindId>");//绑卡id
        sBuilder.append("<childMerchantId>" + childMerchantId + "</childMerchantId>");//子商户
        sBuilder.append("<currency>156</currency>");//交易币种
        sBuilder.append("<valid>"+payOrder.getValidDate()+"</valid>");//
        sBuilder.append("<cvn2>"+payOrder.getSecurityCode()+"</cvn2>");//
        sBuilder.append("<reckonCurrency>156</reckonCurrency>");//清算币种
        sBuilder.append("<amount>"+amountStr+"</amount>");//交易金额
        sBuilder.append("<productCategory>05</productCategory>");//商品类别
        sBuilder.append("<productName>订单支付</productName>");//商品描述
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通支付请求报文："+plainXML);

        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = getRandomSecretkey(16);
        byte[] keyBytes = keyStr.getBytes("UTF-8");
        byte[] base64EncryptDataBytes = Base64.encodeBase64(CryptoUtil.AESEncrypt(plainBytes, keyBytes, "AES", "AES/ECB/PKCS5Padding", null));
        String encryptData = new String(base64EncryptDataBytes, "UTF-8");
        byte[] base64SingDataBytes = Base64.encodeBase64(CryptoUtil.digitalSign(plainBytes, hzfPriKey, "SHA1WithRSA"));
        String signData = new String(base64SingDataBytes, "UTF-8");
        byte[] base64EncyrptKeyBytes = Base64.encodeBase64(CryptoUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding"));
        String encrtptKey = new String(base64EncyrptKeyBytes, "UTF-8");

        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        nvps.add(new BasicNameValuePair("encryptData", encryptData));
        nvps.add(new BasicNameValuePair("encryptKey", encrtptKey));
        nvps.add(new BasicNameValuePair("agencyId", param.get("agencyId").toString()));
        nvps.add(new BasicNameValuePair("signData", signData));
        nvps.add(new BasicNameValuePair("tranCode", "IFP004"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("applyUrl").toString(), null, nvps, null);

        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: " + t.respDecryption(response));

        return checkResult(t.respDecryption(response),trade);
    }

    @Override
    public CrossResponseMsgDTO getPayCode(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        JSONObject param = JSON.parseObject(channelInfo.getChannelParam());
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        String basicInfoBackData = trade.getRegisterCollectTable().getChannelRespResult();
        String childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP012</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createKey("pay_order")  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
//        sBuilder.append("<terminalId>" + param.get("terminalId") + "</terminalId>");//商户终端号
        sBuilder.append("<userId>"+merchantCard.getBankCardNum()+"</userId>");//商户用户 标识
        sBuilder.append("<oriReqMsgId>" + payOrder.getPlatformOrderId() + "</oriReqMsgId>");//商户交易流水号
        sBuilder.append("<childMerchantId>" + childMerchantId + "</childMerchantId>");//子商户
//        sBuilder.append("<productName>订单支付</productName>");//商品描述
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通支付短信请求报文："+plainXML);

        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = getRandomSecretkey(16);
        byte[] keyBytes = keyStr.getBytes("UTF-8");
        byte[] base64EncryptDataBytes = Base64.encodeBase64(CryptoUtil.AESEncrypt(plainBytes, keyBytes, "AES", "AES/ECB/PKCS5Padding", null));
        String encryptData = new String(base64EncryptDataBytes, "UTF-8");
        byte[] base64SingDataBytes = Base64.encodeBase64(CryptoUtil.digitalSign(plainBytes, hzfPriKey, "SHA1WithRSA"));
        String signData = new String(base64SingDataBytes, "UTF-8");
        byte[] base64EncyrptKeyBytes = Base64.encodeBase64(CryptoUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding"));
        String encrtptKey = new String(base64EncyrptKeyBytes, "UTF-8");

        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        nvps.add(new BasicNameValuePair("encryptData", encryptData));
        nvps.add(new BasicNameValuePair("encryptKey", encrtptKey));
        nvps.add(new BasicNameValuePair("agencyId", param.get("agencyId").toString()));
        nvps.add(new BasicNameValuePair("signData", signData));
        nvps.add(new BasicNameValuePair("tranCode", "IFP012"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("applyUrl").toString(), null, nvps, null);

        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: " + t.respDecryption(response));

        return checkResult(t.respDecryption(response),trade);
    }

    @Override
    public CrossResponseMsgDTO confirmPay(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        JSONObject param = JSON.parseObject(channelInfo.getChannelParam());
        String basicInfoBackData = trade.getRegisterCollectTable().getChannelRespResult();
        String childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP013</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + getRandomSecretkey(16)  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<childMerchantId>" + childMerchantId + "</childMerchantId>");//子商户号
        sBuilder.append("<userId>"+trade.getMerchantCardTable().getBankCardNum()+"</userId>");//商户用户 标识
        sBuilder.append("<oriReqMsgId>" + payOrder.getPlatformOrderId() + "</oriReqMsgId>");//平台交易流水号
        sBuilder.append("<validateCode>"+payOrder.getSmsCode()+"</validateCode>");//验证码
        sBuilder.append("<deviceType>"+type[new Random().nextInt(3)]+"</deviceType>");//设备类型
        sBuilder.append("<deviceId>"+getRandomSecretkey(15)+"</deviceId>");//移动终端设备的唯一标识
        sBuilder.append("<userIP>"+trade.getIP()+"</userIP>");//客户端IP
//        sBuilder.append("<productName>订单支付</productName>");//商品描述
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通确认支付请求报文："+plainXML);

        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = getRandomSecretkey(16);
        byte[] keyBytes = keyStr.getBytes("UTF-8");
        byte[] base64EncryptDataBytes = Base64.encodeBase64(CryptoUtil.AESEncrypt(plainBytes, keyBytes, "AES", "AES/ECB/PKCS5Padding", null));
        String encryptData = new String(base64EncryptDataBytes, "UTF-8");
        byte[] base64SingDataBytes = Base64.encodeBase64(CryptoUtil.digitalSign(plainBytes, hzfPriKey, "SHA1WithRSA"));
        String signData = new String(base64SingDataBytes, "UTF-8");
        byte[] base64EncyrptKeyBytes = Base64.encodeBase64(CryptoUtil.RSAEncrypt(keyBytes, yhPubKey, 2048, 11, "RSA/ECB/PKCS1Padding"));
        String encrtptKey = new String(base64EncyrptKeyBytes, "UTF-8");

        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        nvps.add(new BasicNameValuePair("encryptData", encryptData));
        nvps.add(new BasicNameValuePair("encryptKey", encrtptKey));
        nvps.add(new BasicNameValuePair("agencyId", param.get("agencyId").toString()));
        nvps.add(new BasicNameValuePair("signData", signData));
        nvps.add(new BasicNameValuePair("tranCode", "IFP013"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("applyUrl").toString(), null, nvps, null);

        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: " + t.respDecryption(response));

        return checkResult(t.respDecryption(response),trade);
    }

    private static String getRandomSecretkey(int length) {
        String val = "";
        Random random = new Random();
//        int length = 16; //length为几位密码
        for (int i = 0; i < length; i++) {
            String charOrNum = "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 交易结果处理
     * @param content
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */

    private CrossResponseMsgDTO checkResult(String content, RequestCrossMsgDTO trade) throws Exception {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
        if(StringUtils.isNotBlank(content)) {
            Map<String, String> headMap = new HashMap<String, String>();
            Map<String, String> bodyMap = new HashMap<String, String>();
            try {
                Document doc = DocumentHelper.parseText(content);//将xml转为dom对象
                Element root = doc.getRootElement();//获取根节点
                Element headElement = root.element("head");//获取名称为queryRequest的子节点
                List<Element> headElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : headElements) {  //遍历子元素
                    headElement = (Element) obj;
                    headMap.put(headElement.getName(), headElement.getTextTrim());//getName
                }
                Element bodyElement = root.element("body");//获取名称为queryRequest的子节点
                List<Element> bodyElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : bodyElements) {  //遍历子元素
                    bodyElement = (Element) obj;
                    bodyMap.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String respType = headMap.get("respType");
            if(respType.equals("S")) {
                // 判断交易状态
                String respCode = headMap.get("respCode");
                String respDate = headMap.get("respDate");
                bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                switch(respCode){
                    case "000000":
                        String payMsgId = headMap.get("payMsgId");
                        String reqMsgId = headMap.get("reqMsgId");
                        bankResult.setChannelOrderId(payMsgId);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                        bankResult.setCrossStatusMsg("交易成功");
                        bankResult.setChannelResponseMsg(content);
                        if (headMap.get("tranCode").equals("IFP004")){
//                            bankResult =  applySMS(trade);
                            bankResult.setChannelOrderId(reqMsgId);
                            bankResult.setCrossStatusMsg("交易申请成功");
                        }else if (headMap.get("tranCode").equals("IFP012")){
                            bankResult.setChannelOrderId(headMap.get("reqMsgId"));
                        }else {
                            bankResult.setChannelOrderId(headMap.get("payMsgId"));
                        }
                        break;
                    case "100001":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossStatusMsg("报文不合法");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    case "100003":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossStatusMsg("验证签名失败");
                        bankResult.setChannelResponseMsg(content);
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg("交易失败:" + headMap.get("respMsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        break;
                }

            }else if(respType.equals("R")){
                bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                bankResult.setCrossStatusMsg("交易处理中:" + headMap.get("respMsg"));
                bankResult.setChannelOrderId(headMap.get("payMsgId"));
                bankResult.setChannelResponseMsg(content);
            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossStatusMsg("交易失败:" + headMap.get("respMsg"));
                bankResult.setChannelResponseMsg(content);
            }
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            bankResult.setCrossStatusMsg("交易失败：支付返回结果为空！");
        }
        return bankResult;
    }
}
