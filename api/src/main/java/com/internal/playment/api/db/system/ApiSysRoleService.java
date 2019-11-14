package com.internal.playment.api.db.system;

import com.internal.playment.common.table.system.SysRoleTable;

import java.util.List;

public interface ApiSysRoleService {

    List<SysRoleTable> getList(SysRoleTable sysRoleTable);

    boolean delByIds(List<Long> ids);

    boolean saveOrUpdate(SysRoleTable sysRoleTable);
}
