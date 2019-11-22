package com.internal.playment.common.page;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PayCardholderInfo implements Serializable {
    private String payId;
    private String cardholderName;
    private String cardholderPhone;
    private Integer identityType;
    private String identityNum;
    private String bankName;
    private String bankBranchName;
    private String bankBranchNum;
    private String bankcardNum;
    private Integer bankcardType;
    private String expiryYear;
    private String expiryMonth;
    private String cvv;
    private String agentMerchantName;
    private Date channelBankTime;
    private String channelBankResult;
    private String currency;
    private String payFee;
    private String orgOrderId;
    private String terminalMerId;
    private Integer orderStatus;
    private String productName;
    private String channelName;
}
