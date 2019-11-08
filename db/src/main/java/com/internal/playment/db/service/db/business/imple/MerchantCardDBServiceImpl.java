package com.internal.playment.db.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.db.mapper.business.AnewMerchantCardMapper;
import com.internal.playment.db.service.db.business.MerchantCardDBService;
import org.springframework.stereotype.Service;

@Service
public class MerchantCardDBServiceImpl extends ServiceImpl<AnewMerchantCardMapper, MerchantCardTable> implements MerchantCardDBService {
}
