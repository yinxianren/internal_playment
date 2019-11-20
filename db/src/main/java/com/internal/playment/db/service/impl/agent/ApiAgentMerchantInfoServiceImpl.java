package com.internal.playment.db.service.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.agent.ApiAgentMerchantInfoService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
import com.internal.playment.db.service.db.agent.AgentMerchantInfoDbService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.dubbo.config.annotation.Service;
/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiAgentMerchantInfoServiceImpl implements ApiAgentMerchantInfoService, NewPayAssert {

    private final AgentMerchantInfoDbService agentMerchantInfoDbService;

    @Override
    public AgentMerchantInfoTable getOne(AgentMerchantInfoTable ami) {
        if(isNull(ami)) return null;
        LambdaQueryWrapper<AgentMerchantInfoTable> lambdaQueryWrapper =new QueryWrapper<AgentMerchantInfoTable>().lambda();
        if( !isBlank(ami.getAgentMerchantId()) ) lambdaQueryWrapper.eq(AgentMerchantInfoTable::getAgentMerchantId,ami.getAgentMerchantId());

        return agentMerchantInfoDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(AgentMerchantInfoTable ami) {
        if(isNull(ami)) return false;
        return agentMerchantInfoDbService.save(ami);
    }

    @Override
    public Boolean saveOrUpdate(AgentMerchantInfoTable agentMerchantInfoTable) {
        if (isNull(agentMerchantInfoTable)) return false;
        return agentMerchantInfoDbService.saveOrUpdate(agentMerchantInfoTable);
    }

    @Override
    public List<AgentMerchantInfoTable> list(AgentMerchantInfoTable agentMerchantInfoTable) {
        if (isNull(agentMerchantInfoTable)) return agentMerchantInfoDbService.list();
        LambdaQueryWrapper<AgentMerchantInfoTable> queryWrapper = new QueryWrapper<AgentMerchantInfoTable>().lambda();
        if (!isNull(agentMerchantInfoTable.getStatus())) queryWrapper.eq(AgentMerchantInfoTable::getStatus,agentMerchantInfoTable.getStatus());
        if (!isBlank(agentMerchantInfoTable.getAgentMerchantId())) queryWrapper.eq(AgentMerchantInfoTable::getAgentMerchantId,agentMerchantInfoTable.getAgentMerchantId());
        if (!isBlank(agentMerchantInfoTable.getAgentMerchantName())) queryWrapper.eq(AgentMerchantInfoTable::getAgentMerchantName,agentMerchantInfoTable.getAgentMerchantName());
        return agentMerchantInfoDbService.list(queryWrapper);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return agentMerchantInfoDbService.removeByIds(ids);
    }

    @Override
    public List<AgentMerchantInfoTable> listByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return new ArrayList<>() ;
        LambdaQueryWrapper<AgentMerchantInfoTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AgentMerchantInfoTable::getId,ids);
        return agentMerchantInfoDbService.list(queryWrapper);
    }
}
