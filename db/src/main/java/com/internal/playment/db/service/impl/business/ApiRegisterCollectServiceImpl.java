package com.internal.playment.db.service.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.business.ApiRegisterCollectService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.db.service.db.business.RegisterCollectDbService;
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
public class ApiRegisterCollectServiceImpl implements ApiRegisterCollectService, NewPayAssert {

    private final RegisterCollectDbService registerCollectDbService;

    @Override
    public RegisterCollectTable getOne(RegisterCollectTable rct) {
        if(isNull(rct)) return null;
        LambdaQueryWrapper<RegisterCollectTable> lambdaQueryWrapper = new QueryWrapper().lambda();
        if( !isBlank(rct.getMerchantId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerchantId,rct.getMerchantId());
        if( !isBlank(rct.getTerminalMerId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getTerminalMerId,rct.getTerminalMerId());
        if( !isBlank(rct.getMerOrderId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerOrderId,rct.getMerOrderId());
        if( !isBlank(rct.getPlatformOrderId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getPlatformOrderId,rct.getPlatformOrderId());
        if( !isNull(rct.getStatus()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getStatus,rct.getStatus());
        if( !isBlank(rct.getBussType()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getBussType,rct.getBussType());
        if( !isBlank(rct.getChannelId()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getChannelId,rct.getChannelId());
        if( !isBlank(rct.getBankCardNum()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getBankCardNum,rct.getBankCardNum());
        if( !isBlank(rct.getBankCardPhone()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getBankCardPhone,rct.getBankCardPhone());
        return registerCollectDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<RegisterCollectTable> getList(RegisterCollectTable rct) {
        if(isNull(rct)) return null;
        LambdaQueryWrapper<RegisterCollectTable> lambdaQueryWrapper = new QueryWrapper().lambda();
        if( !isBlank(rct.getMerchantId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerchantId,rct.getMerchantId());
        if( !isBlank(rct.getTerminalMerId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getTerminalMerId,rct.getTerminalMerId());
        if( !isBlank(rct.getMerOrderId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerOrderId,rct.getMerOrderId());
        if( !isBlank(rct.getBussType()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getBussType,rct.getBussType());
        if( !isBlank(rct.getPlatformOrderId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getPlatformOrderId,rct.getPlatformOrderId());
        if( !isNull(rct.getStatus()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getStatus,rct.getStatus());
        if( !isBlank(rct.getChannelId()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getChannelId,rct.getChannelId());
        if( !isBlank(rct.getBankCardNum()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getBankCardNum,rct.getBankCardNum());
        if( !isBlank(rct.getBankCardPhone()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getBankCardPhone,rct.getBankCardPhone());
        return registerCollectDbService.list(lambdaQueryWrapper);
    }
    @Override
    public boolean save(RegisterCollectTable rct) {
        if(isNull(rct))  return false;
        return registerCollectDbService.save(rct);
    }

    @Override
    public boolean updateByPrimaryKey(RegisterCollectTable rct) {
        if(isNull(rct))  return false;
        return registerCollectDbService.updateById(rct);
    }

}
