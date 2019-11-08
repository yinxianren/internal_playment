package com.internal.playment.api.db.agent;


import com.internal.playment.common.table.agent.AgentMerchantWalletTable;

import java.util.List;

public interface ApiAgentMerchantWalletService {

    AgentMerchantWalletTable getOne(AgentMerchantWalletTable amt);

    boolean updateOrSave(AgentMerchantWalletTable amt);

    List<AgentMerchantWalletTable> getList(AgentMerchantWalletTable agentMerchantWalletTable);
}
