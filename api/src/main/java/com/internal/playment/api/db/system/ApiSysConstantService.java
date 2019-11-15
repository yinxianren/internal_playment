package com.internal.playment.api.db.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.SysConstantTable;

import java.util.List;

public interface ApiSysConstantService {

    boolean saveOrUpdate(SysConstantTable sysConstantTable);

    boolean delByIds(List<String> ids);

    IPage page(SysConstantTable sysConstantTable, PageDTO pageDTO);
}
