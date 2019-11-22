package com.internal.playment.db.service.impl.agent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.agent.ApiAgentMerchantsDetailsService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.agent.AgentMerchantsDetailsTable;
import com.internal.playment.db.service.db.agent.AgentMerchantsDetailsDBService;
import lombok.AllArgsConstructor;
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
public class ApiAgentMerchantsDetailsServiceImpl implements ApiAgentMerchantsDetailsService, NewPayAssert {

    private final AgentMerchantsDetailsDBService agentMerchantsDetailsDBService;

    @Override
    public boolean save(AgentMerchantsDetailsTable amd) {
        if(isNull(amd)) return  false;
        return agentMerchantsDetailsDBService.save(amd);
    }

    @Override
    public IPage page(AgentMerchantsDetailsTable amd) {
        if (isNull(amd)) return new Page();
        LambdaQueryWrapper<AgentMerchantsDetailsTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isBlank(amd.getAgentMerchantId())) queryWrapper.eq(AgentMerchantsDetailsTable::getAgentMerchantId,amd.getAgentMerchantId());
        if (!isBlank(amd.getMerOrderId())) queryWrapper.eq(AgentMerchantsDetailsTable::getMerOrderId,amd.getMerOrderId());
        if (!isBlank(amd.getPlatformOrderId())) queryWrapper.eq(AgentMerchantsDetailsTable::getPlatformOrderId,amd.getPlatformOrderId());
        if (!isBlank(amd.getProductId())) queryWrapper.eq(AgentMerchantsDetailsTable::getProductId,amd.getProductId());
        if (!isNull(amd.getBeginTime())) queryWrapper.gt(AgentMerchantsDetailsTable::getUpdateTime,amd.getBeginTime());
        if (!isNull(amd.getEndTime())) queryWrapper.lt(AgentMerchantsDetailsTable::getUpdateTime,amd.getEndTime());
        queryWrapper.orderByDesc(AgentMerchantsDetailsTable::getTimestamp);
        IPage iPage = new Page(amd.getPageNum(),amd.getPageSize());

        return agentMerchantsDetailsDBService.page(iPage,queryWrapper);
    }
}
