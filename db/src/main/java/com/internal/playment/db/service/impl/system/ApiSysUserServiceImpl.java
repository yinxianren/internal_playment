package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.internal.playment.api.db.system.ApiSysUserServie;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.SysUserTable;
import com.internal.playment.db.service.db.system.SysUserDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiSysUserServiceImpl implements ApiSysUserServie, NewPayAssert {

    @Autowired
    private SysUserDBService sysUserDBService;

    @Override
    public boolean savaOrUpdate(SysUserTable sysUserTable) {
        if (isNull(sysUserTable)) return false;
        return sysUserDBService.saveOrUpdate(sysUserTable);
    }

    @Override
    public List<SysUserTable> getList(SysUserTable sysUserTable) {
        if (isNull(sysUserTable)) return sysUserDBService.list();
        LambdaQueryWrapper<SysUserTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(sysUserTable.getUserName())) queryWrapper.eq(SysUserTable::getUserName,sysUserTable.getUserName());
        if (null != sysUserTable.getId()) queryWrapper.eq(SysUserTable::getId,sysUserTable.getId());
        return sysUserDBService.list(queryWrapper);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return false;
        return sysUserDBService.removeByIds(ids);
    }
}
