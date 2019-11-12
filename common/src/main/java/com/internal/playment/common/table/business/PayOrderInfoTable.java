package com.internal.playment.common.table.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:07
 * Description:
 */
@ToString
@TableName("8_pay_order_info_table")
@Getter
@NoArgsConstructor
public class PayOrderInfoTable implements Serializable {

    @TableId(type= IdType.INPUT)
    private Long id ;// 表主键,
    private String platformOrderId;// 平台订单号,
    private String channelOrderId;//通道订单号
    private String merchantId;// 商户号,
    private String terminalMerId;// 终端号,
    private String merOrderId;// 商户订单号,
    private Integer   identityType;// 身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证,
    private String   identityNum;// 证件号,
    private String bankCode;// 银行简称,如：中国农业银行： ABC，中国工商银行： ICBC,
    private Integer  bankCardType;// 卡号类型，1借记卡  2信用卡,
    private String   bankCardNum;// 银行卡号,
    private String     bankCardPhone;// 银行卡手机号,
    private String validDate;// 信用卡必填，格式：MMYY,
    private String securityCode;// 信用卡必填，格式：信用卡必填，信用卡背面三位安全码,
    private String  deviceId;// 交易设备号,
    private Integer   deviceType;// 交易设备类型(1-电脑;2-手机;3-其他) 字符串类型,
    private String   macAddr;// MAC地址,
    private String  channelId;// 通道id,
    private String  regPlatformOrderId;//进件B3的平台id  reg_platform_order_id
    private String  cardPlatformOrderId;//办卡B6的平台id card_platform_order_id
    private String  bussType;//'b7:支付申请,b8:支付验证码,b9:确认交易
    private String  productId;// 产品类型ID,
    private BigDecimal productFee ;// 产品费率,
    private String  currency;// 币种,
    private BigDecimal    amount;// 订单金额,
    private BigDecimal  inAmount;// 入账金额,
    private BigDecimal  payFee;// 扣款手续率,
    private BigDecimal  terFee;// 终端手续费,
    private BigDecimal  channelRate;// 通道费率,
    private BigDecimal  channelFee;// 通道手续费,
    private BigDecimal   agentRate;// 通道费率,
    private BigDecimal   agentFee;// 通道手续费,
    private BigDecimal   merRate;// 商户费率,
    private BigDecimal   merFee;// 商户手续费,
    private BigDecimal   platformIncome;// 单笔平台收入,
    private String   settleCycle;// 结算周期,
    private Integer     settleStatus;// 结算状态，0：已经结算，1：未结算,
    private String  channelRespResult;// 通道响应结果,
    private String  crossRespResult;// cross响应结果,
    private Integer status;// 状态 0：success,1:fail,
    private Date createTime;// 创建时间,
    private Date updateTime;// 更新时间,

    @TableField(exist = false)
    private String smsCode; //收到的短信验证码
    @TableField(exist = false)
    private Collection<String> merOrderIdCollect; //支持多订单查找
    @TableField(exist = false)
    private Collection<Integer> statusCollect; //支持多状态查找


    @TableField(exist = false)
    private String beginTime; //查询开始时间
    @TableField(exist = false)
    private String endTime; //查询结束时间

    @TableField(exist = false)
    private String organizationId;


    //分页参数
    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;


    public PayOrderInfoTable setBeginTime(String beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public PayOrderInfoTable setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public PayOrderInfoTable setSmsCode(String smsCode) {
        this.smsCode = smsCode;
        return this;
    }

    public PayOrderInfoTable setStatusCollect(Collection<Integer> statusCollect) {
        this.statusCollect = statusCollect;
        return this;
    }

    public PayOrderInfoTable setMerOrderIdCollect(Collection<String> merOrderIdCollect) {
        this.merOrderIdCollect = merOrderIdCollect;
        return this;
    }

    public PayOrderInfoTable setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
        return this;
    }

    public PayOrderInfoTable setRegPlatformOrderId(String regPlatformOrderId) {
        this.regPlatformOrderId = regPlatformOrderId;
        return this;
    }

    public PayOrderInfoTable setCardPlatformOrderId(String cardPlatformOrderId) {
        this.cardPlatformOrderId = cardPlatformOrderId;
        return this;
    }

    public PayOrderInfoTable setId(Long id) {
        this.id = id;
        return this;
    }

    public PayOrderInfoTable setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
        return this;
    }

    public PayOrderInfoTable setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public PayOrderInfoTable setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
        return this;
    }

    public PayOrderInfoTable setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
        return this;
    }

    public PayOrderInfoTable setIdentityType(Integer identityType) {
        this.identityType = identityType;
        return this;
    }

    public PayOrderInfoTable setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
        return this;
    }

    public PayOrderInfoTable setBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public PayOrderInfoTable setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
        return this;
    }

    public PayOrderInfoTable setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
        return this;
    }

    public PayOrderInfoTable setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
        return this;
    }

    public PayOrderInfoTable setValidDate(String validDate) {
        this.validDate = validDate;
        return this;
    }

    public PayOrderInfoTable setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
        return this;
    }

    public PayOrderInfoTable setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public PayOrderInfoTable setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public PayOrderInfoTable setMacAddr(String macAddr) {
        this.macAddr = macAddr;
        return this;
    }

    public PayOrderInfoTable setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public PayOrderInfoTable setBussType(String bussType) {
        this.bussType = bussType;
        return this;
    }

    public PayOrderInfoTable setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public PayOrderInfoTable setProductFee(BigDecimal productFee) {
        this.productFee = productFee;
        return this;
    }

    public PayOrderInfoTable setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public PayOrderInfoTable setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PayOrderInfoTable setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
        return this;
    }

    public PayOrderInfoTable setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
        return this;
    }

    public PayOrderInfoTable setTerFee(BigDecimal terFee) {
        this.terFee = terFee;
        return this;
    }

    public PayOrderInfoTable setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
        return this;
    }

    public PayOrderInfoTable setChannelFee(BigDecimal channelFee) {
        this.channelFee = channelFee;
        return this;
    }

    public PayOrderInfoTable setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
        return this;
    }

    public PayOrderInfoTable setAgentFee(BigDecimal agentFee) {
        this.agentFee = agentFee;
        return this;
    }

    public PayOrderInfoTable setMerRate(BigDecimal merRate) {
        this.merRate = merRate;
        return this;
    }

    public PayOrderInfoTable setMerFee(BigDecimal merFee) {
        this.merFee = merFee;
        return this;
    }

    public PayOrderInfoTable setPlatformIncome(BigDecimal platformIncome) {
        this.platformIncome = platformIncome;
        return this;
    }

    public PayOrderInfoTable setSettleCycle(String settleCycle) {
        this.settleCycle = settleCycle;
        return this;
    }

    public PayOrderInfoTable setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
        return this;
    }

    public PayOrderInfoTable setChannelRespResult(String channelRespResult) {
        this.channelRespResult = channelRespResult;
        return this;
    }

    public PayOrderInfoTable setCrossRespResult(String crossRespResult) {
        this.crossRespResult = crossRespResult;
        return this;
    }

    public PayOrderInfoTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public PayOrderInfoTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public PayOrderInfoTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
