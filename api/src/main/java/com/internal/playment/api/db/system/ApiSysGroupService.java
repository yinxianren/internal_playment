package com.internal.playment.api.db.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.SysGroupTable;

import java.util.List;

public interface ApiSysGroupService {

    boolean saveOrUpdate(SysGroupTable sysGroupTable);

    boolean delByIds(List<Long> ids);

    IPage page(SysGroupTable sysGroupTable, PageDTO pageDTO);

    List<SysGroupTable> getList(SysGroupTable sysGroupTable);

    boolean delByCodes(List<String> codes);
}
