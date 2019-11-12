package com.internal.playment.api.db.merchant;


import com.internal.playment.common.table.merchant.MerchantBankRateTable;

import java.util.List;

public interface ApiMerchantBankRateSerrvice {

    List<MerchantBankRateTable> getList(MerchantBankRateTable merchantBankRateTable);

    Boolean batchSaveOrUpdate(List<MerchantBankRateTable> list);
}
