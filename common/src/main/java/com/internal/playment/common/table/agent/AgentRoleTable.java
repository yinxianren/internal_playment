package com.internal.playment.common.table.agent;

import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-19 
 */

@Data
@TableName ("3_agment_role_table" )
public class AgentRoleTable implements Serializable {


	private Long id; //ID

	private String role; //角色名称（系统）ROLE_INTERNAL_ADMIN（内部管理员）ROLE_INTERNAL_USER（内部用户）ROLE_MERCHANT_ADMIN（商户管理员）ROLE_MERCHANT_USER（商户用户）

	private String roleName; //角色名称（用户，可自定义）

	private String belongto;

	private String privilegesId; //拥有权限ID，以数组形式保存。

	private Integer available; //是否可用

	private String remark; //备注

	private String creator; //创建人

	private Date createTime; //创建时间

	private String modifier; //修改人

	private Date modifyTime; //更新时间

}
