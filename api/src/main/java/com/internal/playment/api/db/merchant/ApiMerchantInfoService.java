package com.internal.playment.api.db.merchant;


import com.internal.playment.common.table.merchant.MerchantInfoTable;

import java.util.List;

public interface ApiMerchantInfoService {

    MerchantInfoTable getOne(MerchantInfoTable merchantInfoTable);

    List<MerchantInfoTable> getMerchants(MerchantInfoTable merchantInfoTable);

    Boolean saveOrUpdate(MerchantInfoTable merchantInfoTable);

    Boolean delByIds(List<String> ids);

}
