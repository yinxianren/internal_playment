package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.system.ApiSysGroupService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.SysGroupTable;
import com.internal.playment.db.service.db.system.SysGroupDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}" , timeout = 30000)
public class ApiSysGroupServiceImpl implements ApiSysGroupService, NewPayAssert {

    @Autowired
    private SysGroupDBService sysGroupDBService;

    @Override
    public boolean saveOrUpdate(SysGroupTable sysGroupTable) {
        if (isNull(sysGroupTable)) return false;
        return sysGroupDBService.saveOrUpdate(sysGroupTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasElement(ids)) return false;
        return sysGroupDBService.removeByIds(ids);
    }

    @Override
    public IPage page(SysGroupTable sysGroupTable, PageDTO pageDTO) {
        if (isNull(sysGroupTable)) return new Page();
        LambdaQueryWrapper<SysGroupTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return sysGroupDBService.page(iPage,queryWrapper);
    }
}
