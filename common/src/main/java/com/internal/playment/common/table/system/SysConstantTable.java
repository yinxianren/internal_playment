package com.internal.playment.common.table.system;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-15 
 */

@Data
@TableName ("1_sys_constant_table" )
public class SysConstantTable  implements Serializable {


	@TableId(type = IdType.UUID)
	private String id;

	private String name; //常量名称

	private String firstValue; //常量值

	private String secondValue; //常量值

	private String groupCode; //常量组别

	private Long sortValue; //排序

	private Date createTime; //创建时间

	private Date updateTime; //修改时间

	private Integer status; //启用状态 0启用，1禁用

}
