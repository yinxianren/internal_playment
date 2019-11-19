package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantAcountTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantAcountMapper;
import com.internal.playment.db.service.db.merchant.MerchantAcountDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantAcountDBServiceImpl extends ServiceImpl<AnewMerchantAcountMapper, MerchantAcountTable> implements MerchantAcountDBService {
}
