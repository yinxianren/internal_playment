package com.internal.playment.db.service.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.business.ApiRegisterInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.db.service.db.business.RegisterInfoDBService;
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
public class ApiRegisterInfoServiceImpl implements ApiRegisterInfoService, NewPayAssert {

    private final RegisterInfoDBService registerInfoDBService;


    @Override
    public RegisterInfoTable getOne(RegisterInfoTable rit) {
        if(isNull(rit)) return null;
        LambdaQueryWrapper<RegisterInfoTable> lambdaQueryWrapper = new QueryWrapper<RegisterInfoTable>()
                .lambda().eq(RegisterInfoTable::getStatus, StatusEnum._0.getStatus());
        if( !isNull(rit.getId())) lambdaQueryWrapper.eq(RegisterInfoTable::getId,rit.getId());
        if( !isBlank(rit.getMerchantId()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getMerchantId,rit.getMerchantId());
        if( !isBlank(rit.getTerminalMerId()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getTerminalMerId,rit.getTerminalMerId());
        if( !isNull(rit.getIdentityType()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getIdentityType,rit.getIdentityType());
        if( !isBlank(rit.getUserName()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getUserName,rit.getUserName());
        if( !isBlank(rit.getIdentityNum()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getIdentityNum,rit.getIdentityNum());
        return registerInfoDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean replaceSave(RegisterInfoTable rit) {
        if(isNull(rit)) return false;
        return registerInfoDBService.replaceSave(rit);
    }



    @Override
    public boolean save(RegisterInfoTable rit) {
        if(isNull(rit)) return false;
        return registerInfoDBService.save(rit);
    }

    @Override
    public boolean updateByKey(RegisterInfoTable rit) {
        if(isNull(rit)) return false;
        return registerInfoDBService.updateById(rit);
    }

    @Override
    public boolean saveOrUpdate(RegisterInfoTable rit) {
        if(isNull(rit)) return false;
        return registerInfoDBService.saveOrUpdate(rit);
    }


}
