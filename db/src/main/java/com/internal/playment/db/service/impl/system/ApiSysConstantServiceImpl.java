package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.api.db.system.ApiSysConstantService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.SysConstantTable;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiSysConstantServiceImpl implements ApiSysConstantService {
    @Override
    public boolean saveOrUpdate(SysConstantTable sysConstantTable) {
        return false;
    }

    @Override
    public boolean delByIds(List<String> ids) {
        return false;
    }

    @Override
    public IPage page(SysConstantTable sysConstantTable, PageDTO pageDTO) {
        return null;
    }
}
