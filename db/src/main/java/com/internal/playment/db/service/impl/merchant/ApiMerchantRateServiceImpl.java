package com.internal.playment.db.service.impl.merchant;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.merchant.ApiMerchantRateService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.internal.playment.db.service.db.merchant.MerchantRateDBService;
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
public class ApiMerchantRateServiceImpl implements ApiMerchantRateService, NewPayAssert {

    private final MerchantRateDBService merchantRateDBService;

    @Override
    public MerchantRateTable getOne(MerchantRateTable mr) {
        if(isNull(mr)) return null;
        LambdaQueryWrapper<MerchantRateTable> lambdaQueryWrapper = new QueryWrapper<MerchantRateTable>().lambda();
        if( !isBlank(mr.getMerchantId())) lambdaQueryWrapper.eq(MerchantRateTable::getMerchantId,mr.getMerchantId());
        if( !isBlank(mr.getProductId())) lambdaQueryWrapper.eq(MerchantRateTable::getProductId,mr.getProductId());
        if( !isBlank(mr.getChannelId())) lambdaQueryWrapper.eq(MerchantRateTable::getChannelId,mr.getChannelId());
        if( !isNull(mr.getStatus())) lambdaQueryWrapper.eq(MerchantRateTable::getStatus,mr.getStatus());
        return merchantRateDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(MerchantRateTable mr) {
        if(isNull(mr)) return false;
        return merchantRateDBService.save(mr);
    }

    @Override
    public Boolean batchSaveOrUpdate(List<MerchantRateTable> rateTables) {
        if(isHasNotElement(rateTables)) return false;
        return merchantRateDBService.saveOrUpdateBatch(rateTables);
    }

    @Override
    public List<MerchantRateTable> getList(MerchantRateTable mer) {
        if (isNull(mer)) return merchantRateDBService.list();
        LambdaQueryWrapper<MerchantRateTable> queryWrapper = new QueryWrapper<MerchantRateTable>().lambda();
        if (!isBlank(mer.getMerchantId()) )  queryWrapper.eq(MerchantRateTable::getMerchantId,mer.getMerchantId());
        if( !isNull(mer.getStatus()) )       queryWrapper.eq(MerchantRateTable::getStatus,mer.getStatus());
        return merchantRateDBService.list(queryWrapper);
    }
}
