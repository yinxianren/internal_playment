package com.internal.playment.common.dto;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午2:19
 * Description:
 */
@ToString
@Getter
public class CrossResponseMsgDTO implements Serializable {

    private Integer crossStatusCode ;//cross返回的状态码
    private String  crossStatusMsg;//cross返回的状态码信息
    private String  crossResponseMsg;//cross返回信息信息
    private String  channelOrderId;//通道流水号
    private Date    channelResponseTime;//通道响应返回的时间
    private String  channelResponseMsg;//通道返回信息
    private String  errorCode;
    private String  errorMsg;


    public CrossResponseMsgDTO setCrossStatusMsg(String crossStatusMsg) {
        this.crossStatusMsg = crossStatusMsg;
        return this;
    }

    public CrossResponseMsgDTO setCrossStatusCode(Integer crossStatusCode) {
        this.crossStatusCode = crossStatusCode;
        return this;
    }

    public CrossResponseMsgDTO setCrossResponseMsg(String crossResponseMsg) {
        this.crossResponseMsg = crossResponseMsg;
        return this;
    }

    public CrossResponseMsgDTO setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
        return this;
    }


    public CrossResponseMsgDTO setChannelResponseTime(Date channelResponseTime) {
        this.channelResponseTime = channelResponseTime;
        return this;
    }

    public CrossResponseMsgDTO setChannelResponseMsg(String channelResponseMsg) {
        this.channelResponseMsg = channelResponseMsg;
        return this;
    }

    public CrossResponseMsgDTO setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public CrossResponseMsgDTO setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
