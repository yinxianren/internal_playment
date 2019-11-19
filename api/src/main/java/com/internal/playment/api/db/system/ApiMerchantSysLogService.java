package com.internal.playment.api.db.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.system.MerchantSysLogTable;

public interface ApiMerchantSysLogService {

    boolean saveOrUpdate(MerchantSysLogTable merchantSysLogTable);

    IPage page(MerchantSysLogTable merchantSysLogTable, PageDTO pageDTO);
}
