package com.internal.playment.common.table.merchant;

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

@TableName ( "2_merchant_quota_risk_table" )
@Getter
public class MerchantQuotaRiskTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private BigDecimal singleQuotaAmount;//单笔风控额度
	private BigDecimal dayQuotaAmount;//单日笔风控额度
	private BigDecimal monthQuotaAmount;//单月笔风控额度
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public MerchantQuotaRiskTable setId(Long id) {
		this.id = id;
		return this;
	}

	public MerchantQuotaRiskTable setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	public MerchantQuotaRiskTable setSingleQuotaAmount(BigDecimal singleQuotaAmount) {
		this.singleQuotaAmount = singleQuotaAmount;
		return this;
	}

	public MerchantQuotaRiskTable setDayQuotaAmount(BigDecimal dayQuotaAmount) {
		this.dayQuotaAmount = dayQuotaAmount;
		return this;
	}

	public MerchantQuotaRiskTable setMonthQuotaAmount(BigDecimal monthQuotaAmount) {
		this.monthQuotaAmount = monthQuotaAmount;
		return this;
	}

	public MerchantQuotaRiskTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public MerchantQuotaRiskTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public MerchantQuotaRiskTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
