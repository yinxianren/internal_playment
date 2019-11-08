package com.internal.playment.cross.impl.sicpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.internal.playment.api.cross.sicpay.ApiSicPayOtherBusinessCrossComponent;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.inner.CryptoUtil;
import com.internal.playment.common.inner.HttpClient4Util;
import com.internal.playment.common.inner.UUID;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.cross.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:42
 * Description:
 */
@Slf4j
@Service(version = "${application.version}", group = "sicPay" , timeout = 30000 )
public class ApiSicPayOtherBusinessCrossComponentImpl implements ApiSicPayOtherBusinessCrossComponent {

    @Override
    public CrossResponseMsgDTO queryBondCard(RequestCrossMsgDTO trade) throws Exception {
        //公钥
        PublicKey yhPubKey = SicPayRSAUtils.getPublicKey();
        //私钥
        PrivateKey hzfPriKey = SicPayRSAUtils.getPrivateKey();
        ChannelExtraInfoTable extraChannelInfo = trade.getChannelExtraInfoTable();
        JSONObject param = JSON.parseObject(extraChannelInfo.getChannelParam());
        StringBuilder sBuilder = new StringBuilder();
        MerchantCardTable merchantCard = trade.getMerchantCardTable();
        JSONObject result = JSONObject.parseObject(merchantCard.getChannelRespResult());
        sBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sBuilder.append("<merchant>");
        sBuilder.append("<head>");
        //公共参数
        sBuilder.append("<version>2.0.0</version>");
        sBuilder.append("<agencyId>" + param.get("agencyId") + "</agencyId>"); //平台商户标识
        sBuilder.append("<msgType>01</msgType>"); //报文类型，01 商户请求报文
        sBuilder.append("<tranCode>IFP023</tranCode>"); //服务交易码
        sBuilder.append("<reqMsgId>" + UUID.createKey("merchant_card")  + "</reqMsgId>"); //订单号

        sBuilder.append("<reqDate>" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "</reqDate>");//请求日期
        sBuilder.append("</head>");
        //私有参数
        String basicInfoBackData = trade.getRegisterCollectTable().getChannelRespResult();
        String childMerchantId = JSONObject.parseObject(basicInfoBackData).getString("bankData");
        sBuilder.append("<body>");
        sBuilder.append("<oriReqMsgId>"+ merchantCard.getId() +"</oriReqMsgId>"); // 原订单号
        sBuilder.append("<childMerchantId>"+ childMerchantId +"</childMerchantId>"); // 子商户号
        sBuilder.append("<bindOrderNo>"+ result.getString("bindOrderNo") +"</bindOrderNo>"); //
        sBuilder.append("</body>");
        sBuilder.append("</merchant>");
        String plainXML = sBuilder.toString();
        log.info("高汇通绑卡业务查询请求报文："+plainXML);
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
        nvps.add(new BasicNameValuePair("tranCode", "IFP023"));
        nvps.add(new BasicNameValuePair("callBack", param.get("callBackUrl").toString()));

        HttpClient4Util httpClient4Util = new HttpClient4Util();
        byte[] retBytes = httpClient4Util.doPost(param.get("bindCardUrl").toString(), null, nvps, null);
        String response = new String(retBytes, "UTF-8");
        System.out.println("回调结果： " + new String(retBytes, "UTF-8"));
        SicEncype t = new SicEncype();
        log.info("明文结果: " + t.respDecryption(response));
        return new SicPayApiBondCardCrossComponentImpl().checkResult(t.respDecryption(response),trade);
    }
}
