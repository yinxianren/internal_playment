package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午1:47
 * Description:
 */

@TableName("5_merchant_setting_table")
@Getter
public class MerchantSettingTable implements Serializable {
    @TableId(type= IdType.AUTO)
    private Long id  ;//表主键
    private String  merchantId ;//商户号
    private String productId ;//产品类型ID
    private String organizationId ;//机构ID
    private String channelId ;//通道id
    private Integer status;//状态 0：启用1:禁用
    private Date createTime ;//创建时间
    private Date updateTime ;//更新时间
    @TableField(exist = false)
    private List<String> channelIds;


    public MerchantSettingTable setId(Long id) {
        this.id = id;
        return  this;
    }

    public MerchantSettingTable setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return  this;
    }

    public MerchantSettingTable setProductId(String productId) {
        this.productId = productId;
        return  this;
    }

    public MerchantSettingTable setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return  this;
    }

    public MerchantSettingTable setChannelId(String channelId) {
        this.channelId = channelId;
        return  this;
    }

    public MerchantSettingTable setStatus(Integer status) {
        this.status = status;
        return  this;
    }

    public MerchantSettingTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return  this;
    }

    public MerchantSettingTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return  this;
    }

    public void setChannelIds(List<String> channelIds) {
        this.channelIds = channelIds;
    }
}
