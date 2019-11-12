package com.internal.playment.db.service.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.business.ApiPayOrderInfoService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.db.service.db.business.PayOrderInfoDBService;
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
public class ApiPayOrderInfoServiceImpl implements ApiPayOrderInfoService, NewPayAssert {
    private final PayOrderInfoDBService payOrderInfoDBService;

    @Override
    public PayOrderInfoTable getOne(PayOrderInfoTable pit) {
        if(isNull(pit)) return null;
        LambdaQueryWrapper<PayOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<PayOrderInfoTable>() .lambda();
        if( !isNull(pit.getStatus())) lambdaQueryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if( !isBlank(pit.getPlatformOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
        if( !isBlank(pit.getMerOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerOrderId,pit.getMerOrderId());
        if( !isBlank(pit.getMerchantId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if( !isBlank(pit.getTerminalMerId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getTerminalMerId,pit.getTerminalMerId());
        if( !isBlank(pit.getBussType())) lambdaQueryWrapper.eq(PayOrderInfoTable::getBussType,pit.getBussType());
        if( isHasElement(pit.getStatusCollect())) lambdaQueryWrapper.in(PayOrderInfoTable::getStatus,pit.getStatusCollect());
        return payOrderInfoDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<PayOrderInfoTable> getList(PayOrderInfoTable pit) {
        if( isNull(pit)) return null;
        LambdaQueryWrapper<PayOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<PayOrderInfoTable>() .lambda();
        if(isHasElement(pit.getMerOrderIdCollect()) ) lambdaQueryWrapper.in(PayOrderInfoTable::getMerOrderId,pit.getMerOrderIdCollect());
        if(isHasElement(pit.getStatusCollect())) lambdaQueryWrapper.in(PayOrderInfoTable::getStatus,pit.getStatusCollect());
        if( !isNull(pit.getStatus())) lambdaQueryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if( !isBlank(pit.getPlatformOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
        if( !isBlank(pit.getMerOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerOrderId,pit.getMerOrderId());
        if( !isBlank(pit.getMerchantId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if( !isBlank(pit.getTerminalMerId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getTerminalMerId,pit.getTerminalMerId());
        if( !isBlank(pit.getBussType())) lambdaQueryWrapper.eq(PayOrderInfoTable::getBussType,pit.getBussType());
        if( !isBlank(pit.getBeginTime())) lambdaQueryWrapper.ge(PayOrderInfoTable::getUpdateTime,pit.getBeginTime());
        if( !isBlank(pit.getEndTime())) lambdaQueryWrapper.le(PayOrderInfoTable::getUpdateTime,pit.getEndTime());
        return payOrderInfoDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(PayOrderInfoTable pit) {
        if(isNull(pit)) return false;
        return payOrderInfoDBService.save(pit);
    }

    @Override
    public boolean updateByPrimaryKey(PayOrderInfoTable pit) {
        if(isNull(pit)) return false;
        return payOrderInfoDBService.updateById(pit);
    }

    @Override
    public IPage page(PayOrderInfoTable pit) {
        if (isNull(pit)) return new Page();
        LambdaQueryWrapper<PayOrderInfoTable> queryWrapper = new QueryWrapper<PayOrderInfoTable>().lambda();
        if ( isBlank(pit.getMerchantId())) queryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if ( isBlank(pit.getPlatformOrderId())) queryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
//            if (StringUtils.isNotEmpty(pit.getOrganizationId())) queryWrapper.setChannelId(searchInfo.getExpressName());
        if ( !isNull(pit.getStatus())) queryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if ( !isNull (pit.getSettleStatus())) queryWrapper.eq(PayOrderInfoTable::getSettleStatus,pit.getSettleStatus());
        if ( isBlank(pit.getProductId())) queryWrapper.eq(PayOrderInfoTable::getProductId,pit.getProductId());
        if ( !isNull(pit.getBeginTime())) queryWrapper.ge(PayOrderInfoTable::getCreateTime,pit.getBeginTime());
        if ( !isNull(pit.getEndTime())) queryWrapper.le(PayOrderInfoTable::getUpdateTime,pit.getEndTime());
        IPage<PayOrderInfoTable> iPage = new Page(pit.getPageNum(),pit.getPageSize());
        return payOrderInfoDBService.page(iPage,queryWrapper);
    }
}
