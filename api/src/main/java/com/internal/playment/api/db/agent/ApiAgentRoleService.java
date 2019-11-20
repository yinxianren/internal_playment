package com.internal.playment.api.db.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.agent.AgentRoleTable;

import java.util.List;

public interface ApiAgentRoleService {
    boolean saveOrUpdate(AgentRoleTable agentRoleTable);

    boolean delByIds(List<Long> ids);

    IPage page(AgentRoleTable agentRoleTable, PageDTO pageDTO);

    List<AgentRoleTable> getList(AgentRoleTable agentRoleTable);
}
