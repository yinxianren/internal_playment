package com.internal.playment.common.table.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "5_channel_extra_info_table" )
@Getter
public class ChannelExtraInfoTable  implements Serializable {

	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String extraChannelId;//附属通道ID
	private String extraChannelName;//附属通道名称
	private String organizationId;//机构ID
	private String bussType;//
	private String requestUrl;//请求cross的路径
	private String channelParam;//通道配置参数
	private Integer status;//状态 0：启用 ,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间
	private String creator;//创建者

	public ChannelExtraInfoTable setId(Long id) {
		this.id = id;
		return this;
	}

	public ChannelExtraInfoTable setExtraChannelId(String extraChannelId) {
		this.extraChannelId = extraChannelId;
		return this;
	}

	public ChannelExtraInfoTable setExtraChannelName(String extraChannelName) {
		this.extraChannelName = extraChannelName;
		return this;
	}

	public ChannelExtraInfoTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public ChannelExtraInfoTable setBussType(String bussType) {
		this.bussType = bussType;
		return this;
	}

	public ChannelExtraInfoTable setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
		return this;
	}

	public ChannelExtraInfoTable setChannelParam(String channelParam) {
		this.channelParam = channelParam;
		return this;
	}

	public ChannelExtraInfoTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public ChannelExtraInfoTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public ChannelExtraInfoTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}
