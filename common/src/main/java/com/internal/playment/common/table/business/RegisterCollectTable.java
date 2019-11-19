package com.internal.playment.common.table.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午10:31
 * Description:
 */
@ToString
@TableName("6_register_collect_table")
@Getter
public class RegisterCollectTable implements Serializable {
    @TableId(type= IdType.INPUT)
    private Long id ;//表主键',
    private String platformOrderId;//平台订单号',
    private Long  ritId;//主表id',
    private String organizationId;//通道机构ID
    private String  productGroupId;// 产品类型ID,
    private String  merchantId;//商户号',
    private String  terminalMerId;//终端商户号',
    private String  merOrderId;//商户订单号',
    private String  category;//经营类目编号',
    private byte[]  miMerCertPic1;//资质影印件',
    private byte[]  miMerCertPic2;//资质影印件',
    private String  bankCode;//银行简称	如：中国农业银行： ABC，中国工商银行： ICBC',
    private Integer bankCardType;//卡号类型	1借记卡  2信用卡',
    private Integer bankAccountProp; //0：个人账户，1：对公账户
    private String  cardHolderName;//持卡人姓名',
    private String  bankCardNum;//银行卡号',
    private String  bankCardPhone;//'银行卡手机号',
    private BigDecimal  payFee;//扣款手续费,用户扣款费率，单位： %，如2.8',
    private BigDecimal  backFee;//代付手续费	用户还款费率,单位：元/笔,保留两位小数',
    private String  channelRespResult;//通道响应结果',
    private String  crossRespResult;//'cross响应结果',
    private String bussType;//'b1：业务登记 ,b2:绑定银行卡，b3：业务开通
    private Integer status;//状态 0：success ,1:fail',
    private Date   createTime;//创建时间',
    private Date   updateTime;//更新时间',
    @TableField(exist = false)
    private Collection<MerchantRateTable> merchantRateTableCollect;

    public RegisterCollectTable setMerchantRateTableCollect(Collection<MerchantRateTable> merchantRateTableCollect) {
        this.merchantRateTableCollect = merchantRateTableCollect;
        return this;
    }

    public RegisterCollectTable setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public RegisterCollectTable setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
        return this;
    }

    public RegisterCollectTable setBussType(String bussType) {
        this.bussType = bussType;
        return this;
    }

    public RegisterCollectTable setId(Long id) {
        this.id = id;
        return this;
    }

    public RegisterCollectTable setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
        return this;
    }

    public RegisterCollectTable setRitId(Long ritId) {
        this.ritId = ritId;
        return this;
    }

    public RegisterCollectTable setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public RegisterCollectTable setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
        return this;
    }

    public RegisterCollectTable setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
        return this;
    }

    public RegisterCollectTable setCategory(String category) {
        this.category = category;
        return this;
    }

    public RegisterCollectTable setMiMerCertPic1(byte[] miMerCertPic1) {
        this.miMerCertPic1 = miMerCertPic1;
        return this;
    }

    public RegisterCollectTable setMiMerCertPic2(byte[] miMerCertPic2) {
        this.miMerCertPic2 = miMerCertPic2;
        return this;
    }

    public RegisterCollectTable setBankCode(String bankCode) {
        this.bankCode = bankCode;
        return this;
    }

    public RegisterCollectTable setBankCardType(Integer bankCardType) {
        this.bankCardType = bankCardType;
        return this;
    }

    public RegisterCollectTable setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
        return this;
    }

    public RegisterCollectTable setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
        return this;
    }

    public RegisterCollectTable setBankCardPhone(String bankCardPhone) {
        this.bankCardPhone = bankCardPhone;
        return this;
    }

    public RegisterCollectTable setPayFee(BigDecimal payFee) {
        this.payFee = payFee;
        return this;
    }

    public RegisterCollectTable setBackFee(BigDecimal backFee) {
        this.backFee = backFee;
        return this;
    }

    public RegisterCollectTable setChannelRespResult(String channelRespResult) {
        this.channelRespResult = channelRespResult;
        return this;
    }

    public RegisterCollectTable setCrossRespResult(String crossRespResult) {
        this.crossRespResult = crossRespResult;
        return this;
    }

    public RegisterCollectTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public RegisterCollectTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public RegisterCollectTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public RegisterCollectTable setBankAccountProp(Integer bankAccountProp) {
        this.bankAccountProp = bankAccountProp;
        return this;
    }
}
