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
 * @Date 2019-10-22 
 */

@TableName ( "8_channel_history_table" )
@Getter
public class ChannelHistoryTable  implements Serializable {
	@TableId(type= IdType.INPUT)
	private Long id;//表主键
	private String merchantId;//商户号
	private String terminalMerId;
	private String channelId;//通道id
	private String productId;//产品类型ID
	private BigDecimal totalAmount;//累计订单金额
	private Long totalCount;//累计使用次数
	private String createTime;//创建时间
	private Date updateTime;//更新时间

	public ChannelHistoryTable setId(Long id) {
		this.id = id;
		return this;
	}

	public ChannelHistoryTable setMerchantId(String merchantId) {
		this.merchantId = merchantId;
		return this;
	}

	public ChannelHistoryTable setTerminalMerId(String terminalMerId) {
		this.terminalMerId = terminalMerId;
		return this;
	}

	public ChannelHistoryTable setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public ChannelHistoryTable setProductId(String productId) {
		this.productId = productId;
		return this;
	}

	public ChannelHistoryTable setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
		return this;
	}

	public ChannelHistoryTable setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	public ChannelHistoryTable setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public ChannelHistoryTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}
}
