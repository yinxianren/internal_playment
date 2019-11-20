package com.internal.playment.api.db.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.UserLoginIpTable;

import java.util.List;

public interface ApiUserLoginIpService {

    boolean saveOrUpdate(UserLoginIpTable userLoginIpTable);
    boolean delByIds(List<Long> ids);
    IPage page(UserLoginIpTable userLoginIpTable, PageDTO pageDTO);
}
