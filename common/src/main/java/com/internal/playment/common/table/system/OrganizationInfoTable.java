package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author  monkey
 * @Date 2019-10-22 
 */

@TableName ( "5_organization_info_table" )
@Getter
public class OrganizationInfoTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String organizationId;//机构ID
	private String organizationName;//机构名称
	private String applicationClassObj;//接口对象 application_class_obj
	private Integer status;//状态 0：启用 ,1:禁用
	private Date createTime;//创建时间
	private Date updateTime;//更新时间
	private String creator;//创建者
	@TableField(exist = false)
	private String productIds;
	@TableField(exist = false)
	private List<ProductSettingTable> productTypes;
	@TableField(exist = false)
    private Collection<String> organizationIdColl;

	public OrganizationInfoTable setOrganizationIdColl(Collection<String> organizationIdColl) {
		this.organizationIdColl = organizationIdColl;
		return this;
	}

	public OrganizationInfoTable setId(Long id) {
		this.id = id;
		return this;
	}

	public OrganizationInfoTable setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
		return this;
	}

	public OrganizationInfoTable setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
		return this;
	}

	public OrganizationInfoTable setApplicationClassObj(String applicationClassObj) {
		this.applicationClassObj = applicationClassObj;
		return this;
	}

	public OrganizationInfoTable setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public OrganizationInfoTable setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public OrganizationInfoTable setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public OrganizationInfoTable setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	public OrganizationInfoTable setProductIds(String productIds) {
		this.productIds = productIds;
		return this;
	}

	public OrganizationInfoTable setProductTypes(List<ProductSettingTable> productTypes) {
		this.productTypes = productTypes;
		return this;
	}
}
