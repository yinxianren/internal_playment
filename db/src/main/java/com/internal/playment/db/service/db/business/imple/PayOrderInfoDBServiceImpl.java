package com.internal.playment.db.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.db.mapper.business.AnewPayOrderInfoMapper;
import com.internal.playment.db.service.db.business.PayOrderInfoDBService;
import org.springframework.stereotype.Service;

@Service
public class PayOrderInfoDBServiceImpl extends ServiceImpl<AnewPayOrderInfoMapper, PayOrderInfoTable> implements PayOrderInfoDBService {
}
