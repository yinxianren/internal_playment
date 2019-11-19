package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@TableName("5_product_group_type_table")
public class ProductGroupTypeTable implements Serializable {
    private Long id ;
    private String productGroupId;// 产品组ID
    private String productGroupName;//产品组名称
    private String productGroupMsg;//产品组信息
    private String organizationId;//机构ID
    private Integer status;//状态 0：启用 1:禁用
    private Date createTime;// 创建时间
    private Date updateTime;//更新时间

    public ProductGroupTypeTable setId(Long id) {
        this.id = id;
        return this;
    }

    public ProductGroupTypeTable setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
        return this;
    }

    public ProductGroupTypeTable setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
        return this;
    }

    public ProductGroupTypeTable setProductGroupMsg(String productGroupMsg) {
        this.productGroupMsg = productGroupMsg;
        return this;
    }

    public ProductGroupTypeTable setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public ProductGroupTypeTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public ProductGroupTypeTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public ProductGroupTypeTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
