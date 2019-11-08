package com.internal.playment.common.table.agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 上午9:21
 * Description:
 */
@TableName("3_agent_merchant_info_table")
@Getter
public class AgentMerchantInfoTable implements Serializable {
    @TableId(type= IdType.AUTO)
    private Long id;//主键
    private String agentMerchantId ;//代理商ID
    private String agentMerchantName ;//代理商名称
    private String agentMerchantShortName;//代理商简称
    private Integer agentIdentityType;//证件类型： 1身份证、2护照、3港澳回乡证、4台胞证、5军官证
    private String  agentIdentityNum ;//证件号码
    private String  agentIdentityPath;//证件图片存放路径
    private String  phone ;//电话
    private Integer phoneStatus ;//：0已经认证(新增时候默认未认证) ,1:未认证
    private String  email ;//邮箱
    private Integer emailStatus;//0：已经认证(新增时候默认未认证) ,1:未认证
    private String qq ;//qq
    private Integer status ;//商户审核状态 0：启用 ,1:禁用，2：未审核
    private Date createTime ;//创建时间
    private Date updateTime ;//更新时间

    public AgentMerchantInfoTable setId(Long id) {
        this.id = id;
        return this;
    }

    public AgentMerchantInfoTable setAgentMerchantId(String agentMerchantId) {
        this.agentMerchantId = agentMerchantId;
        return this;
    }

    public AgentMerchantInfoTable setAgentMerchantName(String agentMerchantName) {
        this.agentMerchantName = agentMerchantName;
        return this;
    }

    public AgentMerchantInfoTable setAgentMerchantShortName(String agentMerchantShortName) {
        this.agentMerchantShortName = agentMerchantShortName;
        return this;
    }

    public AgentMerchantInfoTable setAgentIdentityType(Integer agentIdentityType) {
        this.agentIdentityType = agentIdentityType;
        return this;
    }

    public AgentMerchantInfoTable setAgentIdentityNum(String agentIdentityNum) {
        this.agentIdentityNum = agentIdentityNum;
        return this;
    }

    public AgentMerchantInfoTable setAgentIdentityPath(String agentIdentityPath) {
        this.agentIdentityPath = agentIdentityPath;
        return this;
    }

    public AgentMerchantInfoTable setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public AgentMerchantInfoTable setPhoneStatus(Integer phoneStatus) {
        this.phoneStatus = phoneStatus;
        return this;
    }

    public AgentMerchantInfoTable setEmail(String email) {
        this.email = email;
        return this;
    }

    public AgentMerchantInfoTable setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
        return this;
    }

    public AgentMerchantInfoTable setQq(String qq) {
        this.qq = qq;
        return this;
    }

    public AgentMerchantInfoTable setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public AgentMerchantInfoTable setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public AgentMerchantInfoTable setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
