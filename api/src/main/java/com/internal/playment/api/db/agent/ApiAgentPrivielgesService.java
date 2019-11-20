package com.internal.playment.api.db.agent;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.agent.AgentPrivielgesTable;

import java.util.List;

public interface ApiAgentPrivielgesService {
    boolean saveOrUpdate(AgentPrivielgesTable agentPrivielgesTable);

    boolean delByIds(List<Long> ids);

    IPage page(AgentPrivielgesTable agentPrivielgesTable, PageDTO pageDTO);

    List<AgentPrivielgesTable> getList(AgentPrivielgesTable agentPrivielgesTable);
}
