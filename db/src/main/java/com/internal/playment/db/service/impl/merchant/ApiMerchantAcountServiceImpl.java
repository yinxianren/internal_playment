package com.internal.playment.db.service.impl.merchant;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.internal.playment.api.db.merchant.ApiMerchantAcountService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantAcountTable;
import com.internal.playment.db.service.db.merchant.MerchantAcountDBService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "${application.version}" , timeout = 30000)
public class ApiMerchantAcountServiceImpl implements ApiMerchantAcountService, NewPayAssert {

    @Autowired
    private MerchantAcountDBService merchantAcountDBService;

    @Override
    public MerchantAcountTable getOne(MerchantAcountTable merchantAcountTable) {
        if (isNull(merchantAcountTable)) return null;
        LambdaQueryWrapper<MerchantAcountTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(merchantAcountTable.getMerId())) queryWrapper.eq(MerchantAcountTable::getMerId,merchantAcountTable.getMerId());
        return merchantAcountDBService.getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdate(MerchantAcountTable merchantAcountTable) {
        if (isNull(merchantAcountTable)) return false;
        return merchantAcountDBService.saveOrUpdate(merchantAcountTable);
    }
}
