package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.system.ApiMerchantSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.MerchantSysLogTable;
import com.internal.playment.db.service.db.system.MerchantSysLogDBService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "${application.version}" , timeout = 30000)
public class ApiMerchantSysLogServiceImpl implements ApiMerchantSysLogService, NewPayAssert {

    @Autowired
    private MerchantSysLogDBService merchantSysLogDBService;

    @Override
    public boolean saveOrUpdate(MerchantSysLogTable merchantSysLogTable) {
        if (isNull(merchantSysLogTable)) return false;
        return merchantSysLogDBService.saveOrUpdate(merchantSysLogTable);
    }

    @Override
    public IPage page(MerchantSysLogTable merchantSysLogTable, PageDTO pageDTO) {
        if (isNull(merchantSysLogTable)) return new Page();
        LambdaQueryWrapper<MerchantSysLogTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(MerchantSysLogTable::getStartTime);
        if (!isNull(pageDTO.getBeginTime())) queryWrapper.gt(MerchantSysLogTable::getStartTime,pageDTO.getBeginTime());
        if (!isNull(pageDTO.getEndTime())) queryWrapper.lt(MerchantSysLogTable::getStartTime,pageDTO.getEndTime());
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return merchantSysLogDBService.page(iPage,queryWrapper);
    }
}
