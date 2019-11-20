package com.internal.playment.api.db.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.merchant.MerchantPrivilegesTable;

import java.util.List;

public interface ApiMerchantPrivilegesService {

    boolean saveOrUpdate(MerchantPrivilegesTable merchantPrivilegesTable);

    boolean delByIds(List<Long> ids);

    IPage page(MerchantPrivilegesTable merchantPrivilegesTable, PageDTO pageDTO);

    List<MerchantPrivilegesTable> getList(MerchantPrivilegesTable merchantPrivilegesTable);

}
