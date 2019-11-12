package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantBankRateTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantBankRateMapper;
import com.internal.playment.db.service.db.merchant.MerchantBankRateDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantRateBankRateServiceImpl extends ServiceImpl<AnewMerchantBankRateMapper, MerchantBankRateTable> implements MerchantBankRateDBService {
}
