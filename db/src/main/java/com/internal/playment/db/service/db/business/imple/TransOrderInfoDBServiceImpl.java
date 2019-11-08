package com.internal.playment.db.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.db.mapper.business.AnewTransOrderInfoMapper;
import com.internal.playment.db.service.db.business.TransOrderInfoDBService;
import org.springframework.stereotype.Service;

@Service
public class TransOrderInfoDBServiceImpl extends ServiceImpl<AnewTransOrderInfoMapper, TransOrderInfoTable> implements TransOrderInfoDBService {
}
