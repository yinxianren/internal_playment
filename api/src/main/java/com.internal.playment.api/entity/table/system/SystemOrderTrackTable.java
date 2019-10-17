package com.internal.playment.api.entity.table.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 上午9:52
 * Description:
 */
@Data
@TableName("systemOrderTrackTable")
public class SystemOrderTrackTable implements Serializable {
    private String id;//表主键
    private String merId;//商户号
    private String merOrderId;//商户订单号
    private String platformOrderId;//平台订单号
    private BigDecimal amount;//订单金额
    private String returnUrl;//返回地址
    private String noticeUrl;//异步通知地址
    private Integer status;//状态
    private String requestMsg;//请求报文
    private String referUrl;  // 请求路径
    private Date tradeTime;  // 交易时间
    private String responseResult; // 响应结果
    private Date   createTime;//创建时间

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

    public SystemOrderTrackTable setStatus(Integer status) {
        this.status = status;
        return  this;
    }

    public SystemOrderTrackTable setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
        return  this;
    }

    public SystemOrderTrackTable setReferUrl(String referUrl) {
        this.referUrl = referUrl;
        return  this;
    }

    public SystemOrderTrackTable setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
        return  this;
    }

    public SystemOrderTrackTable setResponseResult(String responseResult) {
        this.responseResult = responseResult;
        return  this;
    }

    public SystemOrderTrackTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return  this;
    }
}
