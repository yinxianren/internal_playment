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
 * @Description
 * @Author  monkey
 * @Date 2019-10-22 
 */
@ToString
@TableName ( "7_merchant_card_table" )
@Getter
public class MerchantCardTable  implements Serializable,Cloneable{
	@TableId(type= IdType.INPUT)
	private Long id;//表主键
	private String organizationId;
	private String platformOrderId;//平台订单号
	private String registerCollectPlatformOrderId;//进件附属表的流水号
	private String merchantId;//商户号
	private String merOrderId;//商户订单号
	private String terminalMerId;//终端商户号
	private String cardHolderName;//身份证用户名
	private Integer identityType;//身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证
	private String identityNum;//证件号
	private String bankCode;//银行简称	如：中国农业银行： ABC，中国工商银行： ICBC
	private String bankName;//银行名称  bank_name
	private Integer bankAccountProp;//账户属性	0：个人账户，1：对公账户  bank_account_prop
	private String subBankName;//支行名称  sub_bank_name
	private Integer bankCardType;//卡号类型，1借记卡  2信用卡
	private String bankCardNum;//银行卡号
	private String bankCardPhone;//银行卡手机号
	private String validDate;//信用卡必填，格式：MMYY
	private String securityCode;//信用卡必填，格式：信用卡必填，信用卡背面三位安全码
	private String channelRespResult;//通道响应结果
	private String crossRespResult;//cross响应结果
	private String bussType;//b4：绑卡申请,b5:绑卡验证码，b6：确认绑卡
	private BigDecimal payFee;//	扣款手续费
	private BigDecimal backFee;//	代付手续费
	private String backCardNum;//	还款银行卡号
	private String backBankCode;//	还款银行编码
	private String backCardPhone;//	还款银行卡手机号
	private Integer status;//状态 0：success ,1:fail
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	@TableField(exist = false)
	private String smsCode;

	public MerchantCardTable setBankName(String bankName) {
		this.bankName = bankName;
		return this;
	}

	public MerchantCardTable setBankAccountProp(Integer bankAccountProp) {
		this.bankAccountProp = bankAccountProp;
		return this;
	}

	public MerchantCardTable setSubBankName(String subBankName) {
		this.subBankName = subBankName;
		return this;
	}

	public MerchantCardTable setSmsCode(String smsCode) {
		this.smsCode = smsCode;
		return this;
	}

	public MerchantCardTable setPayFee(BigDecimal payFee) {
		this.payFee = payFee;
		return this;
	}

	public MerchantCardTable setBackFee(BigDecimal backFee) {
		this.backFee = backFee;
		return this;
	}

	public MerchantCardTable setBackCardNum(String backCardNum) {
		this.backCardNum = backCardNum;
		return this;
	}

	public MerchantCardTable setBackBankCode(String backBankCode) {
		this.backBankCode = backBankCode;
		return this;
	}

	public MerchantCardTable setBackCardPhone(String backCardPhone) {
		this.backCardPhone = backCardPhone;
		return this;
	}

	public MerchantCardTable setRegisterCollectPlatformOrderId(String registerCollectPlatformOrderId) {
		this.registerCollectPlatformOrderId = registerCollectPlatformOrderId;
		return this;
	}

	public MerchantCardTable setBussType(String bussType) {
		this.bussType = bussType;
		return this;
	}


	public MerchantCardTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public MerchantCardTable setId(Long id) {
		this.id = id;
		return this;
	}

	public MerchantCardTable setPlatformOrderId(String platformOrderId) {
		this.platformOrderId = platformOrderId;
		return this;
	}

	public MerchantCardTable setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	public MerchantCardTable setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
		return this;
	}

	public MerchantCardTable setTerminalMerId(String terminalMerId) {
		this.terminalMerId = terminalMerId;
		return this;
	}

	public MerchantCardTable setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
		return this;
	}

	public MerchantCardTable setIdentityType(Integer identityType) {
		this.identityType = identityType;
		return this;
	}

	public MerchantCardTable setIdentityNum(String identityNum) {
		this.identityNum = identityNum;
		return this;
	}

	public MerchantCardTable setBankCode(String bankCode) {
		this.bankCode = bankCode;
		return this;
	}

	public MerchantCardTable setBankCardType(Integer bankCardType) {
		this.bankCardType = bankCardType;
		return this;
	}

	public MerchantCardTable setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
		return this;
	}

	public MerchantCardTable setBankCardPhone(String bankCardPhone) {
		this.bankCardPhone = bankCardPhone;
		return this;
	}

	public MerchantCardTable setValidDate(String validDate) {
		this.validDate = validDate;
		return this;
	}

	public MerchantCardTable setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
		return this;
	}

	public MerchantCardTable setChannelRespResult(String channelRespResult) {
		this.channelRespResult = channelRespResult;
		return this;
	}

	public MerchantCardTable setCrossRespResult(String crossRespResult) {
		this.crossRespResult = crossRespResult;
		return this;
	}

	public MerchantCardTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public MerchantCardTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public MerchantCardTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
