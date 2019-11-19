package com.internal.playment.common.table.merchant;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Description  
 * @Author  monkey
 * @Date 2019-11-19 
 */

@Data
@TableName ("2_merchant_acount_table" )
public class MerchantAcountTable  implements Serializable {

	@TableId(type= IdType.AUTO)
	private Long id;

	private String merId;

	private String benefitName;

	private String bankName;

	private String bankcardNum;

	private Long bankcardType;

	private String bankBranchName;

	private String bankBranchNum;

	private Long identityType;

	private String identityNum;

	private String identityUrl1;

	private String identityUrl2;

}
