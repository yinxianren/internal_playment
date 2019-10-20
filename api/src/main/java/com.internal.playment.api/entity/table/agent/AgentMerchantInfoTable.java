package com.internal.playment.api.entity.table.agent;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 上午9:21
 * Description:
 */

@Data
@TableName("agentMerchantInfoTable")
public class AgentMerchantInfoTable {
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
    private String createTime ;//创建时间
    private String updateTime ;//更新时间
}
