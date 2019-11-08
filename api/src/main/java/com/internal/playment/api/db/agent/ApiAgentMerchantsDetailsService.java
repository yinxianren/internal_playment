package com.internal.playment.api.db.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.table.agent.AgentMerchantsDetailsTable;

public interface ApiAgentMerchantsDetailsService {

    boolean save(AgentMerchantsDetailsTable amd);

    IPage page(AgentMerchantsDetailsTable amd);

}
