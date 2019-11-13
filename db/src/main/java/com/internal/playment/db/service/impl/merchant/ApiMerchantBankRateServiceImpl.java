package com.internal.playment.db.service.impl.merchant;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.internal.playment.api.db.merchant.ApiMerchantBankRateSerrvice;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantBankRateTable;
import com.internal.playment.db.service.db.merchant.MerchantBankRateDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}" , timeout = 30000 )
public class ApiMerchantBankRateServiceImpl implements ApiMerchantBankRateSerrvice, NewPayAssert {

    @Autowired
    private MerchantBankRateDBService merchantBankRateDBService;

    @Override
    public List<MerchantBankRateTable> getList(MerchantBankRateTable merchantBankRateTable) {
        if (isNull(merchantBankRateTable)) return null;
        LambdaQueryWrapper<MerchantBankRateTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(merchantBankRateTable.getMerchantId())) queryWrapper.eq(MerchantBankRateTable::getMerchantId,merchantBankRateTable.getMerchantId());
        queryWrapper.orderByAsc(MerchantBankRateTable::getStatus);
        return merchantBankRateDBService.list(queryWrapper);
    }

    @Override
    public Boolean batchSaveOrUpdate(List<MerchantBankRateTable> list) {
        if (isHasNotElement(list)) return false;
        return merchantBankRateDBService.saveOrUpdateBatch(list);
    }
}
