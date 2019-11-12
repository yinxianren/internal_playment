package com.internal.playment.common.table.channel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Getter
@TableName("5_channel_info_table")
public class ChannelInfoTable implements Serializable {
    @TableId(type= IdType.AUTO)
    private Long id;//表主键,
    private String channelId;//通道id,
    private String channelName;//通道名称,
    private String channelTab;//通道标识,
    private String  busiType;//'业务类型，pay :收单，trans:代付'
    private String organizationId;//机构ID,
    private String productId;//产品类型ID,
    private Integer channelLevel;//通道等级，数字越大等级越高，不超过99,
    private String channelTransCode;//通道交易码,
    private String requestUrl;//请求cross的路径,
    private String channelParam;//通道配置参数,
    private String settleCycle;//结算周期,
    private BigDecimal productFee;//产品费率,
    private BigDecimal  channelSingleFee;// decimal(6,2)  0.00 ;//通道单笔费用，元/笔,
    private BigDecimal channelRateFee;//通道单笔费率,
    private BigDecimal singleMinAmount;//单笔最小金额,
    private BigDecimal singleMaxAmount;//单笔最大金额,
    private BigDecimal dayQuotaAmount;//单日最大金额,
    private BigDecimal monthQuotaAmount;//单月最大金额,
    private Integer status;//状态 0：启用 ,1:禁用,
    private Date createTime ;//创建时间,
    private Date updateTime ;//更新时间,
    private Integer bankStatus;//大额银行启用状态 0：启用 ,1:禁用,

    @TableField(exist = false)
    private Collection<String>  organizationIds;

    public ChannelInfoTable setOrganizationIds(Collection<String> organizationIds) {
        this.organizationIds = organizationIds;
        return this;
    }

    public ChannelInfoTable setId(Long id) {
        this.id = id;
        return this;
    }

    public ChannelInfoTable setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public ChannelInfoTable setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public ChannelInfoTable setChannelTab(String channelTab) {
        this.channelTab = channelTab;
        return this;
    }

    public ChannelInfoTable setBusiType(String busiType) {
        this.busiType = busiType;
        return this;
    }

    public ChannelInfoTable setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public ChannelInfoTable setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public ChannelInfoTable setChannelLevel(Integer channelLevel) {
        this.channelLevel = channelLevel;
        return this;
    }

    public ChannelInfoTable setChannelTransCode(String channelTransCode) {
        this.channelTransCode = channelTransCode;
        return this;
    }

    public ChannelInfoTable setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public ChannelInfoTable setChannelParam(String channelParam) {
        this.channelParam = channelParam;
        return this;
    }

    public ChannelInfoTable setSettleCycle(String settleCycle) {
        this.settleCycle = settleCycle;
        return this;
    }

    public ChannelInfoTable setProductFee(BigDecimal productFee) {
        this.productFee = productFee;
        return this;
    }

    public ChannelInfoTable setChannelSingleFee(BigDecimal channelSingleFee) {
        this.channelSingleFee = channelSingleFee;
        return this;
    }

    public ChannelInfoTable setChannelRateFee(BigDecimal channelRateFee) {
        this.channelRateFee = channelRateFee;
        return this;
    }

    public ChannelInfoTable setSingleMinAmount(BigDecimal singleMinAmount) {
        this.singleMinAmount = singleMinAmount;
        return this;
    }

    public ChannelInfoTable setSingleMaxAmount(BigDecimal singleMaxAmount) {
        this.singleMaxAmount = singleMaxAmount;
        return this;
    }

    public ChannelInfoTable setDayQuotaAmount(BigDecimal dayQuotaAmount) {
        this.dayQuotaAmount = dayQuotaAmount;
        return this;
    }

    public ChannelInfoTable setMonthQuotaAmount(BigDecimal monthQuotaAmount) {
        this.monthQuotaAmount = monthQuotaAmount;
        return this;
    }

    public ChannelInfoTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public ChannelInfoTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public ChannelInfoTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setBankStatus(Integer bankStatus) {
        this.bankStatus = bankStatus;
    }
}
