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
@TableName ("3_agment_privielges_table" )
public class AgentPrivielgesTable implements Serializable {

	private Long id; //权限主键

	private String name; //权限名称

	private String description; //权限描述

	private Long parentId; //父级名称

	private String stateName; //页面菜单：声明/描述

	private String iconFont; //页面菜单：图标class

	private Integer available; //是否可用

	private Date createTime; //创建时间

	private Date updateTime; //修改时间

}
