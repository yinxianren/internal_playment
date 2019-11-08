package com.internal.playment.common.dto;

import com.internal.playment.common.table.business.*;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import lombok.Getter;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 上午9:11
 * Description:
 */
@Getter
public class RequestCrossMsgDTO  implements Serializable {

    private RegisterCollectTable registerCollectTable;
    private RegisterInfoTable registerInfoTable;
    private MerchantCardTable merchantCardTable;
    private PayOrderInfoTable payOrderInfoTable;
    private TransOrderInfoTable transOrderInfoTable;
    private ChannelInfoTable channelInfoTable;
    private ChannelExtraInfoTable channelExtraInfoTable;
    private String IP;

    public RequestCrossMsgDTO setIP(String IP) {
        this.IP = IP;
        return this;
    }

    public RequestCrossMsgDTO setRegisterCollectTable(RegisterCollectTable registerCollectTable) {
        this.registerCollectTable = registerCollectTable;
        return this;
    }

    public RequestCrossMsgDTO setRegisterInfoTable(RegisterInfoTable registerInfoTable) {
        this.registerInfoTable = registerInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setMerchantCardTable(MerchantCardTable merchantCardTable) {
        this.merchantCardTable = merchantCardTable;
        return this;
    }

    public RequestCrossMsgDTO setPayOrderInfoTable(PayOrderInfoTable payOrderInfoTable) {
        this.payOrderInfoTable = payOrderInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setTransOrderInfoTable(TransOrderInfoTable transOrderInfoTable) {
        this.transOrderInfoTable = transOrderInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setChannelInfoTable(ChannelInfoTable channelInfoTable) {
        this.channelInfoTable = channelInfoTable;
        return this;
    }

    public RequestCrossMsgDTO setChannelExtraInfoTable(ChannelExtraInfoTable channelExtraInfoTable) {
        this.channelExtraInfoTable = channelExtraInfoTable;
        return this;
    }
}
