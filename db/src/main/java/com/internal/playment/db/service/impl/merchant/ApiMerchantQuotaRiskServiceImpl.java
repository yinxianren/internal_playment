package com.internal.playment.db.service.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.merchant.ApiMerchantQuotaRiskService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.internal.playment.db.service.db.merchant.MerchantQuotaRiskDBService;
import lombok.AllArgsConstructor;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiMerchantQuotaRiskServiceImpl implements ApiMerchantQuotaRiskService, NewPayAssert {

    private final MerchantQuotaRiskDBService merchantQuotaRiskDBService;


    @Override
    public MerchantQuotaRiskTable getOne(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return null;
        LambdaQueryWrapper<MerchantQuotaRiskTable> lambdaQueryWrapper = new QueryWrapper<MerchantQuotaRiskTable>().lambda();
        if( !isNull(mqr.getStatus())) lambdaQueryWrapper.eq(MerchantQuotaRiskTable::getStatus,mqr.getStatus());
        if( !isBlank(mqr.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantQuotaRiskTable::getMerchantId,mqr.getMerchantId());

        return merchantQuotaRiskDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<MerchantQuotaRiskTable> getList(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return null;
        LambdaQueryWrapper<MerchantQuotaRiskTable> lambdaQueryWrapper = new QueryWrapper<MerchantQuotaRiskTable>().lambda();
        if( !isNull(mqr.getStatus())) lambdaQueryWrapper.eq(MerchantQuotaRiskTable::getStatus,mqr.getStatus());
        if( !isBlank(mqr.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantQuotaRiskTable::getMerchantId,mqr.getMerchantId());
        return merchantQuotaRiskDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return false;
        return merchantQuotaRiskDBService.save(mqr);
    }

    @Override
    public boolean updateByPrimaryKey(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return false;
        return merchantQuotaRiskDBService.updateById(mqr);
    }
}
