package com.internal.playment.common.table.system;

import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-20 
 */

@Data
@TableName ("1_user_login_ip_table" )
public class UserLoginIpTable  implements Serializable {


	private Long id;

	private String customerId; //代理/商户号

	private String loginIp; //ip

	private Date addTime; //添加时间

	private Date updateTime; //修改时间

}
