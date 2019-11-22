package com.internal.playment.db.service.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.business.ApiTransOrderInfoService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.db.service.db.business.TransOrderInfoDBService;
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
public class ApiTransOrderInfoServiceImpl implements ApiTransOrderInfoService, NewPayAssert {

    private final TransOrderInfoDBService transOrderInfoDBService;

    @Override
    public TransOrderInfoTable getOne(TransOrderInfoTable tit) {
        if(isNull(tit)) return null;
        LambdaQueryWrapper<TransOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<TransOrderInfoTable>()
                .lambda();
        if( !isBlank(tit.getMerOrderId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerOrderId,tit.getMerOrderId());
        if( !isBlank(tit.getMerchantId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerchantId,tit.getMerchantId());
        if( !isBlank(tit.getTerminalMerId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getTerminalMerId,tit.getTerminalMerId());
        if( !isBlank(tit.getBusiType()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getBusiType,tit.getBusiType());
        if( !isBlank(tit.getPlatformOrderId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getPlatformOrderId,tit.getPlatformOrderId());
        if( !isNull(tit.getStatus()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getStatus,tit.getStatus());
        return transOrderInfoDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<TransOrderInfoTable> getList(TransOrderInfoTable tit) {
        if(isNull(tit)) return null;
        LambdaQueryWrapper<TransOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<TransOrderInfoTable>()
                .lambda();
        if(  isHasElement(tit.getStatusCollect())) lambdaQueryWrapper.in(TransOrderInfoTable::getStatus,tit.getStatusCollect());
        if( !isBlank(tit.getMerOrderId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerOrderId,tit.getMerOrderId());
        if( !isBlank(tit.getMerchantId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerchantId,tit.getMerchantId());
        if( !isBlank(tit.getTerminalMerId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getTerminalMerId,tit.getTerminalMerId());
        if( !isBlank(tit.getBusiType()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getBusiType,tit.getBusiType());
        if( !isNull(tit.getStatus()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getStatus,tit.getStatus());
        if( !isBlank(tit.getOrgMerOrderId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getOrgMerOrderId,tit.getOrgMerOrderId());
        if( !isBlank(tit.getBeginTime())) lambdaQueryWrapper.ge(TransOrderInfoTable::getUpdateTime,tit.getBeginTime());
        if( !isBlank(tit.getEndTime())) lambdaQueryWrapper.le(TransOrderInfoTable::getUpdateTime,tit.getEndTime());
        if( isHasElement(tit.getStatusCollect())) lambdaQueryWrapper.in(TransOrderInfoTable::getStatus,tit.getStatusCollect());
        if( !isNull(tit.getNotifyStatus()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getNotifyStatus,tit.getNotifyStatus());
        return transOrderInfoDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(TransOrderInfoTable tit) {
        if(isNull(tit)) return false;
        return transOrderInfoDBService.save(tit);
    }

    @Override
    public boolean updateById(TransOrderInfoTable tit) {
        if(isNull(tit)) return false;
        return transOrderInfoDBService.updateById(tit);
    }

    @Override
    public boolean updateByWhereCondition(TransOrderInfoTable tit) {
        if(isNull(tit)) return false;
        LambdaUpdateWrapper<TransOrderInfoTable> updateWrapper = new UpdateWrapper<TransOrderInfoTable>().lambda();
        //set
        if( !isNull(tit.getNotifyStatus()) )  updateWrapper.set( TransOrderInfoTable::getNotifyStatus,tit.getNotifyStatus() );
        if( !isNull(tit.getStatus()) )  updateWrapper.set( TransOrderInfoTable::getStatus,tit.getStatus() );
        if( !isNull(tit.getUpdateTime()) )  updateWrapper.set( TransOrderInfoTable::getUpdateTime,tit.getUpdateTime() );
        if( !isBlank(tit.getCrossRespResult()))   updateWrapper.set( TransOrderInfoTable::getCrossRespResult,tit.getCrossRespResult() );
        if( !isBlank(tit.getChannelRespResult()))   updateWrapper.set( TransOrderInfoTable::getChannelRespResult,tit.getChannelRespResult() );
        if( !isBlank(tit.getChannelOrderId()))   updateWrapper.set( TransOrderInfoTable::getChannelOrderId,tit.getChannelOrderId() );
        //where
        if( !isNull(tit.getId()) )  updateWrapper.eq( TransOrderInfoTable::getId,tit.getId() );
        if( !isBlank(tit.getPlatformOrderId()))   updateWrapper.eq( TransOrderInfoTable::getPlatformOrderId,tit.getPlatformOrderId() );
        return transOrderInfoDBService.update(updateWrapper);
    }

    @Override
    public IPage page(TransOrderInfoTable transOrderInfoTable) {
        if(isNull(transOrderInfoTable)) return new Page();
        IPage page = new Page(transOrderInfoTable.getPageNum(),transOrderInfoTable.getPageSize());
        LambdaQueryWrapper<TransOrderInfoTable> queryWrapper = new LambdaQueryWrapper();
        if ( !isBlank(transOrderInfoTable.getMerchantId())) queryWrapper.eq(TransOrderInfoTable::getMerchantId,transOrderInfoTable.getMerchantId());
        if ( !isBlank(transOrderInfoTable.getPlatformOrderId())) queryWrapper.eq(TransOrderInfoTable::getPlatformOrderId,transOrderInfoTable.getPlatformOrderId());
//            if (StringUtils.isNotEmpty(pit.getOrganizationId())) queryWrapper.setChannelId(searchInfo.getExpressName());
        if ( !isNull(transOrderInfoTable.getStatus())) queryWrapper.eq(TransOrderInfoTable::getStatus,transOrderInfoTable.getStatus());
        if ( !isNull (transOrderInfoTable.getSettleStatus())) queryWrapper.eq(TransOrderInfoTable::getSettleStatus,transOrderInfoTable.getSettleStatus());
        if ( !isBlank(transOrderInfoTable.getProductId())) queryWrapper.eq(TransOrderInfoTable::getProductId,transOrderInfoTable.getProductId());
        if ( !isNull(transOrderInfoTable.getBeginTime())) queryWrapper.ge(TransOrderInfoTable::getUpdateTime,transOrderInfoTable.getBeginTime());
        if ( !isNull(transOrderInfoTable.getEndTime())) queryWrapper.le(TransOrderInfoTable::getUpdateTime,transOrderInfoTable.getEndTime());
        return transOrderInfoDBService.page(page,queryWrapper);
    }
}
