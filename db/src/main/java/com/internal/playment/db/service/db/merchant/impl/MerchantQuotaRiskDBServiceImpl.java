package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantQuotaRiskMapper;
import com.internal.playment.db.service.db.merchant.MerchantQuotaRiskDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantQuotaRiskDBServiceImpl extends ServiceImpl<AnewMerchantQuotaRiskMapper, MerchantQuotaRiskTable> implements MerchantQuotaRiskDBService {
}
