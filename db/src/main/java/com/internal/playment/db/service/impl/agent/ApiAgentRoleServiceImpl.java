package com.internal.playment.db.service.impl.agent;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.agent.ApiAgentRoleService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.agent.AgentRoleTable;
import com.internal.playment.db.service.db.agent.AgentRoleDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiAgentRoleServiceImpl implements ApiAgentRoleService, NewPayAssert {

    @Autowired
    private AgentRoleDBService agentRoleDBService;

    @Override
    public boolean saveOrUpdate(AgentRoleTable agentRoleTable) {
        if (isNull(agentRoleTable)) return false;
        return agentRoleDBService.saveOrUpdate(agentRoleTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return false;
        return agentRoleDBService.removeByIds(ids);
    }

    @Override
    public IPage page(AgentRoleTable agentRoleTable, PageDTO pageDTO) {
        if (isNull(agentRoleTable)) return new Page();
        LambdaQueryWrapper<AgentRoleTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return agentRoleDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<AgentRoleTable> getList(AgentRoleTable agentRoleTable) {
        if (isNull(agentRoleTable)) return agentRoleDBService.list();
        LambdaQueryWrapper<AgentRoleTable> queryWrapper = new LambdaQueryWrapper<>();
        return agentRoleDBService.list(queryWrapper);
    }
}
