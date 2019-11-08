package com.internal.playment.db.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.internal.playment.db.mapper.agent.AnewAgentMerchantSettingMapper;
import com.internal.playment.db.service.db.agent.AgentMerchantSettingDBService;
import org.springframework.stereotype.Service;

@Service
public class AgentMerchantSettingDBServiceImpl extends ServiceImpl<AnewAgentMerchantSettingMapper, AgentMerchantSettingTable> implements AgentMerchantSettingDBService {
}
