package com.internal.playment.db.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.db.mapper.agent.AnewAgentMerchantWalletMapper;
import com.internal.playment.db.service.db.agent.AgentMerchantWalletDBService;
import org.springframework.stereotype.Service;

@Service
public class AgentMerchantWalletDBServiceImpl extends ServiceImpl<AnewAgentMerchantWalletMapper, AgentMerchantWalletTable> implements AgentMerchantWalletDBService {
}
