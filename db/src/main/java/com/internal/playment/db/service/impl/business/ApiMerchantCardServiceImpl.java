package com.internal.playment.db.service.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.business.ApiMerchantCardService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.db.service.db.business.MerchantCardDBService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

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
public class ApiMerchantCardServiceImpl implements ApiMerchantCardService, NewPayAssert {

    private final MerchantCardDBService merchantCardDBService;

    @Override
    public MerchantCardTable getOne(MerchantCardTable mct) {
        if(isNull(mct)) return null;
        LambdaQueryWrapper<MerchantCardTable> lambdaQueryWrapper = new QueryWrapper<MerchantCardTable>() .lambda();
        if( !isNull(mct.getStatus()) )  lambdaQueryWrapper.eq(MerchantCardTable::getStatus,mct.getStatus());
        if( !isBlank(mct.getMerOrderId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerOrderId,mct.getMerOrderId());
        if( !isBlank(mct.getMerchantId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerchantId,mct.getMerchantId());
        if( !isBlank(mct.getTerminalMerId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getTerminalMerId,mct.getTerminalMerId());
        if( !isBlank(mct.getBankCardNum()) )  lambdaQueryWrapper.eq(MerchantCardTable::getBankCardNum,mct.getBankCardNum());
        if( !isBlank(mct.getBussType()) )  lambdaQueryWrapper.eq(MerchantCardTable::getBussType,mct.getBussType());
        if( !isBlank(mct.getPlatformOrderId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getPlatformOrderId,mct.getPlatformOrderId());
        if( !isBlank(mct.getOrganizationId()) ) lambdaQueryWrapper.eq(MerchantCardTable::getOrganizationId,mct.getOrganizationId());
        return merchantCardDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<MerchantCardTable> getList(MerchantCardTable mct) {
        if(isNull(mct)) return null;
        LambdaQueryWrapper<MerchantCardTable> lambdaQueryWrapper = new QueryWrapper<MerchantCardTable>() .lambda();
        if( !isNull(mct.getStatus()) )  lambdaQueryWrapper.eq(MerchantCardTable::getStatus,mct.getStatus());
        if( !isBlank(mct.getMerOrderId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerOrderId,mct.getMerOrderId());
        if( !isBlank(mct.getMerchantId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerchantId,mct.getMerchantId());
        if( !isBlank(mct.getTerminalMerId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getTerminalMerId,mct.getTerminalMerId());
        if( !isBlank(mct.getBankCardNum()) )  lambdaQueryWrapper.eq(MerchantCardTable::getBankCardNum,mct.getBankCardNum());
        if( !isBlank(mct.getBussType()) )  lambdaQueryWrapper.eq(MerchantCardTable::getBussType,mct.getBussType());
        return  merchantCardDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean updateById(MerchantCardTable mct) {
        if(isNull(mct)) return false;
        return merchantCardDBService.updateById(mct);
    }

    public boolean bachUpdateById(List<MerchantCardTable> list){
        if(isHasNotElement(list)) return false;
        return merchantCardDBService.updateBatchById(list);
    }

    @Override
    public boolean save(MerchantCardTable mct) {
        if(isNull(mct)) return false;
        return merchantCardDBService.save(mct);
    }

    @Override
    public List<MerchantCardTable> getListByPlatformOrderId(Set<String> platformOrderIds, MerchantCardTable mct){
        if(isHasNotElement(platformOrderIds)) return null;
        LambdaQueryWrapper<MerchantCardTable> lambdaQueryWrapper = new QueryWrapper<MerchantCardTable>()
                .lambda().in(MerchantCardTable::getPlatformOrderId,platformOrderIds);

        if( !isBlank(mct.getBussType()) )  lambdaQueryWrapper.eq(MerchantCardTable::getBussType,mct.getBussType());
        if( !isNull(mct.getStatus()) )  lambdaQueryWrapper.eq(MerchantCardTable::getStatus,mct.getStatus());
        if( !isBlank(mct.getMerOrderId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerOrderId,mct.getMerOrderId());
        if( !isBlank(mct.getMerchantId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerchantId,mct.getMerchantId());
        if( !isBlank(mct.getTerminalMerId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getTerminalMerId,mct.getTerminalMerId());
        return  merchantCardDBService.list(lambdaQueryWrapper);

    }
}
