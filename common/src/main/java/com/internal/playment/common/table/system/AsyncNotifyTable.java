package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName( "1_async_notify_table" )
@Getter
public class AsyncNotifyTable implements Serializable {
    @TableId(type= IdType.INPUT)
    private Long id;// BIGINT(19) NOT NULL AUTO_INCREMENT COMMENT 表主键,
    private String merchantId;// VARCHAR(32) NOT NULL COMMENT 商户号,
    private String terminalMerId;// VARCHAR(64) NOT NULL COMMENT 终端商户号,
    private String merOrderId;// VARCHAR(64) NOT NULL COMMENT 商户订单号,
    private String platformOrderId;// VARCHAR(64) NOT NULL COMMENT 平台订单号,
    private BigDecimal amount;// DECIMAL(12,2) DEFAULT 0.00 COMMENT 订单金额,
    private Integer orderStatus;// TINYINT(2) DEFAULT 0 COMMENT 订单状态（0成功、1失败、2未处理、3处理中）,
    private Integer  notifyCount;// TINYINT(2) DEFAULT 0 COMMENT 通知次数,
    private Integer status;// TINYINT(2) DEFAULT 0 COMMENT 状态 0：结束通知,1:继续通知,
    private String notifyUrl;
    private String respResult;
    private Date UpdateTime;
    private Date createTime;// DATETIME NOT NULL COMMENT 创建时间,
    @TableField(exist = false)
    private String beginTime;
    @TableField(exist = false)
    private String endTime;


    public AsyncNotifyTable setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public AsyncNotifyTable setRespResult(String respResult) {
        this.respResult = respResult;
        return this;
    }

    public AsyncNotifyTable setUpdateTime(Date updateTime) {
        UpdateTime = updateTime;
        return this;
    }

    public AsyncNotifyTable setBeginTime(String beginTime) {
        this.beginTime = beginTime;
        return this;
    }

    public AsyncNotifyTable setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public AsyncNotifyTable setId(Long id) {
        this.id = id;
        return this;
    }

    public AsyncNotifyTable setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public AsyncNotifyTable setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
        return this;
    }

    public AsyncNotifyTable setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
        return this;
    }

    public AsyncNotifyTable setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
        return this;
    }

    public AsyncNotifyTable setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public AsyncNotifyTable setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public AsyncNotifyTable setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
        return this;
    }

    public AsyncNotifyTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public AsyncNotifyTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}
