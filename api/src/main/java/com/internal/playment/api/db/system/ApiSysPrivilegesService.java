package com.internal.playment.api.db.system;

import com.internal.playment.common.table.system.SysPrivilegesTable;

import java.util.List;

public interface ApiSysPrivilegesService {
    List<SysPrivilegesTable> getList(SysPrivilegesTable sysPrivilegesTable);
}
