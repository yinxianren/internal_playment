package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.system.ApiSysLogService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.SysLogTable;
import com.internal.playment.db.service.db.system.SysLogDBService;
import org.springframework.beans.factory.annotation.Autowired;


@Service(version = "${application.version}",timeout = 30000)
public class ApiSysLogServiceImpl implements ApiSysLogService, NewPayAssert {

    @Autowired
    private SysLogDBService sysLogDBService;

    @Override
    public boolean saveOrUpdate(SysLogTable sysLogTable) {
        if (isNull(sysLogTable)) return false;
        return sysLogDBService.saveOrUpdate(sysLogTable);
    }

    @Override
    public IPage page(SysLogTable sysLogTable, PageDTO pageDTO) {
        if (isNull(sysLogTable)) return new Page();
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return sysLogDBService.page(iPage,queryWrapper);
    }
}
