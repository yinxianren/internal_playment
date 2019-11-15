package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.api.db.system.ApiSysGroupService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.SysGroupTable;

import java.util.List;

@Service(version = "${application.version}" , timeout = 30000)
public class ApiSysGroupServiceImpl implements ApiSysGroupService {
    @Override
    public boolean saveOrUpdate(SysGroupTable sysGroupTable) {
        return false;
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        return false;
    }

    @Override
    public IPage page(SysGroupTable sysGroupTable, PageDTO pageDTO) {
        return null;
    }
}
