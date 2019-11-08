package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantInfoMapper;
import com.internal.playment.db.service.db.merchant.MerchantInfoDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午10:06
 * Description:
 */
@Service
public class MerchantInfoDbServiceImpl  extends ServiceImpl<AnewMerchantInfoMapper, MerchantInfoTable>  implements MerchantInfoDbService {
}
