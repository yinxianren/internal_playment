package com.internal.playment.common.table.terminal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "4_terminal_merchants_wallet_table" )
@Getter
public class TerminalMerchantsWalletTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private String terminalMerId;//终端商户号
	private BigDecimal totalAmount;//订单总金额
	private BigDecimal incomeAmount;//入账总金额
	private BigDecimal outAmount;//出帐总金额
	private BigDecimal totalBalance;//总金额
	private BigDecimal totalAvailableAmount;//总可用金额
	private BigDecimal totalUnavailableAmount;//总可不用金额
	private BigDecimal totalFee;//总手续
	private BigDecimal totalMargin;//总保证金
	private BigDecimal totalFreezeAmount;//总可冻结金额
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public TerminalMerchantsWalletTable setId(Long id) {
		this.id = id;
		return this;
	}

	public TerminalMerchantsWalletTable setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	public TerminalMerchantsWalletTable setTerminalMerId(String terminalMerId) {
		this.terminalMerId = terminalMerId;
		return this;
	}

	public TerminalMerchantsWalletTable setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public TerminalMerchantsWalletTable setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
		return this;
	}

	public TerminalMerchantsWalletTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}

	public TerminalMerchantsWalletTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public TerminalMerchantsWalletTable setTotalAvailableAmount(BigDecimal totalAvailableAmount) {
		this.totalAvailableAmount = totalAvailableAmount;
		return this;
	}

	public TerminalMerchantsWalletTable setTotalUnavailableAmount(BigDecimal totalUnavailableAmount) {
		this.totalUnavailableAmount = totalUnavailableAmount;
		return this;
	}

	public TerminalMerchantsWalletTable setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
		return this;
	}

	public TerminalMerchantsWalletTable setTotalMargin(BigDecimal totalMargin) {
		this.totalMargin = totalMargin;
		return this;
	}

	public TerminalMerchantsWalletTable setTotalFreezeAmount(BigDecimal totalFreezeAmount) {
		this.totalFreezeAmount = totalFreezeAmount;
		return this;
	}

	public TerminalMerchantsWalletTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public TerminalMerchantsWalletTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public TerminalMerchantsWalletTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
