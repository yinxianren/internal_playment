package com.internal.playment.common.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午3:07
 * Description:
 */
@Getter
public class ResponseEntity  implements Serializable {
    private String merId;
    private String merOrderId;
    private String platformOrderId;
    private Integer status;
    private String msg;
    private String amount;
    private String signMsg;
    private String errorCode;
    private String errorMsg;
    private String channelTab;

    public ResponseEntity setChannelTab(String channelTab) {
        this.channelTab = channelTab;
        return this;
    }

    public ResponseEntity setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public ResponseEntity setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
        return this;
    }

    public ResponseEntity setMerId(String merId) {
        this.merId = merId;
        return this;
    }

    public ResponseEntity setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
        return this;
    }

    public ResponseEntity setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public ResponseEntity setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResponseEntity setSignMsg(String signMsg) {
        this.signMsg = signMsg;
        return this;
    }

    public ResponseEntity setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ResponseEntity setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
