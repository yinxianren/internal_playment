package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantRoleMapper;
import com.internal.playment.db.service.db.merchant.MerchantRoleDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantRoleDBServiceImpl extends ServiceImpl<AnewMerchantRoleMapper, MerchantRoleTable> implements MerchantRoleDBService {
}
