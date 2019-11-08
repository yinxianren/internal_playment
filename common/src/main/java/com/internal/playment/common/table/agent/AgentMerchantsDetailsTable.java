package com.internal.playment.common.table.agent;

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
 * @Date 2019-10-22 
 */

@TableName ( "3_agent_merchants_details_table" )
@Getter
public class AgentMerchantsDetailsTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String agentMerchantId;//代理商ID
	private String merOrderId;//商户订单号
	private String platformOrderId;//平台订单号
	private String productId;//产品类型ID
	private BigDecimal amount;//订单金额
	private BigDecimal inAmount;//入账金额
	private BigDecimal outAmount;//出帐金额
	private BigDecimal rateFee;//单笔费率
	private BigDecimal fee;//手续费
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

	public AgentMerchantsDetailsTable setId(Long id) {
		this.id = id;
		return this;
	}

	public AgentMerchantsDetailsTable setAgentMerchantId(String agentMerchantId) {
		this.agentMerchantId = agentMerchantId;
		return this;
	}

	public AgentMerchantsDetailsTable setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
		return this;
	}

	public AgentMerchantsDetailsTable setPlatformOrderId(String platformOrderId) {
		this.platformOrderId = platformOrderId;
		return this;
	}

	public AgentMerchantsDetailsTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public AgentMerchantsDetailsTable setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public AgentMerchantsDetailsTable setInAmount(BigDecimal inAmount) {
		this.inAmount = inAmount;
		return this;
	}

	public AgentMerchantsDetailsTable setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
		return this;
	}

	public AgentMerchantsDetailsTable setRateFee(BigDecimal rateFee) {
		this.rateFee = rateFee;
		return this;
	}

	public AgentMerchantsDetailsTable setFee(BigDecimal fee) {
		this.fee = fee;
		return this;
	}

	public AgentMerchantsDetailsTable setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
		return this;
	}

	public AgentMerchantsDetailsTable setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public AgentMerchantsDetailsTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public AgentMerchantsDetailsTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public AgentMerchantsDetailsTable setUpdateTime(Date updateTime) {
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
