package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.system.ApiAgentSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.AgentSysLogTable;
import com.internal.playment.db.service.db.system.AgentSysLogDBService;
import org.springframework.beans.factory.annotation.Autowired;
import sun.security.x509.IPAddressName;

@Service(version = "${application.version}" , timeout = 30000)
public class ApiAgentSysLogServiceImpl implements ApiAgentSysLogService, NewPayAssert {

    @Autowired
    private AgentSysLogDBService agentSysLogDBService;

    @Override
    public boolean saveOrUpdate(AgentSysLogTable agentSysLogTable) {
        if (isNull(agentSysLogTable)) return false;
        return agentSysLogDBService.saveOrUpdate(agentSysLogTable);
    }

    @Override
    public IPage page(AgentSysLogTable agentSysLogTable, PageDTO pageDTO) {
        if (isNull(agentSysLogTable)) return new Page();
        LambdaQueryWrapper<AgentSysLogTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isNull(pageDTO.getBeginTime())) queryWrapper.gt(AgentSysLogTable::getStartTime,pageDTO.getBeginTime());
        if (!isNull(pageDTO.getEndTime())) queryWrapper.lt(AgentSysLogTable::getStartTime,pageDTO.getEndTime());
        queryWrapper.orderByDesc(AgentSysLogTable::getStartTime);
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return agentSysLogDBService.page(iPage,queryWrapper);
    }
}
