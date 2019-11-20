package com.internal.playment.db.service.impl.agent;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.agent.ApiAgentPrivielgesService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.agent.AgentPrivielgesTable;
import com.internal.playment.db.service.db.agent.AgentPrivielgesDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiAgentPrivielgesServiceImpl implements ApiAgentPrivielgesService, NewPayAssert {

    @Autowired
    private AgentPrivielgesDBService agentPrivielgesDBService;
    @Override
    public boolean saveOrUpdate(AgentPrivielgesTable agentPrivielgesTable) {
        if (isNull(agentPrivielgesTable)) return false;
        return agentPrivielgesDBService.saveOrUpdate(agentPrivielgesTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return false;
        return agentPrivielgesDBService.removeByIds(ids);
    }

    @Override
    public IPage page(AgentPrivielgesTable agentPrivielgesTable, PageDTO pageDTO) {
        if (isNull(agentPrivielgesTable)) return new Page();
        LambdaQueryWrapper<AgentPrivielgesTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return agentPrivielgesDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<AgentPrivielgesTable> getList(AgentPrivielgesTable agentPrivielgesTable) {
        if (isNull(agentPrivielgesTable)) return agentPrivielgesDBService.list();
        LambdaQueryWrapper<AgentPrivielgesTable> queryWrapper = new LambdaQueryWrapper<>();
        return agentPrivielgesDBService.list(queryWrapper);
    }
}
