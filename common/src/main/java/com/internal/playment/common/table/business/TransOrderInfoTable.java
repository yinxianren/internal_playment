package com.internal.playment.common.table.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:26
 * Description:
 */
@ToString
@TableName("9_trans_order_info_table")
@Getter
public class TransOrderInfoTable implements Serializable {
    @TableId(type= IdType.INPUT)
    private Long id;//表主键
    private String platformOrderId ;//平台订单号
    private String channelOrderId;// 通道订单号
    private String merchantId ;//商户号
    private String terminalMerId ;//终端号
    private String merOrderId ;//商户订单号
    private String orgMerOrderId ;//原交易商户订单号
    private Integer identityType ;//身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证
    private String identityNum ;//证件号
    private String bankCode ;//银行简称如：中国农业银行： ABC，中国工商银行： ICBC
    private String bankName ; //银行名称     	如：中国农业银行   bank_name             	否	32
    private Integer bankAccountProp;//账户属性	0：个人账户，1：对公账户	否	1    bank_account_prop
    private Integer  bankCardType ;//卡号类型，1借记卡  2信用卡
    private String bankCardNum ;//银行卡号
    private String bankCardPhone ;//银行卡手机号
    private String channelId ;//通道id
    private String  busiType;//'业务类型:b11
    private String productId ;//产品类型ID
    private BigDecimal productFee ;//产品费率
    private String currency;//币种
    private BigDecimal amount;//订单金额
    private BigDecimal outAmount ;//出账金额
    private BigDecimal backFee ;//单笔固定金额
    private BigDecimal  terRate ;//终端费率
    private BigDecimal terFee ;//终端手续费
    private BigDecimal channelRate ;//通道费率
    private BigDecimal channelFee ;//通道手续费
    private BigDecimal  agentRate ;//代理商费率
    private BigDecimal agentFee ;//代理商手续费
    private BigDecimal merRate ;//商户费率
    private BigDecimal merFee ;//商户手续费
    private BigDecimal platformIncome ;//单笔平台收入
    private String settleCycle;//结算周期
    private Integer  settleStatus;//结算状态，0：已经结算，1：未结算
    private String channelRespResult;//通道响应结果
    private String crossRespResult;//cross响应结果
    private Integer  status;//状态 0：success1:fail
    private Date createTime;//创建时间
    private Date updateTime;//更新时间

    //分页参数
    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;
    @TableField(exist = false)
    private Date beginTime;
    @TableField(exist = false)
    private Date endTime;

    public TransOrderInfoTable() {
    }

    public TransOrderInfoTable setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }
    public TransOrderInfoTable setBankAccountProp(Integer bankAccountProp) {
        this.bankAccountProp = bankAccountProp;
        return this;
    }
    public TransOrderInfoTable setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
        return this;
    }

    public TransOrderInfoTable setId(Long id) {
        this.id = id;
        return this;
    }

    public TransOrderInfoTable setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
        return this;
    }

    public TransOrderInfoTable setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public TransOrderInfoTable setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
        return this;
    }

    public TransOrderInfoTable setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
        return this;
    }

    public TransOrderInfoTable setOrgMerOrderId(String orgMerOrderId) {
        this.orgMerOrderId = orgMerOrderId;
        return this;
    }

    public TransOrderInfoTable setIdentityType(Integer identityType) {
        this.identityType = identityType;
        return this;
    }

    public TransOrderInfoTable setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
        return this;
    }

    public TransOrderInfoTable setBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public TransOrderInfoTable setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
        return this;
    }

    public TransOrderInfoTable setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
        return this;
    }

    public TransOrderInfoTable setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
        return this;
    }

    public TransOrderInfoTable setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public TransOrderInfoTable setBusiType(String busiType) {
        this.busiType = busiType;
        return this;
    }

    public TransOrderInfoTable setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public TransOrderInfoTable setProductFee(BigDecimal productFee) {
        this.productFee = productFee;
        return this;
    }

    public TransOrderInfoTable setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public TransOrderInfoTable setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransOrderInfoTable setOutAmount(BigDecimal outAmount) {
        this.outAmount = outAmount;
        return this;
    }

    public TransOrderInfoTable setBackFee(BigDecimal backFee) {
        this.backFee = backFee;
        return this;
    }

    public TransOrderInfoTable setTerRate(BigDecimal terRate) {
        this.terRate = terRate;
        return this;
    }

    public TransOrderInfoTable setTerFee(BigDecimal terFee) {
        this.terFee = terFee;
        return this;
    }

    public TransOrderInfoTable setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
        return this;
    }

    public TransOrderInfoTable setChannelFee(BigDecimal channelFee) {
        this.channelFee = channelFee;
        return this;
    }

    public TransOrderInfoTable setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
        return this;
    }

    public TransOrderInfoTable setAgentFee(BigDecimal agentFee) {
        this.agentFee = agentFee;
        return this;
    }

    public TransOrderInfoTable setMerRate(BigDecimal merRate) {
        this.merRate = merRate;
        return this;
    }

    public TransOrderInfoTable setMerFee(BigDecimal merFee) {
        this.merFee = merFee;
        return this;
    }

    public TransOrderInfoTable setPlatformIncome(BigDecimal platformIncome) {
        this.platformIncome = platformIncome;
        return this;
    }

    public TransOrderInfoTable setSettleCycle(String settleCycle) {
        this.settleCycle = settleCycle;
        return this;
    }

    public TransOrderInfoTable setSettleStatus(Integer settleStatus) {
        this.settleStatus = settleStatus;
        return this;
    }

    public TransOrderInfoTable setChannelRespResult(String channelRespResult) {
        this.channelRespResult = channelRespResult;
        return this;
    }

    public TransOrderInfoTable setCrossRespResult(String crossRespResult) {
        this.crossRespResult = crossRespResult;
        return this;
    }

    public TransOrderInfoTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public TransOrderInfoTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public TransOrderInfoTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
