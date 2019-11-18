package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.system.ApiSysConstantService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.SysConstantTable;
import com.internal.playment.db.service.db.system.SysConstantDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiSysConstantServiceImpl implements ApiSysConstantService, NewPayAssert {

    @Autowired
    private SysConstantDBService sysConstantDBService;

    @Override
    public boolean saveOrUpdate(SysConstantTable sysConstantTable) {
        if (isNull(sysConstantTable)) return false;
        return sysConstantDBService.saveOrUpdate(sysConstantTable);
    }

    @Override
    public boolean delByIds(List<String> ids) {
        if (isHasElement(ids)) return false;
        return sysConstantDBService.removeByIds(ids);
    }

    @Override
    public IPage page(SysConstantTable sysConstantTable, PageDTO pageDTO) {
        if (isNull(sysConstantTable)) return new Page();
        LambdaQueryWrapper<SysConstantTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return sysConstantDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<SysConstantTable> getList(SysConstantTable sysConstantTable) {
        if (isNull(sysConstantTable)) return sysConstantDBService.list();
        LambdaQueryWrapper<SysConstantTable> queryWrapper = new LambdaQueryWrapper<SysConstantTable>();
        if (!isBlank(sysConstantTable.getGroupCode())) queryWrapper.eq(SysConstantTable::getGroupCode,sysConstantTable.getGroupCode());
        if (!isBlank(sysConstantTable.getId())) queryWrapper.eq(SysConstantTable::getId,sysConstantTable.getId());
        return sysConstantDBService.list(queryWrapper);
    }
}
