package com.internal.playment.common.table.agent;

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

@TableName ( "3_agent_merchant_wallet_table" )
@Getter
public class AgentMerchantWalletTable implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String agentMerchantId;//代理商ID
	private BigDecimal totalAmount;//订单总金额
	private BigDecimal incomeAmount;//入账总金额
	private BigDecimal outAmount;//出账总金额
	private BigDecimal totalBalance;//总金额
	private BigDecimal totalAvailableAmount;//总可用金额
	private BigDecimal totalUnavailableAmount;//总不可用金额
	private BigDecimal totalFee;//总手续费
	private BigDecimal totalFreezeAmount;//总冻结金额
	private Integer status;//状态 0：启用 ,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public AgentMerchantWalletTable setId(Long id) {
		this.id = id;
		return this;
	}

	public AgentMerchantWalletTable setAgentMerchantId(String agentMerchantId) {
		this.agentMerchantId = agentMerchantId;
		return this;
	}

	public AgentMerchantWalletTable setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public AgentMerchantWalletTable setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
		return this;
	}

	public AgentMerchantWalletTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}

	public AgentMerchantWalletTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public AgentMerchantWalletTable setTotalAvailableAmount(BigDecimal totalAvailableAmount) {
		this.totalAvailableAmount = totalAvailableAmount;
		return this;
	}

	public AgentMerchantWalletTable setTotalUnavailableAmount(BigDecimal totalUnavailableAmount) {
		this.totalUnavailableAmount = totalUnavailableAmount;
		return this;
	}

	public AgentMerchantWalletTable setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
		return this;
	}

	public AgentMerchantWalletTable setTotalFreezeAmount(BigDecimal totalFreezeAmount) {
		this.totalFreezeAmount = totalFreezeAmount;
		return this;
	}

	public AgentMerchantWalletTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public AgentMerchantWalletTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public AgentMerchantWalletTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
