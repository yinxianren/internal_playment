package com.internal.playment.common.table.system;

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

@TableName ( "8_risk_quota_table" )
@Getter
public class RiskQuotaTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String meridChannelid;//商户号/通道ID
	private String bussType;// C:通道,M:商户 
	private String timeType;//时间类型
	private BigDecimal amount;//将对应时间内商户的统计数据转换成CNY后累加
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public RiskQuotaTable setId(Long id) {
		this.id = id;
		return this;
	}

	public RiskQuotaTable setMeridChannelid(String meridChannelid) {
		this.meridChannelid = meridChannelid;
		return this;
	}

	public RiskQuotaTable setBussType(String bussType) {
		this.bussType = bussType;
		return this;
	}

	public RiskQuotaTable setTimeType(String timeType) {
		this.timeType = timeType;
		return this;
	}

	public RiskQuotaTable setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public RiskQuotaTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public RiskQuotaTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
