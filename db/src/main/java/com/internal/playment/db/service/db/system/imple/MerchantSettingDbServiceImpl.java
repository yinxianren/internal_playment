package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.db.mapper.system.AnewMerchantSettingMapper;
import com.internal.playment.db.service.db.system.MerchantSettingDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午1:54
 * Description:
 */
@Service
public class MerchantSettingDbServiceImpl extends ServiceImpl<AnewMerchantSettingMapper, MerchantSettingTable> implements MerchantSettingDbService {
}
