package com.internal.playment.api.db.agent;


import com.internal.playment.common.table.agent.AgentMerchantInfoTable;

import java.util.List;

public interface ApiAgentMerchantInfoService {

    AgentMerchantInfoTable getOne(AgentMerchantInfoTable ami);

    boolean save(AgentMerchantInfoTable ami);

    Boolean saveOrUpdate(AgentMerchantInfoTable agentMerchantInfoTable);

    List<AgentMerchantInfoTable> list(AgentMerchantInfoTable agentMerchantInfoTable);

    Boolean delByIds(List<String> ids);

    List<AgentMerchantInfoTable> listByIds(List<Long> ids);

}
