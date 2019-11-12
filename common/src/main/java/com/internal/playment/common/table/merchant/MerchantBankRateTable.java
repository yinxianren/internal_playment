package com.internal.playment.common.table.merchant;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-12 
 */

@TableName ( "2_merchant_bank_rate_table" )
@Data
public class MerchantBankRateTable  implements Serializable {
   
	private Long id;//主键id
	private String merchantId;//商户id
	private String bankCode;//银行编码
	private String bankName;//银行名称
	private BigDecimal dayMoney;//单日限额
	private BigDecimal singleMoney;//单笔限额
	private Date createTime;//创建时间
	private Date updateTime;//修改时间
	private Integer status;//状态
	private BigDecimal rateFee;//費率

}
