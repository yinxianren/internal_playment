package com.internal.playment.common.table.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午10:44
 * Description:
 */
@ToString
@TableName("6_register_info_table")
@Getter
public class RegisterInfoTable implements Serializable {
    @TableId(type= IdType.INPUT)
    private Long id ;//表主键,
    private String merchantId ;// 商户号,
    private String terminalMerId;// 终端商户号,
    private String terminalMerName ;// 终端商户名,
    private String userName ;// 身份证用户名,
    private String userShortName ;// 用户名简称,
    private Integer identityType ;//身份证类型，1身份证、2护照、3港澳回乡证、4台胞证、5军官证,
    private String  identityNum ;// 证件号,
    private String  phone ;// 手机号,
    private String  merchantType ;// 用户类型：00公司商户，01个体商户,
    private String  province ;// 省份编号,
    private String  city ;// 城市编号,
    private String  address ;// 详细地址,
    private Integer status ;// 0：启用 ,1:禁用，2：未审核,
    private Date createTime ;// 创建时间,
    private Date  updateTime ;// 更新时间,


    public RegisterInfoTable setId(Long id) {
        this.id = id;
        return this;
    }

    public RegisterInfoTable setMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public RegisterInfoTable setTerminalMerId(String terminalMerId) {
        this.terminalMerId = terminalMerId;
        return this;
    }

    public RegisterInfoTable setTerminalMerName(String terminalMerName) {
        this.terminalMerName = terminalMerName;
        return this;
    }

    public RegisterInfoTable setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RegisterInfoTable setUserShortName(String userShortName) {
        this.userShortName = userShortName;
        return this;
    }

    public RegisterInfoTable setIdentityType(Integer identityType) {
        this.identityType = identityType;
        return this;
    }

    public RegisterInfoTable setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
        return this;
    }

    public RegisterInfoTable setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public RegisterInfoTable setMerchantType(String merchantType) {
        this.merchantType = merchantType;
        return this;
    }

    public RegisterInfoTable setProvince(String province) {
        this.province = province;
        return this;
    }

    public RegisterInfoTable setCity(String city) {
        this.city = city;
        return this;
    }

    public RegisterInfoTable setAddress(String address) {
        this.address = address;
        return this;
    }

    public RegisterInfoTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public RegisterInfoTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public RegisterInfoTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
