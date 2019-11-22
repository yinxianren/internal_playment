package com.internal.playment.pay.service.shortcut.impl;

import com.alibaba.fastjson.JSON;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerNoAuthPayOrderApplyDTO;
import com.internal.playment.common.dto.MerPayOrderApplyDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.*;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.inner.UUID;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.channel.ChannelHistoryTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.internal.playment.common.table.system.BankRateTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.RiskQuotaTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple4;
import com.internal.playment.pay.service.CommonServiceAbstract;
import com.internal.playment.pay.service.shortcut.NewPayOrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午6:24
 * Description:
 */
@Service
public class NewPayOrderServiceImpl  extends CommonServiceAbstract implements NewPayOrderService {

    private final static Object lock = new Object();
    private static AtomicInteger count = new AtomicInteger(0) ;
    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) {
        Tuple4<ChannelInfoTable, PayOrderInfoTable, RegisterCollectTable, MerchantCardTable> tuple4 = (Tuple4<ChannelInfoTable, PayOrderInfoTable, RegisterCollectTable, MerchantCardTable>) tuple;
        return new RequestCrossMsgDTO()
                .setChannelInfoTable(tuple4._1) //通道信息
                .setPayOrderInfoTable(tuple4._2)//订单信息
                .setRegisterCollectTable(tuple4._3)
                .setMerchantCardTable(tuple4._4);
    }

    @Override
    public Map<String, ParamRule> getParamMapByB7() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 64));//产品类型		否	64
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("productCategory", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 商品类别	参考附件：商户入住字典表	否	32
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));// 商户订单号
                put("currency", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));// 交易的币种
                put("amount", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 12));// 支付金额
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
//                put("validDate", new ParamRule(ParamTypeEnum.STRING.getType(), 4, 4));// 有效期	信用卡必填，格式：MMYY	选填	4
                put("payFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 8));//扣款手续费		否	8
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 城市
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
                put("deviceId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//交易设备号	交易设备号 (包含但不限于POS终端号或者手机IMEI) 字符串类型，最大长度50位,IMEI：国际移动设备识别码（International Mobile Equipment Identity，IMEI），即通常所说的手机序列号、手机“串号”，用于在移动电话网络中识别每一部独立的手机等移动通信设备，相当于移动电话的身份证。	否	64
                put("deviceType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//	付款用户设备类型	交易设备类型(1-电脑;2-手机;3-其他) 字符串类型	否	1
                put("macAddress", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//MAC地址	MAC地址 (直译为媒体访问控制地址，也称为局域网地址（LAN Address），以太网地址（Ethernet Address）或物理地址（Physical Address），它是一个用来确认网上设备位置的地址。)字符串类型 最大长度30	否	64
            }
        };
    }

    @Override
    public Map<String, ParamRule> getParamMapByB8() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, ParamRule> getParamMapByB9() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("smsCode", new ParamRule(ParamTypeEnum.STRING.getType(), 4, 16));// 短信验证码
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, ParamRule> getParamMapByB10() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 64));//产品类型		否	64
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("productCategory", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 商品类别	参考附件：商户入住字典表	否	32
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));// 商户订单号
                put("currency", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));// 交易的币种
                put("amount", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 12));// 支付金额
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("payFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 2, 8));//扣款手续费		否	8
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 城市
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }
    @Override
    public void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="multipleOrder";
        PayOrderInfoTable payOrderInfoTable;
        try {
            payOrderInfoTable = dbCommonRPCComponent.apiPayOrderInfoService.getOne(new PayOrderInfoTable()
                    .setMerOrderId(merOrderId)
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查找订单号是否重复,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNotNull(payOrderInfoTable,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));
    }

    @Override
    public MerchantQuotaRiskTable getMerchantQuotaRiskByMerId(String merchantId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerchantQuotaRiskByMerId";
        MerchantQuotaRiskTable merchantQuotaRiskTable=null;
        try {
            merchantQuotaRiskTable = dbCommonRPCComponent.apiMerchantQuotaRiskService.getOne(new MerchantQuotaRiskTable()
                    .setMerchantId(merchantId))
                    .setStatus(StatusEnum._0.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户风控表发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(merchantQuotaRiskTable,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
        return merchantQuotaRiskTable;
    }

    @Override
    public List<RegisterCollectTable> getSuccessRegisterInfo(RegisterCollectTable registerCollectTable,InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getSuccessRegisterInfo";
        List<RegisterCollectTable> registerCollectTableList=null;
        try {
            registerCollectTableList = dbCommonRPCComponent.apiRegisterCollectService.getList(registerCollectTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询成功进件附属表发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(registerCollectTableList,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
        return registerCollectTableList;
    }

    @Override
    public List<MerchantCardTable> getSuccessMerchantCardInfo(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getSuccessMerchantCardInfo";
        List<MerchantCardTable> merchantCardTableList = null;
        Set<String> platformOrderIdSet = registerCollectTableList.stream().map(m->m.getPlatformOrderId()).collect(Collectors.toSet());
        try {
            merchantCardTableList = dbCommonRPCComponent.apiMerchantCardService.getList(
                    new MerchantCardTable()
                            .setMerchantId(ipo.getMerId())
                            .setTerminalMerId(ipo.getTerMerId())
                            .setBussType(BusinessTypeEnum.b6.getBusiType())
                            .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询成功绑卡记录发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        isHasNotElement(merchantCardTableList,
                ResponseCodeEnum.RXH00033.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00033.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00033.getMsg()));
        return merchantCardTableList;
    }

    @Override
    public ChannelHistoryTable getChannelHistoryInfo(InnerPrintLogObject ipo, String ...args) throws NewPayException {

        final String localPoint="getChannelHistoryInfo";
        ChannelHistoryTable channelHistoryTable=null;
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        try {
            channelHistoryTable = dbCommonRPCComponent.apiChannelHistoryService.getOne(new ChannelHistoryTable()
                    .setMerchantId(args[0])
                    .setTerminalMerId(args[1])
                    .setProductId(args[2])
                    .setCreateTime(dateFormat.format(calendar.getTime())));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询通道使用历史记录异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return channelHistoryTable;
    }


    @Override
    public Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByMer(MerchantInfoTable merInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRiskQuotaInfoByMer";
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dd= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mm= new SimpleDateFormat("yyyy-MM");
        final String d = dd.format(calendar.getTime());
        final String m = mm.format(calendar.getTime());
        Set<String>  timeTepy = new HashSet<>();
        timeTepy.add(d);
        timeTepy.add(m);

        List<RiskQuotaTable> riskQuotaTableList;
        try {
            riskQuotaTableList = dbCommonRPCComponent.apiRiskQuotaService.getListByTimeType(timeTepy, new RiskQuotaTable()
                    .setMeridChannelid(merInfoTable.getMerchantId()).setBussType(BusinessTypeEnum.M.getBusiType()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询商户当日当月风控交易量统计数据发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        if(isHasNotElement(riskQuotaTableList))
            return new Tuple2<>(null,null);

        RiskQuotaTable _d = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(d)).findAny().orElse(null);
        RiskQuotaTable _m = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(m)).findAny().orElse(null);
        return new Tuple2<>(_d,_m);
    }

    @Override
    public void checkSingleAmountRisk(String amountStr, MerchantQuotaRiskTable merchantQuotaRiskTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="checkSingleAmountRisk";
        BigDecimal amount = new BigDecimal(amountStr);
        state(amount.compareTo(merchantQuotaRiskTable.getSingleQuotaAmount())>0,
                ResponseCodeEnum.RXH00012.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00012.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00012.getMsg()));

    }

    @Override
    public void executePlatformRisk(String amount, MerchantQuotaRiskTable merchantQuotaRiskTable, Tuple2<RiskQuotaTable, RiskQuotaTable> merRiskQuota, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRiskQuotaInfo";
        //历史统计交易量
        RiskQuotaTable _dd = merRiskQuota._1;
        RiskQuotaTable _mm = merRiskQuota._2;
        //日限额
        BigDecimal dayQuotaAmount = merchantQuotaRiskTable.getDayQuotaAmount();
        dayQuotaAmount = (null == dayQuotaAmount ? new BigDecimal(0) : dayQuotaAmount);
        //月限额
        BigDecimal monthQuotaAmount = merchantQuotaRiskTable.getMonthQuotaAmount();
        monthQuotaAmount = (null ==monthQuotaAmount ? new BigDecimal(0) : monthQuotaAmount);
        if( !isNull(_dd)) {
            //单日交易量风控判断
            BigDecimal d_Amount =  _dd.getAmount();
            d_Amount =(null == d_Amount ? new BigDecimal(0) : d_Amount );
            d_Amount = d_Amount.add( new BigDecimal(amount) );
            state(d_Amount.compareTo(dayQuotaAmount) > 1,
                    ResponseCodeEnum.RXH00036.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00036.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00036.getMsg()));
        }
        if(!isNull(_mm)){
            BigDecimal m_Amount = _mm.getAmount();
            m_Amount = ( null == m_Amount ? new BigDecimal(0) : m_Amount );
            m_Amount.add( new BigDecimal(amount) );
            state(m_Amount.compareTo(monthQuotaAmount) > 1,
                    ResponseCodeEnum.RXH00037.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH00037.getMsg(), localPoint),
                    format(" %s", ResponseCodeEnum.RXH00037.getMsg()));
        }
    }

    @Override
    public ChannelInfoTable getChannelInfoByChannelHistory( ChannelHistoryTable channelHistoryTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelInfoByChannelHistory";
        ChannelInfoTable channelInfoTable = null;
        //有通道记录时，直接获取通道信息
        try {
            channelInfoTable = dbCommonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable()
                    .setChannelId(channelHistoryTable.getChannelId())
                    .setProductId(channelHistoryTable.getProductId())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，根据通道历史记录表获取通道信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(channelInfoTable,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误可能性判断：通道使用记录中的保存通道ID和产品ID有误，还有可能是通道被禁用了",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));
        return channelInfoTable;
    }

    @Override
    public Tuple2<RiskQuotaTable, RiskQuotaTable> getRiskQuotaInfoByChannel(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRiskQuotaInfoByChannel";
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dd= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mm= new SimpleDateFormat("yyyy-MM");
        final String d = dd.format(calendar.getTime());
        final String m = mm.format(calendar.getTime());
        Set<String>  timeType = new HashSet<>();
        timeType.add(d);
        timeType.add(m);
        List<RiskQuotaTable> riskQuotaTableList;
        try {
            riskQuotaTableList = dbCommonRPCComponent.apiRiskQuotaService.getListByTimeType(timeType,
                    new RiskQuotaTable()
                            .setMeridChannelid(channelInfoTable.getChannelId())
                            .setBussType(BusinessTypeEnum.C.getBusiType()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，查询通道当日和当月风控交易量统计数据发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        if(isHasNotElement(riskQuotaTableList))
            return new Tuple2<>(null,null);

        RiskQuotaTable _d = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(d)).findAny().orElse(null);
        RiskQuotaTable _m = riskQuotaTableList.stream().filter(t->t.getTimeType().equalsIgnoreCase(m)).findAny().orElse(null);
        return new Tuple2<>(_d,_m);
    }

    @Override
    public ChannelInfoTable executeChannelRisk(ChannelInfoTable channelInfoTable, Tuple2<RiskQuotaTable, RiskQuotaTable> channelRiskQuota, InnerPrintLogObject ipo,String ...args) throws NewPayException {
        final String localPoint="executeChannelRisk";
        //单前交易金额
        BigDecimal amount = new BigDecimal(args[0]);
        //通道单笔最小金额
        BigDecimal ch_mim_amount = channelInfoTable.getSingleMinAmount();
        ch_mim_amount = (null == ch_mim_amount ? new BigDecimal(0) : ch_mim_amount );
        state(amount.compareTo(ch_mim_amount) == -1 ,
                ResponseCodeEnum.RXH00038.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00038.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00038.getMsg()));
        //通道单笔最大金额
        BigDecimal ch_max_amount = channelInfoTable.getSingleMaxAmount();
        ch_max_amount = (null == ch_max_amount ? new BigDecimal(0) : ch_max_amount );
        state(amount.compareTo(ch_max_amount) > 0 ,
                ResponseCodeEnum.RXH00039.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00039.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00039.getMsg()));
        //日交易量
        BigDecimal d_Amount;
        if( null == channelRiskQuota._1)   d_Amount = new BigDecimal(0);
        else  d_Amount = channelRiskQuota._1.getAmount();
        d_Amount = (null == d_Amount ? new BigDecimal(0) : d_Amount );
        d_Amount = d_Amount.add(amount);

        //月交易量
        BigDecimal m_Amount;
        if( null == channelRiskQuota._2)  m_Amount = new BigDecimal(0);
        else  m_Amount = channelRiskQuota._2.getAmount();
        m_Amount = (null == m_Amount ? new BigDecimal(0) : m_Amount );
        m_Amount = m_Amount.add(amount);

        //通道日交易量限额
        BigDecimal ch_d_amount = channelInfoTable.getDayQuotaAmount();
        ch_d_amount = (null == ch_d_amount ? new BigDecimal(0) : ch_d_amount );
        if( d_Amount.compareTo(ch_d_amount) >0 )  return null; //执行通道切换

        //通道月交易量限额
        BigDecimal ch_m_amount = channelInfoTable.getMonthQuotaAmount();
        ch_m_amount = (null == ch_m_amount ? new BigDecimal(0) : ch_m_amount );
        if( m_Amount.compareTo(ch_m_amount) > 0 ) return null; //执行通道切换
        return channelInfoTable;
    }

    @Override
    public ChannelInfoTable getFeasibleChannel(List<ChannelInfoTable> channelInfoTableList, InnerPrintLogObject ipo,String ...args) throws NewPayException {
        final String localPoint="getFeasibleChannel";
        //获取匹配的通道历史交易
        Set<String> channelIdSet = channelInfoTableList.stream().map(t ->t.getChannelId() ).collect(Collectors.toSet());
        List<RiskQuotaTable> riskQuotaTableList;
        try {
            riskQuotaTableList = dbCommonRPCComponent.apiRiskQuotaService.getListByChMerId(channelIdSet, new RiskQuotaTable().setBussType(BusinessTypeEnum.C.getBusiType()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，获取通道历史交易量信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        Map<String,List<RiskQuotaTable>>  group = riskQuotaTableList.stream().collect(Collectors.groupingBy(RiskQuotaTable::getMeridChannelid));

        LinkedList<ChannelInfoTable>  ll = new LinkedList<>(channelInfoTableList);
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dd= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mm= new SimpleDateFormat("yyyy-MM");
        final String d = dd.format(calendar.getTime());
        final String m = mm.format(calendar.getTime());
        //当前交易金额
        BigDecimal amount = new BigDecimal(args[0]);
        for(ChannelInfoTable ci : channelInfoTableList){
            {
                //单笔最小金额
                BigDecimal ch_min_amount = ci.getSingleMinAmount();
                ch_min_amount = (null==ch_min_amount? new BigDecimal(0) : ch_min_amount);
                if(amount.compareTo(ch_min_amount) == -1 ) {
                    ll.remove(ci);
                    break;
                }
                //单笔最大金额
                BigDecimal ch_max_amount = ci.getSingleMaxAmount();
                ch_max_amount = ( null == ch_max_amount ? new BigDecimal(0) : ch_max_amount );
                if(amount.compareTo(ch_max_amount) == 1 ){
                    ll.remove(ci);
                    break;
                }
            }
            {
                List<RiskQuotaTable> rql = group.get(ci.getChannelId());
                if (!isHasNotElement(rql)) {
                    //对应的通道日交易统计总量
                    RiskQuotaTable _d = rql.stream().filter(t -> t.getTimeType().equalsIgnoreCase(d)).findAny().orElse(null);
                    BigDecimal d_amount =  ( null == _d ? amount :
                            ( null == _d.getAmount() ? amount : _d.getAmount().add(amount) )
                    );
                    //通道日限额
                    BigDecimal ch_d_amount = ci.getDayQuotaAmount();
                    ch_d_amount = (null == ch_d_amount ? new BigDecimal(0) : ch_d_amount);
                    if(d_amount.compareTo(ch_d_amount) == 1){
                        ll.remove(ci);
                        break;
                    }
                    //对应的通道月交易统计总量
                    RiskQuotaTable _m = rql.stream().filter(t -> t.getTimeType().equalsIgnoreCase(m)).findAny().orElse(null);
                    BigDecimal m_amount = ( null == _m ? amount :
                            (null == _m.getAmount() ? amount : _m.getAmount().add(amount))
                    );
                    //通道月限额
                    BigDecimal ch_m_amount = ci.getMonthQuotaAmount();
                    ch_m_amount = ( null == ch_m_amount ? new BigDecimal(0) : ch_m_amount );
                    if(m_amount.compareTo(ch_m_amount) == 1 ){
                        ll.remove(ci);
                        break;
                    }
                }
            }
        }

        isHasNotElement(ll,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误可能性：筛选可行性的通道时，发现最后筛选为空，可能金额过大或过小，也可能是限额已经达标了",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));


        if(ll.size() == 1) return  ll.get(0);

        ChannelInfoTable channelInfoTable = ll.stream().reduce((t1,t2)-> t1.getChannelLevel()>t2.getChannelLevel() ? t1 : t2).orElse(null);
        isNull(channelInfoTable,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误可能性：挑选通道星级最高通道时，得到了null",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));
        return channelInfoTable;
    }

    @Override
    public PayOrderInfoTable updateByPayOrderInfo(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateByPayOrderInfo";
        PayOrderInfoTable poi = new PayOrderInfoTable()
                //where
                .setPlatformIncome(payOrderInfoTable.getPlatformIncome())
                //set
                .setCrossRespResult(crossResponseMsg)
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setChannelOrderId(crossResponseMsgDTO.getChannelOrderId())
                .setUpdateTime(new Date())
                .setStatus(crossResponseMsgDTO.getCrossStatusCode());

        try {
            dbCommonRPCComponent.apiPayOrderInfoService.updateByWhereCondition(poi);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，更新订单时发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return  payOrderInfoTable;
    }

    @Override
    public ChannelHistoryTable updateByChannelHistoryInfo(ChannelHistoryTable channelHistoryTable,PayOrderInfoTable payOrderInfoTable) {
        if(isNull(channelHistoryTable)){
            channelHistoryTable = new ChannelHistoryTable();
        }
        channelHistoryTable.setId( null == channelHistoryTable.getId() ? System.currentTimeMillis() : channelHistoryTable.getId())
                .setMerchantId( null == channelHistoryTable.getMerchantId() ? payOrderInfoTable.getMerchantId() : channelHistoryTable.getMerchantId())
                .setTerminalMerId( null == channelHistoryTable.getTerminalMerId() ? payOrderInfoTable.getTerminalMerId() : channelHistoryTable.getTerminalMerId())
                .setChannelId( null == channelHistoryTable.getChannelId() ? payOrderInfoTable.getChannelId() : channelHistoryTable.getChannelId() )
                .setProductId( null == channelHistoryTable.getProductId() ? payOrderInfoTable.getProductId() : channelHistoryTable.getProductId() )
                .setTotalAmount( null == channelHistoryTable.getTotalAmount() ? payOrderInfoTable.getAmount() : channelHistoryTable.getTotalAmount().add(payOrderInfoTable.getAmount()))
                .setTotalCount( null == channelHistoryTable.getTotalCount() ? 1 : channelHistoryTable.getTotalCount()+1 );
        if(null == channelHistoryTable.getCreateTime()) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            channelHistoryTable.setCreateTime( dateFormat.format(calendar.getTime()));
        }
        channelHistoryTable.setUpdateTime(new Date());
        return channelHistoryTable;
    }

    @Override
    public Set<RiskQuotaTable> updateByRiskQuotaInfo(PayOrderInfoTable payOrderInfoTable, Tuple2<RiskQuotaTable, RiskQuotaTable> merRiskQuota, Tuple2<RiskQuotaTable, RiskQuotaTable> channelRiskQuota) {

        RiskQuotaTable d_merRiskQuota = merRiskQuota._1;
        RiskQuotaTable m_merRiskQuota = merRiskQuota._2;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dd= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mm= new SimpleDateFormat("yyyy-MM");
        final String d = dd.format(calendar.getTime());
        final String m = mm.format(calendar.getTime());

        //商户当日
        if(isNull(d_merRiskQuota)) d_merRiskQuota =  new RiskQuotaTable();
        d_merRiskQuota.setId( isNull(d_merRiskQuota.getId()) ? null : d_merRiskQuota.getId() )
                .setMeridChannelid( isBlank(d_merRiskQuota.getMeridChannelid()) ? payOrderInfoTable.getMerchantId() : d_merRiskQuota.getMeridChannelid() )
                .setBussType(isBlank(d_merRiskQuota.getBussType()) ? BusinessTypeEnum.M.getBusiType() : d_merRiskQuota.getBussType() )
                .setTimeType(isBlank(d_merRiskQuota.getTimeType()) ? d : d_merRiskQuota.getTimeType() )
                .setAmount(isNull(d_merRiskQuota.getAmount()) ? payOrderInfoTable.getAmount() : d_merRiskQuota.getAmount().add(payOrderInfoTable.getAmount()))
                .setCreateTime(isNull(d_merRiskQuota.getCreateTime()) ? new Date() : d_merRiskQuota.getCreateTime())
                .setUpdateTime(new Date());

        //商户当月
        if(isNull(m_merRiskQuota)) m_merRiskQuota = new RiskQuotaTable();
        m_merRiskQuota.setId( isNull(m_merRiskQuota.getId()) ?  null : m_merRiskQuota.getId() )
                .setMeridChannelid( isBlank(m_merRiskQuota.getMeridChannelid()) ? payOrderInfoTable.getMerchantId() : m_merRiskQuota.getMeridChannelid() )
                .setBussType(isBlank(m_merRiskQuota.getBussType()) ? BusinessTypeEnum.M.getBusiType() : m_merRiskQuota.getBussType() )
                .setTimeType(isBlank(m_merRiskQuota.getTimeType()) ? m : m_merRiskQuota.getTimeType() )
                .setAmount(isNull(m_merRiskQuota.getAmount()) ? payOrderInfoTable.getAmount() : m_merRiskQuota.getAmount().add(payOrderInfoTable.getAmount()))
                .setCreateTime(isNull(m_merRiskQuota.getCreateTime()) ? new Date() : m_merRiskQuota.getCreateTime())
                .setUpdateTime(new Date());


        RiskQuotaTable d_chRiskQuota = channelRiskQuota._1;
        RiskQuotaTable m_chRiskQuota = channelRiskQuota._2;

        //通道当日
        if(isNull(d_chRiskQuota)) d_chRiskQuota =  new RiskQuotaTable();
        d_chRiskQuota.setId( isNull(d_chRiskQuota.getId()) ?  null : d_chRiskQuota.getId() )
                .setMeridChannelid( isBlank(d_chRiskQuota.getMeridChannelid()) ? payOrderInfoTable.getChannelId() : d_chRiskQuota.getMeridChannelid() )
                .setBussType(isBlank(d_chRiskQuota.getBussType()) ? BusinessTypeEnum.C.getBusiType() : d_chRiskQuota.getBussType() )
                .setTimeType(isBlank(d_chRiskQuota.getTimeType()) ? d : d_chRiskQuota.getTimeType() )
                .setAmount(isNull(d_chRiskQuota.getAmount()) ? payOrderInfoTable.getAmount() : d_chRiskQuota.getAmount().add(payOrderInfoTable.getAmount()))
                .setCreateTime(isNull(d_chRiskQuota.getCreateTime()) ? new Date() : d_chRiskQuota.getCreateTime())
                .setUpdateTime(new Date());

        //通道当月
        if(isNull(m_chRiskQuota)) m_chRiskQuota = new RiskQuotaTable();
        m_chRiskQuota.setId( isNull(m_chRiskQuota.getId()) ?  null : m_chRiskQuota.getId() )
                .setMeridChannelid( isBlank(m_chRiskQuota.getMeridChannelid()) ? payOrderInfoTable.getChannelId() : m_chRiskQuota.getMeridChannelid() )
                .setBussType(isBlank(m_chRiskQuota.getBussType()) ? BusinessTypeEnum.C.getBusiType() : m_chRiskQuota.getBussType() )
                .setTimeType(isBlank(m_chRiskQuota.getTimeType()) ? m : m_chRiskQuota.getTimeType() )
                .setAmount(isNull(m_chRiskQuota.getAmount()) ? payOrderInfoTable.getAmount() : m_chRiskQuota.getAmount().add(payOrderInfoTable.getAmount()))
                .setCreateTime(isNull(m_chRiskQuota.getCreateTime()) ? new Date() : m_chRiskQuota.getCreateTime())
                .setUpdateTime(new Date());

        Set<RiskQuotaTable> set = new HashSet<>(4);
        set.add(d_merRiskQuota);
        set.add(d_chRiskQuota);
        set.add(m_merRiskQuota);
        set.add(m_chRiskQuota);
        return set;
    }

    @Override
    public void batchUpdatePayOderCorrelationInfo(PayOrderInfoTable payOrderInfoTable, ChannelHistoryTable cht, Set<RiskQuotaTable> rqtSet, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="batchUpdatePayOderCorrelationInfo";
        try{
            dbCommonRPCComponent.apiPayOrderBusinessTransactionService.updateByPayOrderCorrelationInfo(payOrderInfoTable,cht,rqtSet);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，事务性更新收单情况信息时发生异常,异常信息：%s；订单信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg(), JSON.toJSONString(payOrderInfoTable))
            );
        }
    }

    @Override
    public PayOrderInfoTable getPayOrderInfoByPlatformOrderId(String platformOrderId, String bussType,InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getPayOrderInfoByPlatformOrderId";
        PayOrderInfoTable payOrderInfoTable=null;
        try{
            payOrderInfoTable = dbCommonRPCComponent.apiPayOrderInfoService.getOne(new PayOrderInfoTable()
                    .setPlatformOrderId(platformOrderId)
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setBussType(bussType)
                    .setStatus(StatusEnum._0.getStatus())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：确认收单短信，查询平台订单号是否存在,异常信息：%s；",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(payOrderInfoTable,
                ResponseCodeEnum.RXH00032.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误可能性：平台订单号不存或者申请收单为成功",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00032.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00032.getMsg()));
        return payOrderInfoTable;
    }

    @Override
    public List<MerchantCardTable> filterMerCardByPaymentMsg(List<MerchantCardTable> merchantCardTableList,InnerPrintLogObject ipo,String ...args) throws NewPayException {
        final String localPoint="filterMerCardByPaymentMsg";
        List<MerchantCardTable>  list =  merchantCardTableList.stream().filter(merCard->
                merCard.getBankCardNum().equalsIgnoreCase(args[0])
                        && merCard.getBankCardPhone().equalsIgnoreCase(args[1]) ).collect(Collectors.toList());
        isNull(list,
                ResponseCodeEnum.RXH00034.getCode(),
                format("%s-->商户号：%s；终端号：%s；银行卡号：%s,手机号：%s,错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),args[0],
                        args[1],ResponseCodeEnum.RXH00034.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00034.getMsg()));
        return list;
    }

    @Override
    public  List<RegisterCollectTable> filterRegCollectInfoByMerCard(List<RegisterCollectTable> registerCollectTableList, List<MerchantCardTable> merchantCardTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filterRegCollectInfoByMerCard";
        LinkedList<RegisterCollectTable> linkedList = new LinkedList<>(registerCollectTableList);
        registerCollectTableList.forEach(reg->{
            merchantCardTableList.forEach(card->{
                if( !reg.getOrganizationId().equalsIgnoreCase(card.getOrganizationId()) ){
                    linkedList.remove(reg);
                }
            });
        });
        isHasNotElement(linkedList,
                ResponseCodeEnum.RXH00046.getCode(),
                format("%s-->商户号：%s；终端号：%s,错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00046.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00046.getMsg()));
        return linkedList;
    }

    @Override
    public List<ChannelInfoTable> getAllUsableChannelList(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo,String ...args) throws NewPayException {
        final String localPoint="getAllUsableChannelList";
        Set<String> organizationIdSet = registerCollectTableList.stream().map(RegisterCollectTable::getOrganizationId).collect(Collectors.toSet());
        List<ChannelInfoTable> list = null;
        try {
            list = dbCommonRPCComponent.apiChannelInfoService.getList(new ChannelInfoTable()
                    .setOrganizationIds(organizationIdSet)
                    .setProductId(args[0])
                    .setBusiType(args[1])
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取所有可用通道信息时，发生异常异常；异常信息：%s；",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(list,
                ResponseCodeEnum.RXH00034.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：根据组织id:%s,产品类型：%s，查询结果为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00034.getMsg(),localPoint
                        , JSON.toJSONString(organizationIdSet),args[0]),
                format(" %s",ResponseCodeEnum.RXH00034.getMsg()));

        return list;
    }

    @Override
    public RegisterCollectTable finallyFilterRegCollect(ChannelInfoTable channelInfoTable, List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="finallyFilterRegCollect";
        RegisterCollectTable registerCollectTable =   registerCollectTableList.stream()
                .filter(reg -> reg.getOrganizationId().equalsIgnoreCase(channelInfoTable.getOrganizationId())).findAny().orElse(null);
        isNull(registerCollectTable,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：筛选最终进件信息结果为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
        return registerCollectTable;
    }

    @Override
    public MerchantCardTable finallyFilterMerCard(List<MerchantCardTable> merchantCardTableList,InnerPrintLogObject ipo,String ...args) throws NewPayException {
        final String localPoint="finallyFilterMerCard";
        MerchantCardTable merchantCardTable = merchantCardTableList.stream()
                .filter(merCard-> merCard.getBankCardNum().equalsIgnoreCase(args[0])
                        && merCard.getBankCardPhone().equalsIgnoreCase(args[1])
                ).findAny().orElse(null);
        isNull(merchantCardTable,
                ResponseCodeEnum.RXH00034.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：筛选最终进件信息发生异常",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00034.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00034.getMsg()));
        return merchantCardTable;
    }

    @Override
    public RegisterCollectTable getSuccessRegInfoByChanInfo(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getSuccessRegInfoByChanInfo";
        RegisterCollectTable registerCollectTable = null;
        try{
            registerCollectTable = dbCommonRPCComponent.apiRegisterCollectService.getOne( new RegisterCollectTable()
                    .setOrganizationId(channelInfoTable.getOrganizationId())
                    .setBussType(BusinessTypeEnum.b3.getBusiType())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取进件成功信息时，发生异常异常；异常信息：%s；",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(registerCollectTable,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：筛选最终进件信息发生异常",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
        return registerCollectTable;
    }

    @Override
    public MerchantCardTable getMerCardByChanAndReg(ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo,String ...args) throws NewPayException {
        final String localPoint="getMerCardByChanAndReg";
        MerchantCardTable merchantCardTable = null;
        try{
            merchantCardTable = dbCommonRPCComponent.apiMerchantCardService.getOne(new MerchantCardTable()
                    .setStatus(StatusEnum._0.getStatus())
                    .setBussType(BusinessTypeEnum.b6.getBusiType())
                    .setOrganizationId(channelInfoTable.getOrganizationId())
                    .setBankCardNum(args[0])
                    .setBankCardPhone(args[1]));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取成功办卡信息时，发生异常异常；异常信息：%s；",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(merchantCardTable,
                ResponseCodeEnum.RXH00034.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：查询绑卡信息时，未找到",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00034.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00034.getMsg()));
        return merchantCardTable;
    }

    @Override
    public List<RegisterCollectTable> filterRegCollectByMerSet(List<RegisterCollectTable> registerCollectTableList, List<MerchantSettingTable> merchantSettingTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filterRegCollectByMerSet";
        LinkedList<RegisterCollectTable> linkedList = new LinkedList<>();
        registerCollectTableList.forEach(reg->{
            merchantSettingTableList.forEach(set->{
                if(  reg.getOrganizationId().equalsIgnoreCase(set.getOrganizationId()) ){
                    linkedList.add(reg);
                }
            });
        });
        isHasNotElement(linkedList,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：根据商品通道配置信息，过滤可用进件信息，结果为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));
        return linkedList;
    }

    @Override
    public List<ChannelInfoTable> subtractUnableChanInfo(List<ChannelInfoTable> channelInfoTableList, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="subtractUnableChanInfo";
        LinkedList<ChannelInfoTable>  linkedList = new LinkedList<>(channelInfoTableList);
        channelInfoTableList.forEach(chan->{
            if(chan.getChannelId().equalsIgnoreCase(channelInfoTable.getChannelId()))
                linkedList.remove(chan);
        });
        isHasNotElement(linkedList,
                ResponseCodeEnum.RXH00047.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：通道切换中，无其他通道可以使用",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH00047.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00047.getMsg()));
        return linkedList;
    }

    @Override
    public ChannelInfoTable judgeThisChannelUsable(ChannelInfoTable channelInfoTable, List<MerchantSettingTable> merchantSettingTableList) {
        ChannelInfoTable chann = null;
        for(MerchantSettingTable set : merchantSettingTableList){
            if(set.getChannelId().equalsIgnoreCase(channelInfoTable.getChannelId())){
                chann = channelInfoTable;
            }
        }
        return chann;
    }


    @Override
    public PayOrderInfoTable updateByPayOrderInfoByBefore(PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo, String  ...args) throws NewPayException {
        final String localPoint="updateByPayOrderInfoByB9";
        PayOrderInfoTable poi = new PayOrderInfoTable()
                //where
                .setPlatformOrderId(payOrderInfoTable.getPlatformOrderId())
                //set
                .setBussType(args[0])
                .setStatus(StatusEnum._3.getStatus());
        try {
            dbCommonRPCComponent.apiPayOrderInfoService.updateByWhereCondition(poi);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，更新订单时发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        if(args.length> 1 ) payOrderInfoTable.setSmsCode(args[1]);
        return  payOrderInfoTable;
    }

    @Override
    public MerPayOrderApplyDTO getMerPayOrderApplyDTO(PayOrderInfoTable payOrderInfoTable) {
        MerPayOrderApplyDTO mpoa= new MerPayOrderApplyDTO();
        mpoa.setProductType(payOrderInfoTable.getProductId());
        mpoa.setTerMerId(payOrderInfoTable.getTerminalMerId());
        mpoa.setMerId(payOrderInfoTable.getMerchantId());
        return mpoa;
    }

    @Override
    public void checkPayOrderInfoTableByB9(PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="checkPayOrderInfoTableByB9";
        state(payOrderInfoTable.getBussType().equalsIgnoreCase(BusinessTypeEnum.b9.getBusiType()),
                ResponseCodeEnum.RXH00032.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00032.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00032.getMsg()));

    }

    @Override
    public PayOrderInfoTable updateByPayOrderInfoByB9After(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, PayOrderInfoTable payOrderInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateByPayOrderInfoByB9After";
        payOrderInfoTable
                .setCrossRespResult(crossResponseMsg)
                .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                .setChannelOrderId(crossResponseMsgDTO.getChannelOrderId())
                .setStatus(crossResponseMsgDTO.getCrossStatusCode());

        if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus()) {
            payOrderInfoTable.setStatus(StatusEnum._7.getStatus());
            return payOrderInfoTable;
        }
        try {
            dbCommonRPCComponent.apiPayOrderInfoService.updateByWhereCondition(new PayOrderInfoTable()
                    //where
                    .setPlatformOrderId(payOrderInfoTable.getPlatformOrderId())
                    //set
                    .setCrossRespResult(crossResponseMsg)
                    .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                    .setChannelOrderId(crossResponseMsgDTO.getChannelOrderId())
                    .setUpdateTime(new Date())
                    .setStatus(crossResponseMsgDTO.getCrossStatusCode()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，更新订单时发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return  payOrderInfoTable;
    }

    @Override
    public PayOrderInfoTable savePayOrderByNoAuth(MerchantInfoTable merInfoTable, MerNoAuthPayOrderApplyDTO merNoAuthPayOrderApplyDTO, ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="savePayOrder";
        AgentMerchantSettingTable agentMerchantSettingTable;
        MerchantRateTable merchantRateTable;
        BankRateTable bankRateTable = null;
        try {
            agentMerchantSettingTable = dbCommonRPCComponent.apiAgentMerchantSettingService.getOne(new AgentMerchantSettingTable()
                    .setAgentMerchantId(merInfoTable.getAgentMerchantId())
                    .setProductId(merNoAuthPayOrderApplyDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

            isNull(agentMerchantSettingTable,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：申请支付时，代理商费率未NULL",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

            merchantRateTable = dbCommonRPCComponent.apiMerchantRateService.getOne(new MerchantRateTable()
                    .setMerchantId(merNoAuthPayOrderApplyDTO.getMerId())
                    .setProductId(merNoAuthPayOrderApplyDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

            isNull(merchantRateTable,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：申请支付时，商户费率未NULL",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

            if(merNoAuthPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE_REP.getProductId())
                    || merNoAuthPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE.getProductId())){
                bankRateTable = dbCommonRPCComponent.apiBankRateService.getOne(new BankRateTable()
                        .setBankCode(merNoAuthPayOrderApplyDTO.getBankCode())
                        .setOrganizationId(channelInfoTable.getOrganizationId())
                        .setProductId(merNoAuthPayOrderApplyDTO.getProductType()));
            }
        }catch (Exception e){
            if(e instanceof  NewPayException){
                NewPayException npe = (NewPayException) e;
                throw npe;
            }else {
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，保存订单前，查询商户代理商信息或者是查询商户费率设置信息时发生异常,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint, e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg())
                );
            }
        }


        if(merNoAuthPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE_REP.getProductId())
                || merNoAuthPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE.getProductId())){
            isNull(bankRateTable,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：申请支付时，大额银行卡费率为null",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));
        }


        isNull(merchantRateTable,
                ResponseCodeEnum.RXH00041.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;获取产品（%s）的商户费率为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00041.getMsg(),localPoint, merNoAuthPayOrderApplyDTO.getProductType()),
                format(" %s",ResponseCodeEnum.RXH00041.getMsg()));

        BigDecimal amount =  new BigDecimal(merNoAuthPayOrderApplyDTO.getAmount());
        BigDecimal payFee = new BigDecimal(merNoAuthPayOrderApplyDTO.getPayFee());

        //计算终端费用和入账金额
        payFee = payFee.divide(new BigDecimal(100)); //转小数
        BigDecimal terFee=  amount.multiply(payFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal inAmount = amount.subtract(terFee);


        //商户费用
        BigDecimal merRate = null;
        BigDecimal merFree = null;
        if( !isNull(bankRateTable)){
            merRate = bankRateTable.getBankRate();
            merRate = (null == merRate ? new BigDecimal(0) : merRate);
            merFree =  amount.multiply(merRate.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_UP);
        }else{
            merRate = merchantRateTable.getRateFee();
            merRate = (null == merRate ? new BigDecimal(0) : merRate);
            BigDecimal singleFree = merchantRateTable.getSingleFee();
            singleFree = (null == singleFree ? new BigDecimal(0) : singleFree);
            merFree = amount.multiply(merRate.divide(new BigDecimal(100))).add(singleFree).setScale(2, BigDecimal.ROUND_UP);

        }

        state(merFree.compareTo(terFee) == 1,
                ResponseCodeEnum.RXH00042.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;payFree = %s,商户费率 = %s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00042.getMsg(),localPoint,
                        merNoAuthPayOrderApplyDTO.getPayFee(),merchantRateTable.getRateFee()),
                format(" %s",ResponseCodeEnum.RXH00042.getMsg()));

        //通道费用
        BigDecimal channelRate = channelInfoTable.getChannelRateFee();
        channelRate = (null == channelRate ? new BigDecimal(0) : channelRate);
        BigDecimal channelSingleFee = channelInfoTable.getChannelSingleFee();
        channelSingleFee = (null == channelSingleFee ? new BigDecimal(0) : channelSingleFee);
        BigDecimal channelFeeAmount = amount.multiply(channelRate.divide(new BigDecimal(100))).add(channelSingleFee).setScale(2, BigDecimal.ROUND_UP);

        state(channelFeeAmount.compareTo(terFee) == 1,
                ResponseCodeEnum.RXH00043.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;payFree = %s,通道费率 = %s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00043.getMsg(),localPoint,
                        merNoAuthPayOrderApplyDTO.getPayFee(),channelInfoTable.getChannelRateFee()),
                format(" %s",ResponseCodeEnum.RXH00043.getMsg()));


        state(channelFeeAmount.compareTo(merFree) == 1,
                ResponseCodeEnum.RXH00044.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用大于平台收取商户的费用；通道费率 = %s,商户费率 = %s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                        channelInfoTable.getChannelRateFee(),merchantRateTable.getRateFee()),
                format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

        //商户费用+通道费用 不大于 终端费用
        BigDecimal merAddChanFree = merFree.add(channelFeeAmount);
        state(merAddChanFree.compareTo(terFee) == 1,
                ResponseCodeEnum.RXH00044.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用（%s）+商户费用(%s)>终端费用（%s）"
                        ,ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                        channelFeeAmount,merFree,terFee),
                format(" %s",ResponseCodeEnum.RXH00044.getMsg()));


        //代理商费用
        BigDecimal agentMerRate = null;
        BigDecimal agentMerFree = null;
        if(!isNull(agentMerchantSettingTable)){
            agentMerRate = agentMerchantSettingTable.getRateFee();
            agentMerRate = (null == agentMerRate ? new BigDecimal(0) : agentMerRate);
            BigDecimal agentSingleFree = agentMerchantSettingTable.getSingleFee();
            agentSingleFree = (null == agentSingleFree ? new BigDecimal(0) : agentSingleFree );
            agentMerFree = amount.multiply(agentMerRate.divide(new BigDecimal(100))).add(agentSingleFree).setScale(2, BigDecimal.ROUND_UP);

            BigDecimal threeTotalFee = merAddChanFree.add(agentMerFree);
            state( threeTotalFee.compareTo(terFee) == 1,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用（%s）+商户费用(%s)+代理商费用（%s）>终端费用（%s）",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                            channelFeeAmount,merFree,agentMerFree,terFee),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));
        }


        //平台收入 = 商户费用-通道费用-代理商费用
        agentMerFree = null == agentMerFree? new BigDecimal(0) : agentMerFree;
        BigDecimal platformIncome = merFree.subtract(channelFeeAmount).subtract(agentMerFree);
        state( platformIncome.compareTo(new BigDecimal(0)) == -1,
                ResponseCodeEnum.RXH00044.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s; 平台利润为负（%s）=商户费用（%s）-通道费用（%s）-代理商费用（%s）",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                        platformIncome, merFree,channelFeeAmount,agentMerFree),
                format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

        PayOrderInfoTable payOrderInfoTable = new PayOrderInfoTable();
        payOrderInfoTable
                .setMerOrderId(merNoAuthPayOrderApplyDTO.getMerOrderId())
                .setMerchantId(merNoAuthPayOrderApplyDTO.getMerId())                              .setTerminalMerId(merNoAuthPayOrderApplyDTO.getTerMerId())
                .setIdentityType( Integer.valueOf(merNoAuthPayOrderApplyDTO.getIdentityType()))   .setIdentityNum(merNoAuthPayOrderApplyDTO.getIdentityNum())
                .setBankCode(merNoAuthPayOrderApplyDTO.getBankCode())                             .setBankCardType(Integer.valueOf(merNoAuthPayOrderApplyDTO.getBankCardType()))
                .setBankCardNum(merNoAuthPayOrderApplyDTO.getBankCardNum())                       .setBankCardPhone(merNoAuthPayOrderApplyDTO.getBankCardPhone())
                .setValidDate(merNoAuthPayOrderApplyDTO.getValidDate())                           .setSecurityCode(merNoAuthPayOrderApplyDTO.getSecurityCode())
                .setDeviceId(null)                                                                .setDeviceType(null)
                .setMacAddr(null)                                                                 .setChannelId(channelInfoTable.getChannelId())
                .setRegPlatformOrderId(registerCollectTable.getPlatformOrderId())           .setCardPlatformOrderId(merchantCardTable.getPlatformOrderId())
                .setBussType(BusinessTypeEnum.b10.getBusiType())                             .setProductId(channelInfoTable.getProductId())
                .setProductFee(channelInfoTable.getProductFee())                            .setCurrency(merNoAuthPayOrderApplyDTO.getCurrency())
                .setAmount(amount)                                                          .setInAmount(inAmount)
                .setTerFee(terFee)                                                          .setPayFee(new BigDecimal(merNoAuthPayOrderApplyDTO.getPayFee()))
                .setChannelRate(channelInfoTable.getChannelRateFee())                       .setChannelFee(channelFeeAmount)
                .setAgentRate(agentMerchantSettingTable.getRateFee())                       .setAgentFee(agentMerFree)
                .setMerRate( merchantRateTable.getRateFee())                                .setMerFee(merFree)
                .setPlatformIncome(platformIncome)                                          .setSettleCycle(merchantRateTable.getSettleCycle())
                .setSettleStatus(1)                                                         .setStatus(StatusEnum._2.getStatus())
                .setNotifyStatus(1)
                .setChannelRespResult(null)                                                 .setCrossRespResult(null)
                .setNotifyUrl(merNoAuthPayOrderApplyDTO.getNoticeUrl())                     .setReturnUrl(merNoAuthPayOrderApplyDTO.getReturnUrl())
                .setCreateTime(new Date())                                                  .setUpdateTime(new Date());

        try {
            synchronized (this){
                StringBuilder sb = new StringBuilder();
                sb
                        .append("RXH")
                        .append(new Random(System.currentTimeMillis()).nextInt(1000000))
                        .append("-B10-")
                        .append(System.nanoTime());
                payOrderInfoTable.setPlatformOrderId(sb.toString());
            }
            dbCommonRPCComponent.apiPayOrderInfoService.save(payOrderInfoTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，保存订单时发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return payOrderInfoTable;
    }

    @Override
    public void checkProductTypeByB10(MerNoAuthPayOrderApplyDTO merNoAuthPayOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="checkProductTypeByB10";
        String productType =  merNoAuthPayOrderApplyDTO.getProductType().toUpperCase();
        Set<String> set = new HashSet(Arrays.asList(
                ProductTypeEnum.RH_QUICKPAY_SMALL_NOSMS.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_SMALL_NOSMS_REP.getProductId().toUpperCase()
        ));
        state( !set.contains(productType),
                ResponseCodeEnum.RXH00057.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；产品类型:%s,代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00057.getMsg(),productType,localPoint),
                format(" %s",ResponseCodeEnum.RXH00057.getMsg()));

    }

    @Override
    public void checkProductTypeByB7(MerPayOrderApplyDTO merPayOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="checkProductTypeByB7";
        String productType =  merPayOrderApplyDTO.getProductType().toUpperCase();
        Set<String> set = new HashSet(Arrays.asList(
                ProductTypeEnum.RH_QUICKPAY_SMALL.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_SMALL_REP.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_LARGE_REP.getProductId().toUpperCase(),
                ProductTypeEnum.RH_QUICKPAY_LARGE.getProductId().toUpperCase()
        ));
        state( !set.contains(productType),
                ResponseCodeEnum.RXH00057.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；产品类型:%s,代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00057.getMsg(),productType,localPoint),
                format(" %s",ResponseCodeEnum.RXH00057.getMsg()));
    }

    @Override
    public List<MerchantSettingTable> filterMerchantSettingTableByProductType(List<MerchantSettingTable> merchantSettingTableList, String productType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filterMerchantSettingTableByProductType";
        List<MerchantSettingTable> list = merchantSettingTableList.stream()
                .filter(mst ->mst.getProductId().equalsIgnoreCase(productType)).collect(Collectors.toList());

        isHasNotElement(list,
                ResponseCodeEnum.RXH00040.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；产品类型:%s,代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00040.getMsg(),productType,localPoint),
                format(" %s",ResponseCodeEnum.RXH00040.getMsg()));
        return list;
    }

    @Override
    public PayOrderInfoTable savePayOrder(MerchantInfoTable merInfoTable, MerPayOrderApplyDTO merPayOrderApplyDTO, ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="savePayOrder";
        AgentMerchantSettingTable agentMerchantSettingTable;
        MerchantRateTable merchantRateTable;
        BankRateTable  bankRateTable = null;

        try {
            agentMerchantSettingTable = dbCommonRPCComponent.apiAgentMerchantSettingService.getOne(new AgentMerchantSettingTable()
                    .setAgentMerchantId(merInfoTable.getAgentMerchantId())
                    .setProductId(merPayOrderApplyDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

            isNull(agentMerchantSettingTable,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：申请支付时，代理商费率未NULL",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));


            merchantRateTable = dbCommonRPCComponent.apiMerchantRateService.getOne(new MerchantRateTable()
                    .setMerchantId(merPayOrderApplyDTO.getMerId())
                    .setProductId(merPayOrderApplyDTO.getProductType())
                    .setStatus(StatusEnum._0.getStatus()));

            isNull(merchantRateTable,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：申请支付时，商户费率未NULL",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));


            if(merPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE_REP.getProductId())
                    || merPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE.getProductId())){
                bankRateTable = dbCommonRPCComponent.apiBankRateService.getOne(new BankRateTable()
                        .setBankCode(merPayOrderApplyDTO.getBankCode())
                        .setOrganizationId(channelInfoTable.getOrganizationId())
                        .setProductId(merPayOrderApplyDTO.getProductType()));
            }
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                throw  npe;
            }else {
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，保存订单前，查询商户代理商信息或者是查询商户费率设置信息时发生异常,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint, e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg())
                );
            }
        }


        if(merPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE_REP.getProductId())
                || merPayOrderApplyDTO.getProductType().equalsIgnoreCase(ProductTypeEnum.RH_QUICKPAY_LARGE.getProductId())){
            isNull(bankRateTable,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;异常根源：申请支付时，大额银行卡费率为null",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));
        }


        isNull(merchantRateTable,
                ResponseCodeEnum.RXH00041.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;获取产品（%s）的商户费率为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00041.getMsg(),localPoint, merPayOrderApplyDTO.getProductType()),
                format(" %s",ResponseCodeEnum.RXH00041.getMsg()));

        BigDecimal amount =  new BigDecimal(merPayOrderApplyDTO.getAmount());
        BigDecimal payFee = new BigDecimal(merPayOrderApplyDTO.getPayFee());

        //计算终端费用和入账金额
        payFee = payFee.divide(new BigDecimal(100)); //转小数
        BigDecimal terFee=  amount.multiply(payFee).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal inAmount = amount.subtract(terFee);


        //商户费用
        BigDecimal merRate = null;
        BigDecimal merFree = null;
        if( !isNull(bankRateTable)){
            merRate = bankRateTable.getBankRate();
            merRate = (null == merRate ? new BigDecimal(0) : merRate);
            merFree =  amount.multiply(merRate.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_UP);
        }else{
            merRate = merchantRateTable.getRateFee();
            merRate = (null == merRate ? new BigDecimal(0) : merRate);
            BigDecimal singleFree = merchantRateTable.getSingleFee();
            singleFree = (null == singleFree ? new BigDecimal(0) : singleFree);
            merFree = amount.multiply(merRate.divide(new BigDecimal(100))).add(singleFree).setScale(2, BigDecimal.ROUND_UP);

        }

        state(merFree.compareTo(terFee) == 1,
                ResponseCodeEnum.RXH00042.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;payFree = %s,商户费率 = %s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00042.getMsg(),localPoint,
                        merPayOrderApplyDTO.getPayFee(),merchantRateTable.getRateFee()),
                format(" %s",ResponseCodeEnum.RXH00042.getMsg()));

        //通道费用
        BigDecimal channelRate = channelInfoTable.getChannelRateFee();
        channelRate = (null == channelRate ? new BigDecimal(0) : channelRate);
        BigDecimal channelSingleFee = channelInfoTable.getChannelSingleFee();
        channelSingleFee = (null == channelSingleFee ? new BigDecimal(0) : channelSingleFee);
        BigDecimal channelFeeAmount = amount.multiply(channelRate.divide(new BigDecimal(100))).add(channelSingleFee).setScale(2, BigDecimal.ROUND_UP);

        state(channelFeeAmount.compareTo(terFee) == 1,
                ResponseCodeEnum.RXH00042.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;payFree = %s,通道费率 = %s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00042.getMsg(),localPoint,
                        merPayOrderApplyDTO.getPayFee(),channelInfoTable.getChannelRateFee()),
                format(" %s",ResponseCodeEnum.RXH00042.getMsg()));


        state(channelFeeAmount.compareTo(merFree) == 1,
                ResponseCodeEnum.RXH00044.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用大于平台收取商户的费用；通道费率 = %s,商户费率 = %s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                        channelInfoTable.getChannelRateFee(),merchantRateTable.getRateFee()),
                format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

        //商户费用+通道费用 不大于 终端费用
        BigDecimal merAddChanFree = merFree.add(channelFeeAmount);
        state(merAddChanFree.compareTo(terFee) == 1,
                ResponseCodeEnum.RXH00044.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用（%s）+商户费用(%s)>终端费用（%s）"
                        ,ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                        channelFeeAmount,merFree,terFee),
                format(" %s",ResponseCodeEnum.RXH00044.getMsg()));


        //代理商费用
        BigDecimal agentMerRate = null;
        BigDecimal agentMerFree = null;
        if(!isNull(agentMerchantSettingTable)){
            agentMerRate = agentMerchantSettingTable.getRateFee();
            agentMerRate = (null == agentMerRate ? new BigDecimal(0) : agentMerRate);
            BigDecimal agentSingleFree = agentMerchantSettingTable.getSingleFee();
            agentSingleFree = (null == agentSingleFree ? new BigDecimal(0) : agentSingleFree );
            agentMerFree = amount.multiply(agentMerRate.divide(new BigDecimal(100))).add(agentSingleFree).setScale(2, BigDecimal.ROUND_UP);

            BigDecimal threeTotalFee = merAddChanFree.add(agentMerFree);
            state( threeTotalFee.compareTo(terFee) == 1,
                    ResponseCodeEnum.RXH00044.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误问题：通道费用（%s）+商户费用(%s)+代理商费用（%s）>终端费用（%s）",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                            channelFeeAmount,merFree,agentMerFree,terFee),
                    format(" %s",ResponseCodeEnum.RXH00044.getMsg()));
        }


        //平台收入 = 商户费用-通道费用-代理商费用
        agentMerRate = (null == agentMerRate ? new BigDecimal(0) : agentMerRate);
        agentMerFree = (null == agentMerFree ? new BigDecimal(0) : agentMerFree);
        BigDecimal platformIncome = merFree.subtract(channelFeeAmount).subtract(agentMerFree);
        state( platformIncome.compareTo(new BigDecimal(0)) == -1,
                ResponseCodeEnum.RXH00044.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s; 平台利润为负（%s）=商户费用（%s）-通道费用（%s）-代理商费用（%s）",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00044.getMsg(),localPoint,
                        platformIncome, merFree,channelFeeAmount,agentMerFree),
                format(" %s",ResponseCodeEnum.RXH00044.getMsg()));

        PayOrderInfoTable payOrderInfoTable = new PayOrderInfoTable();
        synchronized (lock) {
            StringBuilder sb = new StringBuilder()
                    .append("B7RXH")
                    .append(count.incrementAndGet())
                    .append(java.util.UUID.randomUUID().toString().replaceAll("-", ""))
                    .append(System.currentTimeMillis());
            payOrderInfoTable.setPlatformOrderId(sb.toString());
        }
        payOrderInfoTable
                .setMerOrderId(merPayOrderApplyDTO.getMerOrderId())
                .setMerchantId(merPayOrderApplyDTO.getMerId())                              .setTerminalMerId(merPayOrderApplyDTO.getTerMerId())
                .setIdentityType( Integer.valueOf(merPayOrderApplyDTO.getIdentityType()))   .setIdentityNum(merPayOrderApplyDTO.getIdentityNum())
                .setBankCode(merPayOrderApplyDTO.getBankCode())                             .setBankCardType(Integer.valueOf(merPayOrderApplyDTO.getBankCardType()))
                .setBankCardNum(merPayOrderApplyDTO.getBankCardNum())                       .setBankCardPhone(merPayOrderApplyDTO.getBankCardPhone())
                .setValidDate(merPayOrderApplyDTO.getValidDate())                           .setSecurityCode(merPayOrderApplyDTO.getSecurityCode())
                .setDeviceId(merPayOrderApplyDTO.getDeviceId())                             .setDeviceType(Integer.valueOf(merPayOrderApplyDTO.getDeviceType()))
                .setMacAddr(merPayOrderApplyDTO.getMacAddress())                            .setChannelId(channelInfoTable.getChannelId())
                .setRegPlatformOrderId(registerCollectTable.getPlatformOrderId())           .setCardPlatformOrderId(merchantCardTable.getPlatformOrderId())
                .setBussType(BusinessTypeEnum.b7.getBusiType())                             .setProductId(channelInfoTable.getProductId())
                .setProductFee(channelInfoTable.getProductFee())                            .setCurrency(merPayOrderApplyDTO.getCurrency())
                .setAmount(amount)                                                          .setInAmount(inAmount)
                .setTerFee(terFee)                                                          .setPayFee(new BigDecimal(merPayOrderApplyDTO.getPayFee()))
                .setChannelRate(channelInfoTable.getChannelRateFee())                       .setChannelFee(channelFeeAmount)
                .setAgentRate(null == agentMerchantSettingTable ? null : agentMerchantSettingTable.getRateFee() )
                .setAgentFee(agentMerFree)
                .setMerRate( merchantRateTable.getRateFee())                                .setMerFee(merFree)
                .setPlatformIncome(platformIncome)                                          .setSettleCycle(merchantRateTable.getSettleCycle())
                .setSettleStatus(1)                                                         .setStatus(StatusEnum._2.getStatus())
                .setNotifyStatus(1)
                .setChannelRespResult(null)                                                 .setCrossRespResult(null)
                .setNotifyUrl(merPayOrderApplyDTO.getNoticeUrl())                           .setReturnUrl(merPayOrderApplyDTO.getReturnUrl())
                .setCreateTime(new Date())                                                  .setUpdateTime(new Date());
        try {
            dbCommonRPCComponent.apiPayOrderInfoService.save(payOrderInfoTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：申请支付时，保存订单时发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        return payOrderInfoTable;
    }

}

