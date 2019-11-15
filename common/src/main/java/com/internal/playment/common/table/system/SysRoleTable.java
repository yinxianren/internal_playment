package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-13 
 */

@TableName ( "1_sys_role_table" )
@Data
public class SysRoleTable implements Serializable {
   
	private Long id;//ID
	private String role;//角色名称（系统）ROLE_INTERNAL_ADMIN（内部管理员）ROLE_INTERNAL_USER（内部用户）ROLE_MERCHANT_ADMIN（商户管理员）ROLE_MERCHANT_USER（商户用户）
	private String roleName;//角色名称（用户，可自定义）
	private Long belongto;//属于，内部（0）/商户号
	private String privilegesId;//拥有权限ID，以数组形式保存。
	@TableField(exist = false)
	private Boolean available;//是否可用
	private String remark;//备注
	private String creator;//创建人
	private Date createTime;//创建时间
	private String modifier;//修改人
	private Date updateTime;//更新时间
	private Integer status;//是否可用
	@TableField(exist = false)
	private List<SysPrivilegesTable> privileges;

}
