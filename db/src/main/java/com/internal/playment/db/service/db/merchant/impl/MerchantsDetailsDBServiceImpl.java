package com.internal.playment.db.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.merchant.MerchantsDetailsTable;
import com.internal.playment.db.mapper.merchant.AnewMerchantsDetailsMapper;
import com.internal.playment.db.service.db.merchant.MerchantsDetailsDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantsDetailsDBServiceImpl extends ServiceImpl<AnewMerchantsDetailsMapper, MerchantsDetailsTable> implements MerchantsDetailsDBService {
}
