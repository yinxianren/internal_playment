package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午3:22
 * Description:
 */
@TableName("1_system_order_track_table")
@Getter
public class SystemOrderTrackTable implements Serializable {
    @TableId(type= IdType.AUTO)
    private Long id;//主键
    private String ip;//IP地址
    private String merId;//商户号
    private String merOrderId;//商户订单号
    private String platformOrderId;//平台订单号
    private BigDecimal amount;//交易金额
    private String returnUrl;//返回地址
    private String noticeUrl;//异步通知地址
    private Integer tradeCode;//状态吗
    private String requestMsg;//请求报文，最佳是明文
    private String requestPath; // 请求网址
    private String responseResult; // 返回结果
    private String platformPrintLog;//平台打印日志
    private Date tradeTime; // 交易时间
    private Date createTime;//创建时间

    public SystemOrderTrackTable setIp(String ip) {
        this.ip = ip;
        return  this;
    }

    public SystemOrderTrackTable setRequestPath(String requestPath) {
        this.requestPath = requestPath;
        return  this;
    }

    public SystemOrderTrackTable setPlatformPrintLog(String platformPrintLog) {
        this.platformPrintLog = platformPrintLog;
        return  this;
    }

    public SystemOrderTrackTable setId(Long id) {
        this.id = id;
        return  this;
    }

    public SystemOrderTrackTable setMerId(String merId) {
        this.merId = merId;
        return  this;
    }

    public SystemOrderTrackTable setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
        return  this;
    }

    public SystemOrderTrackTable setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
        return  this;
    }

    public SystemOrderTrackTable setAmount(BigDecimal amount) {
        this.amount = amount;
        return  this;
    }

    public SystemOrderTrackTable setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return  this;
    }

    public SystemOrderTrackTable setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
        return  this;
    }

    public SystemOrderTrackTable setTradeCode(Integer tradeCode) {
        this.tradeCode = tradeCode;
        return  this;
    }

    public SystemOrderTrackTable setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
        return  this;
    }

    public SystemOrderTrackTable setReferPath(String requestPath) {
        this.requestPath = requestPath;
        return  this;
    }

    public SystemOrderTrackTable setResponseResult(String responseResult) {
        this.responseResult = responseResult;
        return  this;
    }

    public SystemOrderTrackTable setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
        return  this;
    }

    public SystemOrderTrackTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return  this;
    }
}
