package com.internal.playment.api.db.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.merchant.MerchantUserTable;

import java.util.List;

public interface ApiMerchantUserService {

    boolean saveOrUpdate(MerchantUserTable merchantUserTable);

    boolean delByIds(List<Long> ids);

    IPage page(MerchantUserTable merchantUserTable, PageDTO pageDTO);

    List<MerchantUserTable> getList(MerchantUserTable merchantUserTable);

}
