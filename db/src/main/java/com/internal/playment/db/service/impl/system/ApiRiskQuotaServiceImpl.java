package com.internal.playment.db.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.system.ApiRiskQuotaService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.RiskQuotaTable;
import com.internal.playment.db.service.db.system.RiskQuotaDBService;
import lombok.AllArgsConstructor;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiRiskQuotaServiceImpl implements ApiRiskQuotaService, NewPayAssert {

  private final RiskQuotaDBService riskQuotaDBService;

    @Override
    public List<RiskQuotaTable> getListByTimeType(Set<String> timeTypeSet, RiskQuotaTable rq) {
        if(isHasNotElement(timeTypeSet)) return null;
        LambdaQueryWrapper<RiskQuotaTable> lambdaQueryWrapper = new QueryWrapper<RiskQuotaTable>()
                .lambda().in(RiskQuotaTable::getTimeType,timeTypeSet);
        if( !isBlank(rq.getMeridChannelid()) )  lambdaQueryWrapper.eq(RiskQuotaTable::getMeridChannelid,rq.getMeridChannelid());
        if( !isBlank(rq.getBussType()) )  lambdaQueryWrapper.eq(RiskQuotaTable::getBussType,rq.getBussType());
        return riskQuotaDBService.list(lambdaQueryWrapper);
    }

    @Override
    public List<RiskQuotaTable> getListByChMerId(Set<String> meridChannelidSet, RiskQuotaTable rq) {
        if(isHasNotElement(meridChannelidSet)) return null;
        LambdaQueryWrapper<RiskQuotaTable> lambdaQueryWrapper = new QueryWrapper<RiskQuotaTable>()
                .lambda().in(RiskQuotaTable::getMeridChannelid,meridChannelidSet);
        if( !isBlank(rq.getBussType()) )  lambdaQueryWrapper.eq(RiskQuotaTable::getBussType,rq.getBussType());
        return riskQuotaDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(RiskQuotaTable rq) {
        if(isNull(rq)) return  false;
        return riskQuotaDBService.save(rq);
    }

    @Override
    public boolean updateByPrimaryKey(RiskQuotaTable rq) {
        if(isNull(rq)) return  false;
        return riskQuotaDBService.updateById(rq);
    }
}
