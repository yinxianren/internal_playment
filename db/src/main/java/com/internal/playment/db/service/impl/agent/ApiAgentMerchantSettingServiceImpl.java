package com.internal.playment.db.service.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.agent.ApiAgentMerchantSettingService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.internal.playment.db.service.db.agent.AgentMerchantSettingDBService;
import lombok.AllArgsConstructor;

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
public class ApiAgentMerchantSettingServiceImpl implements ApiAgentMerchantSettingService, NewPayAssert {

    private final AgentMerchantSettingDBService agentMerchantSettingDBService;

    @Override
    public AgentMerchantSettingTable getOne(AgentMerchantSettingTable ams) {
        if(isNull(ams)) return  null;
        LambdaQueryWrapper<AgentMerchantSettingTable> lambdaQueryWrapper = new QueryWrapper<AgentMerchantSettingTable>().lambda();
        if( !isBlank(ams.getAgentMerchantId())) lambdaQueryWrapper.eq(AgentMerchantSettingTable::getAgentMerchantId,ams.getAgentMerchantId());
        if( !isBlank(ams.getProductId())) lambdaQueryWrapper.eq(AgentMerchantSettingTable::getProductId,ams.getProductId());
        if( !isNull(ams.getStatus()) ) lambdaQueryWrapper.eq(AgentMerchantSettingTable::getStatus,ams.getStatus());
        return agentMerchantSettingDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(AgentMerchantSettingTable ams) {
        if(isNull(ams))  return false;
         return agentMerchantSettingDBService.save(ams);
    }

    @Override
    public Boolean update(AgentMerchantSettingTable ams) {
        if(isNull(ams))  return false;
        return agentMerchantSettingDBService.updateById(ams);
    }

    @Override
    public Boolean batchSaveOrUpdate(List<AgentMerchantSettingTable> list) {
        if (isHasNotElement(list)) return false;
        return agentMerchantSettingDBService.saveOrUpdateBatch(list);
    }

    @Override
    public List<AgentMerchantSettingTable> list(AgentMerchantSettingTable agentMerchantSettingTable) {
        if(isNull(agentMerchantSettingTable))  return agentMerchantSettingDBService.list();
        LambdaQueryWrapper<AgentMerchantSettingTable> queryWrapper = new QueryWrapper<AgentMerchantSettingTable>().lambda();
        if (!isBlank(agentMerchantSettingTable.getAgentMerchantId())) queryWrapper.eq(AgentMerchantSettingTable::getAgentMerchantId,agentMerchantSettingTable.getAgentMerchantId());
        return agentMerchantSettingDBService.list(queryWrapper);
    }

    @Override
    public boolean delByAgentIds(List<String> agentIds) {
        if (isHasNotElement(agentIds)) return false;
        LambdaQueryWrapper<AgentMerchantSettingTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AgentMerchantSettingTable::getAgentMerchantId,agentIds);
        return agentMerchantSettingDBService.remove(queryWrapper);
    }
}
