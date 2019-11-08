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

@TableName ( "2_merchant_rate_table" )
@Getter
public class MerchantRateTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String merchantId;//商户号
	private String productId;//产品类型ID
	private String organizationId;//机构ID
	private String channelId;//通道id
	private BigDecimal singleFee;//单笔费用，元/笔
	private BigDecimal rateFee;//单笔费率
	private BigDecimal marginRatio;//保证金比例
	private String marginCycle;//保证金周期
	private String settleCycle;//结算周期
	private Integer status;//状态 0：启用,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public MerchantRateTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public MerchantRateTable setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public MerchantRateTable setId(Long id) {
		this.id = id;
		return this;
	}

	public MerchantRateTable setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	public MerchantRateTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public MerchantRateTable setSingleFee(BigDecimal singleFee) {
		this.singleFee = singleFee;
		return this;
	}

	public MerchantRateTable setRateFee(BigDecimal rateFee) {
		this.rateFee = rateFee;
		return this;
	}

	public MerchantRateTable setMarginRatio(BigDecimal marginRatio) {
		this.marginRatio = marginRatio;
		return this;
	}

	public MerchantRateTable setMarginCycle(String marginCycle) {
		this.marginCycle = marginCycle;
		return this;
	}

	public MerchantRateTable setSettleCycle(String settleCycle) {
		this.settleCycle = settleCycle;
		return this;
	}

	public MerchantRateTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public MerchantRateTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public MerchantRateTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
