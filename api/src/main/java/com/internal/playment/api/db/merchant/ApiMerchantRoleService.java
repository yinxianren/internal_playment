package com.internal.playment.api.db.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.table.merchant.MerchantRoleTable;

import java.util.List;

public interface ApiMerchantRoleService {
    boolean saveOrUpdate(MerchantRoleTable merchantRoleTable);

    boolean delByIds(List<Long> ids);

    IPage page(MerchantRoleTable merchantRoleTable, PageDTO pageDTO);

    List<MerchantRoleTable> getList(MerchantRoleTable merchantRoleTable);
}
