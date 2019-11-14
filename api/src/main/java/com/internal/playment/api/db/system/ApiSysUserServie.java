package com.internal.playment.api.db.system;

import com.internal.playment.common.table.system.SysUserTable;

import java.util.List;

public interface ApiSysUserServie {
    boolean savaOrUpdate(SysUserTable sysUserTable);
    List<SysUserTable> getList(SysUserTable sysUserTable);
    boolean delByIds(List<Long> ids);
}
