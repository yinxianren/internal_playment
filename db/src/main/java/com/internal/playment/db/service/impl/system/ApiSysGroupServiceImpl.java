package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.system.ApiSysGroupService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.enums.StatusEnum;
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
        if (isHasNotElement(ids)) return false;
        return sysGroupDBService.removeByIds(ids);
    }

    @Override
    public IPage page(SysGroupTable sysGroupTable, PageDTO pageDTO) {
        if (isNull(sysGroupTable)) return new Page();
        LambdaQueryWrapper<SysGroupTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isBlank(sysGroupTable.getName())) queryWrapper.like(SysGroupTable::getName,sysGroupTable.getName());
        if (!isBlank(sysGroupTable.getCode())) queryWrapper.eq(SysGroupTable::getCode,sysGroupTable.getCode());
        if (!isNull(sysGroupTable.getModel())) queryWrapper.eq(SysGroupTable::getModel,sysGroupTable.getModel());
        if (!isNull(sysGroupTable.getSystem())) queryWrapper.eq(SysGroupTable::getSystem,sysGroupTable.getSystem());
        queryWrapper.eq(SysGroupTable::getStatus, StatusEnum._0.getStatus());
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return sysGroupDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<SysGroupTable> getList(SysGroupTable sysGroupTable) {
        if (isNull(sysGroupTable)) return sysGroupDBService.list();
        LambdaQueryWrapper<SysGroupTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isBlank(sysGroupTable.getCode())) queryWrapper.eq(SysGroupTable::getCode,sysGroupTable.getCode());
        if (!isBlank(sysGroupTable.getName())) queryWrapper.eq(SysGroupTable::getName,sysGroupTable.getName());
        return sysGroupDBService.list(queryWrapper);
    }

    @Override
    public boolean delByCodes(List<String> codes) {
        if (isHasNotElement(codes)) return false;
        LambdaQueryWrapper<SysGroupTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysGroupTable::getCode,codes);
        return sysGroupDBService.remove(queryWrapper);
    }
}
