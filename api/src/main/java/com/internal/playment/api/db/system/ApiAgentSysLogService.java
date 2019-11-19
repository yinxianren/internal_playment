package com.internal.playment.api.db.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.AgentSysLogTable;

public interface ApiAgentSysLogService {

    boolean saveOrUpdate(AgentSysLogTable agentSysLogTable);

    IPage page(AgentSysLogTable agentSysLogTable, PageDTO pageDTO);
}
