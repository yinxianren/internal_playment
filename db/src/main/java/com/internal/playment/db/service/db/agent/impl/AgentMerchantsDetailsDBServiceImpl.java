package com.internal.playment.db.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.agent.AgentMerchantsDetailsTable;
import com.internal.playment.db.mapper.agent.AnewAgentMerchantDetailsMapper;
import com.internal.playment.db.service.db.agent.AgentMerchantsDetailsDBService;
import org.springframework.stereotype.Service;

@Service
public class AgentMerchantsDetailsDBServiceImpl extends ServiceImpl<AnewAgentMerchantDetailsMapper, AgentMerchantsDetailsTable> implements AgentMerchantsDetailsDBService {
}
