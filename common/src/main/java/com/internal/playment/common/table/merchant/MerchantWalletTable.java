package com.internal.playment.common.table.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "2_merchant_wallet_table" )
@Getter
public class MerchantWalletTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private BigDecimal totalAmount;//订单总金额
	private BigDecimal incomeAmount;//入账总金额
	private BigDecimal outAmount;//出帐总金额
	private BigDecimal totalFee;//总手续
	private BigDecimal feeProfit;//总手续利润
	private BigDecimal totalMargin;//总保证金
	private BigDecimal totalBalance;//总金额
	private BigDecimal totalAvailableAmount;//总可用金额
	private BigDecimal totalUnavailableAmount;//总可不用金额
	private BigDecimal totalFreezeAmount;//总可冻结金额
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	@TableField(exist = false)
	private String agentMerchantId;

	@TableField(exist = false)
	private List<String> merchantIds;

	public MerchantWalletTable setId(Long id) {
		this.id = id;
		return this;
	}

	public MerchantWalletTable setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	public MerchantWalletTable setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public MerchantWalletTable setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
		return this;
	}

	public MerchantWalletTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}

	public MerchantWalletTable setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
		return this;
	}

	public MerchantWalletTable setFeeProfit(BigDecimal feeProfit) {
		this.feeProfit = feeProfit;
		return this;
	}

	public MerchantWalletTable setTotalMargin(BigDecimal totalMargin) {
		this.totalMargin = totalMargin;
		return this;
	}

	public MerchantWalletTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public MerchantWalletTable setTotalAvailableAmount(BigDecimal totalAvailableAmount) {
		this.totalAvailableAmount = totalAvailableAmount;
		return this;
	}

	public MerchantWalletTable setTotalUnavailableAmount(BigDecimal totalUnavailableAmount) {
		this.totalUnavailableAmount = totalUnavailableAmount;
		return this;
	}

	public MerchantWalletTable setTotalFreezeAmount(BigDecimal totalFreezeAmount) {
		this.totalFreezeAmount = totalFreezeAmount;
		return this;
	}

	public MerchantWalletTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public MerchantWalletTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public MerchantWalletTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public void setMerchantIds(List<String> merchantIds) {
		this.merchantIds = merchantIds;
	}

	public void setAgentMerchantId(String agentMerchantId) {
		this.agentMerchantId = agentMerchantId;
	}
}
