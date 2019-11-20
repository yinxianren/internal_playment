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
@TableName ("3_agment_user_table" )
public class AgentUserTable implements Serializable {


	private Long id;

	private String userName; //用户名称

	private String password; //用户密码

	private String realName; //真实姓名

	private Long orderNo; //排序号

	private String deptId; //部门

	private String email; //邮箱

	private String telphone; //电话

	private String mobile; //手机

	private Integer adminState; //管理员

	private String lastLogonIp; //登陆IP

	private Long roleId; //角色ID

	private String belongto;

	private String sessionId; //会话ID

	private Integer available; //是否可用

	private String remark; //备注

	private String creator; //操作人

	private Date createTime; //创建时间

	private String modifier; //更新人

	private Date modifyTime; //更新时间

}
