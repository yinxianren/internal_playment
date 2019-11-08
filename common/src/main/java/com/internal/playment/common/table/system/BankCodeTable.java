package com.internal.playment.common.table.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-10-31 
 */

@TableName ( "1_bank_code_table" )
@Data
public class BankCodeTable  implements Serializable {
	@TableId(type= IdType.AUTO)
	private Long id;//表主键
	private String bankCode;//银行编码
	private String bankShortName;//银行名简称
	private String bankFullName;//银行名全称
	private Integer status;//0:可用，1：不可用
	private Date tradeTime;//交易时间
	private Date createTime;//创建时间

}
