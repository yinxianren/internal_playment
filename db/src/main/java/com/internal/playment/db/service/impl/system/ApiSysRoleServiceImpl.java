package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.internal.playment.api.db.system.ApiSysRoleService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.SysRoleTable;
import com.internal.playment.db.service.db.system.SysRoleDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiSysRoleServiceImpl implements ApiSysRoleService, NewPayAssert {

    @Autowired
    private SysRoleDBService sysRoleDBService;

    @Override
    public List<SysRoleTable> getList(SysRoleTable sysRoleTable) {
        if (isNull(sysRoleTable)) return sysRoleDBService.list();
        LambdaQueryWrapper<SysRoleTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(sysRoleTable.getRoleName())) queryWrapper.eq(SysRoleTable::getRoleName,sysRoleTable.getRoleName());
        return  sysRoleDBService.list(queryWrapper);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return false;
        return sysRoleDBService.removeByIds(ids);
    }

    @Override
    public boolean saveOrUpdate(SysRoleTable sysRoleTable) {
        if (isNull(sysRoleTable)) return false;
        return sysRoleDBService.saveOrUpdate(sysRoleTable);
    }
}
