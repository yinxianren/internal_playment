package com.internal.playment.api.db.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.agent.AgentUserTable;

import java.util.List;

public interface ApiAgentUserService {

    boolean saveOrUpdate(AgentUserTable agentUserTable);

    boolean delByIds(List<Long> ids);

    IPage page(AgentUserTable agentUserTable, PageDTO pageDTO);

    List<AgentUserTable> getList(AgentUserTable agentUserTable);
}
