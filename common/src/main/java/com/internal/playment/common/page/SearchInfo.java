package com.internal.playment.common.page;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SearchInfo implements Serializable {
    public static final int ID_SEARCH_TYPE_ORDER = 0;
    public static final int ID_SEARCH_TYPE_MERCHANT_ORDER = 1;
    public static final int ID_SEARCH_TYPE_BANK_ORDER = 2;

    /**
     * ID查找类型（用于 com.rxh.mapper.core.CoreOrderMapper.Order_Search_Info）：
     * 0：平台订单号
     * 1：商户订单号
     * 2：银行流水号
     */
    private Integer idSearchType;
    private String id;
    private String merId;
    private Integer siteId;
    private String siteUrl;
    private String email;
    private Short exceptionStatus;
    private Integer acquirerAccount;
    private String payMode;
    private String payType;
    private Short bankStatus;
    private Short shipStatus;
    private Short financeStatus;
    private String ip;
    /*
    时间查询类型（暂用于保证金查询）：
    0：交易日期
    1：释放日期
     */
    private Integer timeSearchType;
    /*
    Date可为创建时间
    Date2可为修改时间
     */
    private Date startDate;
    private Date endDate;
    private Date startDate2;
    private Date endDate2;
    // 保证金释放状态
    private Short bondStatus;
    private Short changeType;
    /*
    用于查询多个异常状态的信息
     */
    private List<Short> changeTypeList;
    private Short changeStatus;
    // 创建人
    private String creator;
    // 修改人
    private String modifier;
    // 申请人
    private String applicant;
    // 审核人
    private String auditor;
    private Short type;
    private Short status;
    private String content;
    //merchant_rate_percent
    private String elementValue;
    private String typeValue;
    private BigDecimal upPercent;
    //商户订单号
    private String merOrderId;
    //快递公司
    private String expressName;
    //快递号
    private String expressNo;
    private String country;
    // 原始币种
    private String sourceCurrency;
    // 目标币种
    private String targetCurrency;
    //风控专区
    private Short refType;
    private Integer refId;
    private String element;
    private String transId;
    private String organizationId;
    private String channelId;
    private String channelTransCode;
    private String currency;
    private BigDecimal amount;
    private BigDecimal realAmount;
    private BigDecimal fee;
    private String channelName;
    private BigDecimal channelFee;
    private BigDecimal income;
    private Integer orderStatus;
    private Integer settleStatus;
    private BigDecimal agentFee;
    private Date tradeTime;
    private String payId;
    private  String orderId;
    private String parentId;
    private String settleCycle;
    private String terminalMerId;
    private String customerId;
    private String loginIp;
    private String agentMerId;
    private String productId;


}
