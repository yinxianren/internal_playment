package com.internal.playment.common.table.system;

import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-18 
 */

@Data
@TableName ("1_merchant_sys_log_table" )
public class MerchantSysLogTable  implements Serializable {


	private Long id;

	private Integer type; //常量组log提供

	private String operator; //操作者

	private Date startTime; //操作时间

	private Long spendTime; //方法用时（毫秒）

	private String requestIp; //操作IP

	private String requestUri; //请求uri

	private String methodName; //方法名称

	private String methodDescription; //方法描述，如是异常则是异常描述

	private String message; //信息/异常栈信息（10条栈信息）

}
