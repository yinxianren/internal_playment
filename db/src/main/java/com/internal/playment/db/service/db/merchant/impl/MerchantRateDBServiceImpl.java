package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantRateMapper;
import com.internal.playment.db.service.db.merchant.MerchantRateDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantRateDBServiceImpl extends ServiceImpl<AnewMerchantRateMapper, MerchantRateTable> implements MerchantRateDBService {
}
