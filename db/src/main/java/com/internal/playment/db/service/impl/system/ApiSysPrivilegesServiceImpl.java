package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.internal.playment.api.db.system.ApiSysPrivilegesService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.internal.playment.db.service.db.system.SysPrivilegesDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiSysPrivilegesServiceImpl implements ApiSysPrivilegesService, NewPayAssert {

    @Autowired
    private SysPrivilegesDBService sysPrivilegesDBService;

    @Override
    public List<SysPrivilegesTable> getList(SysPrivilegesTable sysPrivilegesTable) {
        if (isNull(sysPrivilegesTable)) return sysPrivilegesDBService.list();
        LambdaQueryWrapper<SysPrivilegesTable> queryWrapper = new LambdaQueryWrapper();
        if (isHasNotElement(sysPrivilegesTable.getIds())) queryWrapper.in(SysPrivilegesTable::getId,sysPrivilegesTable.getIds());
        return sysPrivilegesDBService.list(queryWrapper);
    }
}
