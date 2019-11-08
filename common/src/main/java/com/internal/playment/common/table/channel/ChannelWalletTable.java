package com.internal.playment.common.table.channel;

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
 * @Date 2019-10-29 
 */

@TableName ( "5_channel_wallet_table" )
@Getter
public class ChannelWalletTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String channelId;//通道id
	private String organizationId;//机构ID
	private BigDecimal totalAmount;//订单总金额
	private BigDecimal incomeAmount;//入账总金额
	private BigDecimal outAmount;//出帐总金额
	private BigDecimal totalFee;//总手续
	private BigDecimal feeProfit;//总手续利润
	private BigDecimal totalBalance;//总金额
	private BigDecimal totalAvailableAmount;//总可用金额
	private BigDecimal totalUnavailableAmount;//总可不用金额
	private BigDecimal totalMargin;//总保证金
	private BigDecimal totalFreezeAmount;//总可冻结金额
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public ChannelWalletTable setId(Long id) {
		this.id = id;
		return this;
	}

	public ChannelWalletTable setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public ChannelWalletTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}


	public ChannelWalletTable setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public ChannelWalletTable setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
		return this;
	}

	public ChannelWalletTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}

	public ChannelWalletTable setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
		return this;
	}

	public ChannelWalletTable setFeeProfit(BigDecimal feeProfit) {
		this.feeProfit = feeProfit;
		return this;
	}

	public ChannelWalletTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public ChannelWalletTable setTotalAvailableAmount(BigDecimal totalAvailableAmount) {
		this.totalAvailableAmount = totalAvailableAmount;
		return this;
	}

	public ChannelWalletTable setTotalUnavailableAmount(BigDecimal totalUnavailableAmount) {
		this.totalUnavailableAmount = totalUnavailableAmount;
		return this;
	}

	public ChannelWalletTable setTotalMargin(BigDecimal totalMargin) {
		this.totalMargin = totalMargin;
		return this;
	}

	public ChannelWalletTable setTotalFreezeAmount(BigDecimal totalFreezeAmount) {
		this.totalFreezeAmount = totalFreezeAmount;
		return this;
	}

	public ChannelWalletTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public ChannelWalletTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public ChannelWalletTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
