package com.internal.playment.common.table.system;

import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-15 
 */

@Data
@TableName ("1_sys_group_table" )
public class SysGroupTable  implements Serializable {

	private Long id;

	private String name; //常量组名称

	private String code; //常量组编码

	private Integer model; //1:系统 2:商户 3:代理商

	private Integer system; //0: 否 1: 是

	private Date createTime; //创建时间

	private Date updateTime; //修改时间

	private Integer status; //启用状态 0启用，1禁用

}
