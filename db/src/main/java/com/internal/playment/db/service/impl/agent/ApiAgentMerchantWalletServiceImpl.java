package com.internal.playment.db.service.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.agent.ApiAgentMerchantWalletService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.db.service.db.agent.AgentMerchantWalletDBService;
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
public class ApiAgentMerchantWalletServiceImpl implements ApiAgentMerchantWalletService, NewPayAssert {

   private final AgentMerchantWalletDBService agentMerchantWalletDBService;

    @Override
    public AgentMerchantWalletTable getOne(AgentMerchantWalletTable amw) {
       if(isNull(amw)) return null;
        LambdaQueryWrapper<AgentMerchantWalletTable> lambdaQueryWrapper = new QueryWrapper<AgentMerchantWalletTable>()
                .lambda().eq(AgentMerchantWalletTable::getStatus, StatusEnum._0.getStatus());
        if(!isBlank(amw.getAgentMerchantId())) lambdaQueryWrapper.eq(AgentMerchantWalletTable::getAgentMerchantId,amw.getAgentMerchantId());
        return agentMerchantWalletDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateOrSave(AgentMerchantWalletTable amw) {
        if(isNull(amw)) return false;
        return agentMerchantWalletDBService.saveOrUpdate(amw);
    }

    @Override
    public List<AgentMerchantWalletTable> getList(AgentMerchantWalletTable agentMerchantWalletTable) {
        LambdaQueryWrapper<AgentMerchantWalletTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(agentMerchantWalletTable.getAgentMerchantId())) queryWrapper.eq(AgentMerchantWalletTable::getAgentMerchantId,agentMerchantWalletTable.getAgentMerchantId());
        return agentMerchantWalletDBService.list(queryWrapper);
    }
}
