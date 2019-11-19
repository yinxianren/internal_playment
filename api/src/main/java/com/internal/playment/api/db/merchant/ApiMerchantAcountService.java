package com.internal.playment.api.db.merchant;

import com.internal.playment.common.table.merchant.MerchantAcountTable;

public interface ApiMerchantAcountService {

    MerchantAcountTable getOne(MerchantAcountTable merchantAcountTable);

    boolean saveOrUpdate(MerchantAcountTable merchantAcountTable);

}
