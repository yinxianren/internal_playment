package com.internal.playment.db.service.impl.agent;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.agent.ApiAgentUserService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.agent.AgentUserTable;
import com.internal.playment.db.service.db.agent.AgentUserDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiAgentUserServiceImpl implements ApiAgentUserService, NewPayAssert {

    @Autowired
    private AgentUserDBService agentUserDBService;

    @Override
    public boolean saveOrUpdate(AgentUserTable agentUserTable) {
        if (isNull(agentUserTable)) return false;
        return agentUserDBService.saveOrUpdate(agentUserTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return false;
        return agentUserDBService.removeByIds(ids);
    }

    @Override
    public IPage page(AgentUserTable agentUserTable, PageDTO pageDTO) {
        if (isNull(agentUserTable)) return new Page();
        LambdaQueryWrapper<AgentUserTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return agentUserDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<AgentUserTable> getList(AgentUserTable agentUserTable) {
        if (isNull(agentUserTable)) return agentUserDBService.list();
        LambdaQueryWrapper<AgentUserTable> queryWrapper = new LambdaQueryWrapper<>();
        return agentUserDBService.list(queryWrapper);
    }
}
