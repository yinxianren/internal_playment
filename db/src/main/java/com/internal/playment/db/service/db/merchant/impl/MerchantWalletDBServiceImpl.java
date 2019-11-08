package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantWalletMapper;
import com.internal.playment.db.service.db.merchant.MerchantWalletDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantWalletDBServiceImpl extends ServiceImpl<AnewMerchantWalletMapper, MerchantWalletTable> implements MerchantWalletDBService {
}
