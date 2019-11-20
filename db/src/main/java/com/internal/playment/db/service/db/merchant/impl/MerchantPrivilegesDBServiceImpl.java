package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantPrivilegesTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantPrivilegesMapper;
import com.internal.playment.db.service.db.merchant.MerchantPrivilegesDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantPrivilegesDBServiceImpl extends ServiceImpl<AnewMerchantPrivilegesMapper, MerchantPrivilegesTable> implements MerchantPrivilegesDBService {
}
