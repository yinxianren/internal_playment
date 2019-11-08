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

@TableName ( "3_agent_merchant_setting_table" )
@Getter
public class AgentMerchantSettingTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String agentMerchantId;//代理商ID
	private String productId;//产品类型ID
	private BigDecimal singleFee;//单笔费用，元/笔
	private BigDecimal rateFee;//单笔费率
	private BigDecimal marginRatio;//保证金比例
	private String marginCycle;//保证金周期
	private String settleCycle;//结算周期
	private Integer status;//状态 0：启用 ,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间

	public AgentMerchantSettingTable setId(Long id) {
		this.id = id;
		return this;
	}

	public AgentMerchantSettingTable setAgentMerchantId(String agentMerchantId) {
		this.agentMerchantId = agentMerchantId;
		return this;
	}

	public AgentMerchantSettingTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public AgentMerchantSettingTable setSingleFee(BigDecimal singleFee) {
		this.singleFee = singleFee;
		return this;
	}

	public AgentMerchantSettingTable setRateFee(BigDecimal rateFee) {
		this.rateFee = rateFee;
		return this;
	}

	public AgentMerchantSettingTable setMarginRatio(BigDecimal marginRatio) {
		this.marginRatio = marginRatio;
		return this;
	}

	public AgentMerchantSettingTable setMarginCycle(String marginCycle) {
		this.marginCycle = marginCycle;
		return this;
	}

	public AgentMerchantSettingTable setSettleCycle(String settleCycle) {
		this.settleCycle = settleCycle;
		return this;
	}

	public AgentMerchantSettingTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public AgentMerchantSettingTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public AgentMerchantSettingTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
