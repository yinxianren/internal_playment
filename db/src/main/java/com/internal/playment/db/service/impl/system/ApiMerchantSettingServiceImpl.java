package com.internal.playment.db.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.system.ApiMerchantSettingService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.db.service.db.system.MerchantSettingDbService;
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
public class ApiMerchantSettingServiceImpl implements ApiMerchantSettingService, NewPayAssert {


    private final MerchantSettingDbService merchantSettingDbService;


    @Override
    public List<MerchantSettingTable> getList(MerchantSettingTable mst) {
        if(isNull(mst)) return null;
        LambdaQueryWrapper<MerchantSettingTable> lambdaQueryWrapper = new QueryWrapper<MerchantSettingTable>()
                .lambda()
                .eq(MerchantSettingTable::getStatus, StatusEnum._0.getStatus()); //默认取可用的

        if( !isBlank(mst.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantSettingTable::getMerchantId,mst.getMerchantId());
        lambdaQueryWrapper.orderByAsc(MerchantSettingTable::getStatus);
        return merchantSettingDbService.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean batchSaveOrUpdate(List<MerchantSettingTable> merchantSettingTables) {
        if (isHasNotElement(merchantSettingTables)) return false;
        return merchantSettingDbService.saveOrUpdateBatch(merchantSettingTables);
    }

    @Override
    public Boolean remove(MerchantSettingTable mst) {
        if (isNull(mst)) return false;
        LambdaQueryWrapper<MerchantSettingTable> queryWrapper = new QueryWrapper<MerchantSettingTable>().lambda();
        queryWrapper.eq(MerchantSettingTable::getMerchantId,mst.getMerchantId());
        return merchantSettingDbService.remove(queryWrapper);
    }


}
