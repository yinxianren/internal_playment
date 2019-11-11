package com.internal.playment.cross.impl.sicpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiBondCardCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.CryptoUtil;
import com.internal.playment.common.inner.HttpClient4Util;
import com.internal.playment.common.inner.StringUtils;
import com.internal.playment.common.inner.UUID;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.cross.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:24
 * Description:
 */

@Slf4j
@Service(version = "${application.version}", group = "sicPay"  , timeout = 30000 )
public class SicPayApiBondCardCrossComponentImpl implements ApiBondCardCrossComponent {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public CrossResponseMsgDTO bondCardApply(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        StringBuilder sBuilder = new StringBuilder();

        MerchantCardTable merchantCard = trade.getMerchantCardTable();

        String bankCardNo = merchantCard.getBankCardNum();
        Integer bankCardType = merchantCard.getBankCardType();
        String userId  =  bankCardNo;
        String basicInfoBackData = trade.getRegisterCollectTable().getChannelRespResult();
        Map<String, String> headMap = new HashMap<String, String>();
        Map<String, String> bodyMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(basicInfoBackData)) {
            try {
                Document doc = DocumentHelper.parseText(basicInfoBackData);//将xml转为dom对象
                Element root = doc.getRootElement();//获取根节点
                Element headElement = root.element("head");//获取名称为queryRequest的子节点
                List<Element> headElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : headElements) {  //遍历子元素
                    headElement = (Element) obj;
                    headMap.put(headElement.getName(), headElement.getTextTrim());//getName
                }
                Element bodyElement = root.element("body");//获取名称为queryRequest的子节点
                List<Element> bodyElements = bodyElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : bodyElements) {  //遍历子元素
                    bodyElement = (Element) obj;
                    bodyMap.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String childMerchantId = bodyMap.get("merchantId");
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP001</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + merchantCard.getId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<bankCardNo>"+ bankCardNo +"</bankCardNo>"); // 银行卡全卡号
        sBuilder.append("<accountName>" + merchantCard.getCardHolderName() +"</accountName>"); // 持卡人姓名
        sBuilder.append("<bankCardType>"+ "01"  +"</bankCardType>"); // 银行卡类型
        sBuilder.append("<certificateType>ZR01</certificateType>"); // 证件类型
        sBuilder.append("<certificateNo>"+ merchantCard.getIdentityNum()  +"</certificateNo>"); // 证件号码

        sBuilder.append("<mobilePhone>"+ merchantCard.getBankCardPhone() +"</mobilePhone>"); // 手机号码
        if (bankCardType == 2) {
            sBuilder.append("<valid>" + merchantCard.getValidDate() +"</valid>"); // 有效期
            sBuilder.append("<cvn2>"+ merchantCard.getSecurityCode() +"</cvn2>"); // 安全码后三位
            sBuilder.append("<bankCardType>"+ "02"  +"</bankCardType>"); // 银行卡类型
        }
        sBuilder.append("<terminalId>"+ param.get("terminalId") +"</terminalId>"); // 商户终端号
        sBuilder.append("<userId>"+ userId +"</userId>"); // 商户用户标识
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 进件返回的子商户号
        sBuilder.append("<backUrl>"+ "http://boc.cmtbuy.com/cross/sicPay/bondCardNofityUrl" +"</backUrl>"); //
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通鉴权绑卡业务请求报文："+plainXML);
        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = AllinPayUtils.getRandomSecretkey();
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP001"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));
        log.info("高汇通鉴权绑卡业务请求地址："+param.get("bindCardUrl").toString());
        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    @Override
    public CrossResponseMsgDTO reGetBondCode(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        StringBuilder sBuilder = new StringBuilder();

        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        String userId  =  merchantCard.getBankCardNum();
        String basicInfoBackData = trade.getRegisterCollectTable().getChannelRespResult();
//        Map<String, String> headMap = new HashMap<String, String>();
//        Map<String, String> bodyMap = new HashMap<String, String>();
        String childMerchantId = null;
        if(StringUtils.isNotBlank(basicInfoBackData)) {
            childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
//            try {
//                Document doc = DocumentHelper.parseText(basicInfoBackData);//将xml转为dom对象
//                Element root = doc.getRootElement();//获取根节点
//                Element headElement = root.element("head");//获取名称为queryRequest的子节点
//                List<Element> headElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
//                for (Object obj : headElements) {  //遍历子元素
//                    headElement = (Element) obj;
//                    headMap.put(headElement.getName(), headElement.getTextTrim());//getName
//                }
//                Element bodyElement = root.element("body");//获取名称为queryRequest的子节点
//                List<Element> bodyElements = bodyElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
//                for (Object obj : bodyElements) {  //遍历子元素
//                    bodyElement = (Element) obj;
//                    bodyMap.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP010</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<userId>"+ userId +"</userId>"); // 商户用户标识
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
        sBuilder.append("<terminalId>"+ param.get("terminalId") +"</terminalId>"); // 商户终端号
        sBuilder.append("<oriReqMsgId>"+ merchantCard.getId() +"</oriReqMsgId>"); // 原订单号
        sBuilder.append("<accountName>" + merchantCard.getCardHolderName() +"</accountName>"); // 持卡人姓名
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通短信发送业务请求报文："+plainXML);
        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = AllinPayUtils.getRandomSecretkey();
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP010"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    @Override
    public CrossResponseMsgDTO confirmBondCard(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        StringBuilder sBuilder = new StringBuilder();

        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        JSONObject result = JSONObject.parseObject(merchantCard.getChannelRespResult());
        String userId  =  merchantCard.getBankCardNum();

        String basicInfoBackData = trade.getRegisterCollectTable().getChannelRespResult();
        Map<String, String> headMap = new HashMap<String, String>();
        Map<String, String> bodyMap = new HashMap<String, String>();
        if(StringUtils.isNotBlank(basicInfoBackData)) {

            try {
                Document doc = DocumentHelper.parseText(basicInfoBackData);//将xml转为dom对象
                Element root = doc.getRootElement();//获取根节点
                Element headElement = root.element("head");//获取名称为queryRequest的子节点
                List<Element> headElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : headElements) {  //遍历子元素
                    headElement = (Element) obj;
                    headMap.put(headElement.getName(), headElement.getTextTrim());//getName
                }
                Element bodyElement = root.element("body");//获取名称为queryRequest的子节点
                List<Element> bodyElements = bodyElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : bodyElements) {  //遍历子元素
                    bodyElement = (Element) obj;
                    bodyMap.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String childMerchantId = bodyMap.get("merchantId");

        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP011</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createKey("merchant_card")  + "</reqMsgId>"); //订单号

        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<bindOrderNo>"+ result.get("bindOrderNo") +"</bindOrderNo>"); // 绑卡订单号
        sBuilder.append("<userId>"+ userId +"</userId>"); // 商户用户标识
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
        sBuilder.append("<oriReqMsgId>"+ result.get("reqMsgId") +"</oriReqMsgId>"); // 原订单号
        sBuilder.append("<validateCode>"+ merchantCard.getSmsCode() +"</validateCode>"); //短信验证码
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通绑卡确认业务请求报文："+plainXML);
        byte[] plainBytes = plainXML.getBytes("UTF-8");
        String keyStr = AllinPayUtils.getRandomSecretkey();
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP011"));
        nvps.add(new BasicNameValuePair("callBack", "http://boc.cmtbuy.com/cross/sicPay/bondCardNofityUrl"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: " + t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    /**
     * 绑卡结果处理
     * @param content
     * @param trade
     * @return
     */
    @SuppressWarnings("unchecked")
    public CrossResponseMsgDTO checkResult(String content, RequestCrossMsgDTO trade) throws  Exception{
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
                List<Element> bodyElements = bodyElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : bodyElements) {  //遍历子元素
                    bodyElement = (Element) obj;
                    bodyMap.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String respType = headMap.get("respType");
            String tranCode = headMap.get("tranCode");
            String reqMsgId = headMap.get("reqMsgId");
            switch (tranCode) {
                case "IFP001":
                    if (respType.equals("S"))
                    {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                                bankResult.setChannelOrderId(payMsgId);
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("绑卡成功");
                                bankResult.setChannelResponseMsg(content);
                                Map map = new HashMap();
                                map.put("bindId",bodyMap.get("bindId"));
                                bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                                break;
                            case "100001":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("报文不合法");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            case "100003":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("验证签名失败");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            default:
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("请求失败:" + headMap.get("respMsg"));
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                        }

                    } else if(respType.equals("R")){
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                                bankResult.setChannelOrderId(payMsgId);
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("绑卡请求成功");
                                bankResult.setChannelResponseMsg(content);
                                trade.getMerchantCardTable().setId(Long.valueOf(reqMsgId));
                                bankResult = reGetBondCode(trade);
                                bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                                break;
                            case "100001":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("报文不合法");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            case "100003":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("验证签名失败");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            default:
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("请求失败:" + headMap.get("respMsg"));
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;

                        }


                    }  else{
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("请求绑卡失败:" + headMap.get("respMsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    }
                    break;
                case "IFP010":
                    if (respType.equals("S")) {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                                bankResult.setChannelOrderId(payMsgId);
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("短信发送成功");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                                break;
                            case "100001":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("报文不合法");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            case "100003":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("验证签名失败");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            default:
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("短信发送失败:" + headMap.get("respMsg"));
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                        }

                    } else {
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("请求短信发送失败:" + headMap.get("respMsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    }
                    break;
                case "IFP011":
                    if (respType.equals("S")) {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                String bindId = headMap.get("bindId");
                                bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                                bankResult.setChannelOrderId(payMsgId);
                                bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                bankResult.setCrossResponseMsg("短信验证成功");
                                bankResult.setChannelResponseMsg(content);;
                                bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                                break;
                            case "100001":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("报文不合法");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            case "100003":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("验证签名失败");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            default:
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("短信验证失败:" + headMap.get("respMsg"));
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                        }

                    } else if(respType.equals("R")){
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "0000":
                                String respDate = headMap.get("respDate");
                                String payMsgId = headMap.get("payMsgId");
                                bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                                bankResult.setChannelOrderId(payMsgId);
                                bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                                bankResult.setCrossResponseMsg("短信验证中");
                                bankResult.setChannelResponseMsg(content);
                                bankResult = new ApiSicPayOtherBusinessCrossComponentImpl().queryBondCard(trade);
                                bankResult.setCrossStatusMsg(StatusEnum._3.getRemark());
                                break;
                            case "100001":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("报文不合法");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            case "100003":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("验证签名失败");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            default:
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("短信验证失败:" + headMap.get("respMsg"));
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;

                        }


                    }  else  {
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("短信验证失败:" + headMap.get("respMsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    }
                    break;
                case "IFP023":
                    if (respType.equals("S")) {
                        // 判断交易状态
                        String respCode = headMap.get("respCode");
                        switch (respCode) {
                            case "000000":
                                String oriRespMsg = bodyMap.get("oriRespMsg");
                                String oriRespCode = bodyMap.get("oriRespCode");
                                String respDate = headMap.get("respDate");
                                Map map = new HashMap();
                                if (oriRespCode.equals("<![CDATA[000000]]>")){
                                    bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                                    bankResult.setCrossResponseMsg("绑卡成功");
                                    bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                                    map.put("bindId",headMap.get("bindId"));
                                }else {
                                    bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                    bankResult.setCrossResponseMsg("绑卡失败："+oriRespMsg);
                                    bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                    bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                    bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                }
                                bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                                bankResult.setChannelResponseMsg(content);
                                map.put("reqMsgId",headMap.get("reqMsgId"));
                                break;
                            case "100001":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("报文不合法");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            case "100003":
                                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                                bankResult.setCrossResponseMsg("签名失败");
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                            default:
                                bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                                bankResult.setCrossResponseMsg("绑卡结果处理中:" + headMap.get("respMsg"));
                                bankResult.setChannelResponseMsg(content);
                                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                                break;
                        }

                    } else {
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossResponseMsg("绑卡查询失败:" + headMap.get("respMsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                    }
                    break;
                default:
            }
        }else{
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("绑卡失败：支付返回结果为空！");
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
        }
        return bankResult;
    }
}
