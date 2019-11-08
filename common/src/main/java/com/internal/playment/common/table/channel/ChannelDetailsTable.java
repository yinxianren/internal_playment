package com.internal.playment.common.table.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

@TableName ( "5_channel_details_table" )
@Getter
public class ChannelDetailsTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String channelId;//通道id
	private String organizationId;//机构ID
	private String productId;//产品类型ID
	private String merOrderId;//商户订单号
	private String platformOrderId;//平台订单号
	private BigDecimal amount;//订单金额
	private BigDecimal inAmount;//入账金额
	private BigDecimal outAmount;//出帐金额
	private BigDecimal chRateFee;//单笔费率
	private BigDecimal chFee;//手续费
	private BigDecimal chFeeProfit;//手续利润
	private BigDecimal merRateFee;
	private BigDecimal merFee;
	private BigDecimal totalBalance;//总余额
	private Long timestamp;//数据插入的时间点，保证并发排序有序
	private Integer status;//状态 0：success ,1:fail
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


	public ChannelDetailsTable setChRateFee(BigDecimal chRateFee) {
		this.chRateFee = chRateFee;
		return this;
	}

	public ChannelDetailsTable setChFee(BigDecimal chFee) {
		this.chFee = chFee;
		return this;
	}

	public ChannelDetailsTable setChFeeProfit(BigDecimal chFeeProfit) {
		this.chFeeProfit = chFeeProfit;
		return this;
	}

	public ChannelDetailsTable setMerRateFee(BigDecimal merRateFee) {
		this.merRateFee = merRateFee;
		return this;
	}

	public ChannelDetailsTable setMerFee(BigDecimal merFee) {
		this.merFee = merFee;
		return this;
	}

	public ChannelDetailsTable setId(Long id) {
		this.id = id;
		return this;
	}

	public ChannelDetailsTable setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public ChannelDetailsTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public ChannelDetailsTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public ChannelDetailsTable setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
		return this;
	}

	public ChannelDetailsTable setPlatformOrderId(String platformOrderId) {
		this.platformOrderId = platformOrderId;
		return this;
	}

	public ChannelDetailsTable setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public ChannelDetailsTable setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
		return this;
	}

	public ChannelDetailsTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}



	public ChannelDetailsTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public ChannelDetailsTable setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public ChannelDetailsTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public ChannelDetailsTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public ChannelDetailsTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
