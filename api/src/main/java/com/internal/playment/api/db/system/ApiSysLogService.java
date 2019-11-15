package com.internal.playment.api.db.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.SysLogTable;

public interface ApiSysLogService {

    IPage page(SysLogTable sysLogTable, PageDTO pageDTO);
}
