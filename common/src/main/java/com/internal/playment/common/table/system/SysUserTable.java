package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-13 
 */

@TableName ( "1_sys_user_table" )
@Data
public class SysUserTable  implements Serializable {
   
	private Long id;
	private String userName;//用户名称
	private String password;//用户密码
	private String realName;//真实姓名
	private Long orderNo;//排序号
	private String deptId;//部门
	private String email;//邮箱
	private String telphone;//电话
	private String mobile;//手机
	private Integer adminState;//管理员
	private String lastLogonIp;//登陆IP
	private Long roleId;//角色ID
	private Long belongto;//所有者： 0-总部，其他-商户id
	private String sessionId;//会话ID
	private Integer available;//是否可用
	private String remark;//备注
	private String creator;//操作人
	private Date createTime;//创建时间
	private String modifier;//更新人
	private Date updateTime;//更新时间

}
