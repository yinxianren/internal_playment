package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.RiskQuotaTable;
import com.internal.playment.db.mapper.system.AnewRiskQuotaMapper;
import com.internal.playment.db.service.db.system.RiskQuotaDBService;
import org.springframework.stereotype.Service;

@Service
public class RiskQuotaDBServiceImpl extends ServiceImpl<AnewRiskQuotaMapper, RiskQuotaTable> implements RiskQuotaDBService {
}
