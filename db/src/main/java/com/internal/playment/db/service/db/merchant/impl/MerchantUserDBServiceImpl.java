package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantUserTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantUserMapper;
import com.internal.playment.db.service.db.merchant.MerchantUserDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantUserDBServiceImpl extends ServiceImpl<AnewMerchantUserMapper, MerchantUserTable> implements MerchantUserDBService {
}
