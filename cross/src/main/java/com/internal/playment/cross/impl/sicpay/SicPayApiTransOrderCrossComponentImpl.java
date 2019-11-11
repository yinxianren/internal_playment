package com.internal.playment.cross.impl.sicpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiTransOrderCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.CryptoUtil;
import com.internal.playment.common.inner.HttpClient4Util;
import com.internal.playment.common.inner.StringUtils;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
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

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:31
 * Description:
 */
@Slf4j
@Service(version = "${application.version}", group = "sicPay"  , timeout = 30000 )
public class SicPayApiTransOrderCrossComponentImpl implements ApiTransOrderCrossComponent {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO trade) throws Exception {
        ChannelInfoTable channelInfo =trade.getChannelInfoTable();
        JSONObject others = JSON.parseObject(channelInfo.getChannelParam());

        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        PayOrderInfoTable payOrder = trade.getPayOrderInfoTable();
        TransOrderInfoTable transOrder = trade.getTransOrderInfoTable();
        String result = trade.getRegisterCollectTable().getChannelRespResult();
        String childMerchantId = JSONObject.parseObject(result).getString("bankData");
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");

        sBuilder.append("<head>");
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>"+ others.get("agencyId")+"</agencyId>");// 商户标识
        sBuilder.append("<msgType>01</msgType>");  //报文类型
        sBuilder.append("<tranCode>IFP021</tranCode>");
        sBuilder.append("<reqMsgId>" + transOrder.getPlatformOrderId()  + "</reqMsgId>");// 请求交易流水号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");// 请求日期时间
        sBuilder.append("</head>");

        sBuilder.append("<body>");
        sBuilder.append("<terminalId>"+ others.get("terminalId")+"</terminalId>"); //终端号
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
        sBuilder.append("<bindId>" + JSONObject.parseObject(trade.getMerchantCardTable().getChannelRespResult()).getString("bindId")+ "</bindId>");//绑卡id
        sBuilder.append("<payNos>"+ payOrder.getChannelOrderId()+"</payNos>"); //平台交易订单号
        sBuilder.append("<merDfAmount>"+ transOrder.getAmount().multiply(new BigDecimal(100))+"</merDfAmount>"); //代付金额
        sBuilder.append("<bankCardNo>"+ transOrder.getBankCardNum() +"</bankCardNo>");//代付卡号
        sBuilder.append("<accountName>"+ trade.getMerchantCardTable().getCardHolderName() +"</accountName>"); //收款人姓名
        sBuilder.append("<remark>"+ "备注" +"</remark>");
        sBuilder.append("<accountProp>1</accountProp>");//代付类型
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");

        String plainXML = sBuilder.toString();
        System.out.println("请求报文："+plainXML);
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
        nvps.add(new BasicNameValuePair("signData", signData));
        nvps.add(new BasicNameValuePair("agencyId", String.valueOf(others.get("agencyId"))));
        nvps.add(new BasicNameValuePair("tranCode", "IFP021"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(String.valueOf(others.get("repayUrl")), null, nvps, null);

        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        System.out.println("明文结果: " + t.respDecryption(response));

        return checkResult(t.respDecryption(response),trade);
    }

    public static String getRandomSecretkey(int length) {
        String val = "";
        Random random = new Random();
//        int length = 16; //length为几位密码
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
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
//                        bankResult.setChannelOrderId(payMsgId);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                        bankResult.setCrossResponseMsg("交易成功");
                        bankResult.setChannelResponseMsg(content);
                        bankResult =  payment(trade);
                        bankResult.setChannelOrderId(headMap.get("reqMsgId"));

                        break;
                    case "100001":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("报文不合法");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                    case "100003":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("验证签名失败");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        bankResult.setCrossResponseMsg("交易失败:" + headMap.get("respMsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        break;
                }

            }else if(respType.equals("R")){
                bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                bankResult.setChannelResponseMsg("交易处理中:" + headMap.get("respMsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            }
            else{
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                bankResult.setCrossResponseMsg("交易失败:" + headMap.get("respMsg"));
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
        return bankResult;
    }
}
