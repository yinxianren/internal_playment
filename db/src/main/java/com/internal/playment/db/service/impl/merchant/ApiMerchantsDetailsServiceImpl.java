package com.internal.playment.db.service.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.merchant.ApiMerchantsDetailsService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantsDetailsTable;
import com.internal.playment.db.service.db.merchant.MerchantsDetailsDBService;
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
public class ApiMerchantsDetailsServiceImpl implements ApiMerchantsDetailsService, NewPayAssert {

    private final MerchantsDetailsDBService merchantsDetailsDBService;

    @Override
    public boolean save(MerchantsDetailsTable mdt) {
      if(isNull(mdt)) return false;

        return merchantsDetailsDBService.save(mdt);
    }

    @Override
    public IPage page(MerchantsDetailsTable merchantsDetailsTable) {
        if (isNull(merchantsDetailsTable)) return new Page();
        LambdaQueryWrapper<MerchantsDetailsTable> queryWrapper = new LambdaQueryWrapper();
        IPage iPage = new Page(merchantsDetailsTable.getPageNum(),merchantsDetailsTable.getPageSize());
        if (!isBlank(merchantsDetailsTable.getMerchantId())) queryWrapper.eq(MerchantsDetailsTable::getMerchantId,merchantsDetailsTable.getMerchantId());
        if (!isBlank(merchantsDetailsTable.getMerOrderId())) queryWrapper.eq(MerchantsDetailsTable::getMerOrderId,merchantsDetailsTable.getMerOrderId());
        if (!isBlank(merchantsDetailsTable.getPlatformOrderId())) queryWrapper.eq(MerchantsDetailsTable::getPlatformOrderId,merchantsDetailsTable.getPlatformOrderId());
        if (!isBlank(merchantsDetailsTable.getProductId())) queryWrapper.eq(MerchantsDetailsTable::getProductId,merchantsDetailsTable.getProductId());
        if (!isNull(merchantsDetailsTable.getBeginTime())) queryWrapper.gt(MerchantsDetailsTable::getUpdateTime,merchantsDetailsTable.getBeginTime());
        if (!isNull(merchantsDetailsTable.getEndTime())) queryWrapper.lt(MerchantsDetailsTable::getUpdateTime,merchantsDetailsTable.getEndTime());
        queryWrapper.orderByDesc(MerchantsDetailsTable::getTimestamp);
        return merchantsDetailsDBService.page(iPage,queryWrapper);
    }
}
