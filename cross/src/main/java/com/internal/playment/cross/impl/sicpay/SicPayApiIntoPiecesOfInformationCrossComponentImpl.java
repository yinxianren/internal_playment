package com.internal.playment.cross.impl.sicpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.ApiIntoPiecesOfInformationCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.CryptoUtil;
import com.internal.playment.common.inner.HttpClient4Util;
import com.internal.playment.common.inner.StringUtils;
import com.internal.playment.common.inner.UUID;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
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

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
@Service(version = "${application.version}", group = "sicPay"  , timeout = 30000 )
public class SicPayApiIntoPiecesOfInformationCrossComponentImpl implements ApiIntoPiecesOfInformationCrossComponent {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public CrossResponseMsgDTO addCusInfo(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        RegisterInfoTable merchantRegisterInfo = trade.getRegisterInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        RegisterCollectTable registerCollectTable = trade.getRegisterCollectTable();

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>100001</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createId() + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<handleType>0</handleType>"); //操作类型 0新增，1修改
        sBuilder.append("<merchantName>"+merchantRegisterInfo.getTerminalMerName()+"</merchantName>"); //商户名称
        sBuilder.append("<shortName>"+merchantRegisterInfo.getUserShortName()+"</shortName>"); //商户简称
        sBuilder.append("<city>"+merchantRegisterInfo.getCity()+"</city>"); //商户城市
        sBuilder.append("<merchantAddress>"+merchantRegisterInfo.getAddress()+"</merchantAddress>"); //商户地址
        sBuilder.append("<servicePhone>"+merchantRegisterInfo.getPhone()+"</servicePhone>"); //客服电话
        sBuilder.append("<merchantType>"+merchantRegisterInfo.getMerchantType()+"</merchantType>"); //商户类型，00公司商户，01个体商户
        sBuilder.append("<category>"+registerCollectTable.getCategory()+"</category>"); //经营类目代码
        sBuilder.append("<corpmanName>"+registerCollectTable.getCardHolderName()+"</corpmanName>"); //法人姓名
        sBuilder.append("<corpmanId>"+merchantRegisterInfo.getIdentityNum()+"</corpmanId>"); //法人身份证
        sBuilder.append("<corpmanMobile>"+merchantRegisterInfo.getPhone()+"</corpmanMobile>"); //法人联系手机
        sBuilder.append("<bankCode>"+registerCollectTable.getBankCode()+"</bankCode>"); //银行代码
//        sBuilder.append("<bankName>"+registerCollectTable.getbankName+"</bankName>"); //开户行全称
        sBuilder.append("<bankaccountNo>"+registerCollectTable.getBankCardNum()+"</bankaccountNo>"); //开户行账号
        sBuilder.append("<bankaccountName>"+registerCollectTable.getCardHolderName()+"</bankaccountName>"); //开户户名
        sBuilder.append("<autoCus>0</autoCus>"); //自动提现
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
//        sBuilder.append("<bankaccountName>0</bankaccountName>"); //
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通商户登记请求报文：{}",plainXML);

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
        nvps.add(new BasicNameValuePair("tranCode", "100001"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("basicInfoUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: {}" ,t.respDecryption(response));
        return  checkResult(t.respDecryption(response),trade);
    }

    @Override
    public CrossResponseMsgDTO bankCardBind(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        RegisterInfoTable merchantRegisterInfo = trade.getRegisterInfoTable();
        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        StringBuilder sBuilder = new StringBuilder();
        JSONObject param = JSONObject.parseObject(extraChannelInfo.getChannelParam());
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>100002</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + merchantRegisterCollect.getMerOrderId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<merchantId>"+JSONObject.parseObject(merchantRegisterCollect.getChannelRespResult()).getString("bankData")+"</merchantId>"); //子商户编码
        sBuilder.append("<mobileNo2>"+merchantRegisterInfo.getPhone()+"</mobileNo2>"); //持卡人手机号
        sBuilder.append("<handleType>0</handleType>"); //操作类型 0新增 1删除 2修改
        sBuilder.append("<bankCode>"+merchantRegisterCollect.getBankCode()+"</bankCode>"); //银行代码
        if (merchantRegisterInfo.getMerchantType().equals("01")){
            sBuilder.append("<bankaccProp>"+0+"</bankaccProp>"); //账户属性 0私人 1公司
        } else {
            sBuilder.append("<bankaccProp>"+1+"</bankaccProp>"); //账户属性 0私人 1公司
        }

        sBuilder.append("<name>"+merchantRegisterInfo.getUserName()+"</name>"); //持卡人姓名
        sBuilder.append("<bankaccountNo>"+merchantRegisterCollect.getBankCardNum()+"</bankaccountNo>"); //银行卡号
        sBuilder.append("<bankaccountType>"+merchantRegisterCollect.getBankCardType()+"</bankaccountType>"); //银行卡类型
        sBuilder.append("<certCode>"+merchantRegisterInfo.getIdentityType()+"</certCode>"); //办卡证件类型
        sBuilder.append("<certNo>"+merchantRegisterInfo.getIdentityNum()+"</certNo>"); //证件号码
//        sBuilder.append("<defaultAcc>0</defaultAcc>"); //是否默认账户 0否
//        sBuilder.append("<province>0</province>"); // 省
//        sBuilder.append("<city>0</city>"); // 市
//        sBuilder.append("<bankBranchName>0</bankBranchName>"); // 支行名称
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通银行卡登记请求报文：{}",plainXML);

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
        nvps.add(new BasicNameValuePair("tranCode", "100002"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bankInfoUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: {}" ,t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    @Override
    public CrossResponseMsgDTO serviceFulfillment(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();

        RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>100003</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + merchantRegisterCollect.getMerOrderId()  + "</reqMsgId>"); //订单号
        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        sBuilder.append("<body>");
        sBuilder.append("<merchantId>"+JSONObject.parseObject(merchantRegisterCollect.getChannelRespResult()).getString("bankData")+"</merchantId>"); // 子商户号
        sBuilder.append("<handleType>0</handleType>"); // 操作类型
        sBuilder.append("<cycleValue>1</cycleValue>"); // 结算周期
        sBuilder.append("<busiList>");
        sBuilder.append("<busiCode>B00108</busiCode>"); // 开通业务
//        sBuilder.append("<rateList>");
        sBuilder.append("<futureRateType>1</futureRateType>"); //费率类型 1百分比 2单笔
        sBuilder.append("<futureRateValue>"+merchantRegisterCollect.getPayFee()+"</futureRateValue>"); // 费率
//        sBuilder.append("</rateList>");
//        sBuilder.append("<certNo>0</certNo>"); //
//        sBuilder.append("<certNo>0</certNo>"); //
//        sBuilder.append("<certNo>0</certNo>"); //
        sBuilder.append("</busiList>");

        sBuilder.append("<busiList>");
        sBuilder.append("<busiCode>B00302</busiCode>"); // 开通业务
        sBuilder.append("<futureRateType>1</futureRateType>"); //费率类型 1百分比 2单笔
        sBuilder.append("<futureRateValue>"+merchantRegisterCollect.getBackFee()+"</futureRateValue>"); // 费率
        sBuilder.append("</busiList>");


        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通开通业务请求报文：{}",plainXML);

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
        nvps.add(new BasicNameValuePair("tranCode", "100003"));
        nvps.add(new BasicNameValuePair("callBack", "http://www.baidu.com"));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("busiInfoUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: {}" , t.respDecryption(response));
        return checkResult(t.respDecryption(response),trade);
    }

    /**
     * 交易结果处理
     * @param content
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */

    private CrossResponseMsgDTO checkResult(String content, RequestCrossMsgDTO trade) throws ParseException {
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
            if(respType.equals("S")) {
                // 判断交易状态
                String respCode = headMap.get("respCode");
                switch(respCode){
                    case "000000":
                        String respDate = headMap.get("respDate");
                        String payMsgId = headMap.get("payMsgId");
                        bankResult.setChannelResponseTime(dateFormat.parse(respDate));
                        bankResult.setChannelOrderId(payMsgId);
                        bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                        bankResult.setCrossStatusMsg("交易成功");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setCrossStatusMsg(StatusEnum._0.getRemark());
                        break;
                    case "100001":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg("报文不合法");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        break;
                    case "100003":
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg("交易失败");
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        break;
                    default:
                        bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                        bankResult.setCrossStatusMsg("交易失败:" + headMap.get("respMsg"));
                        bankResult.setChannelResponseMsg(content);
                        bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                        bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                        bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
                        break;
                }

            }else {
                bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                bankResult.setCrossStatusMsg("交易失败:" + headMap.get("respMsg"));
                bankResult.setChannelResponseMsg(content);
                bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
            }
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossStatusMsg("交易失败：支付返回结果为空！");
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setCrossStatusMsg(StatusEnum._1.getRemark());
        }
        return bankResult;
    }
}
