package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.MerchantSysLogTable;
import com.internal.playment.db.mapper.system.AnewMerchantSysLogMapper;
import com.internal.playment.db.service.db.system.MerchantSysLogDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantSysLogDBServiceImpl extends ServiceImpl<AnewMerchantSysLogMapper, MerchantSysLogTable> implements MerchantSysLogDBService {
}
