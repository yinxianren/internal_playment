package com.internal.playment.api.db.merchant;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.internal.playment.common.table.merchant.MerchantsDetailsTable;

import java.util.List;

public interface ApiMerchantWalletService {

    MerchantWalletTable getOne(MerchantWalletTable mwt);

    boolean updateOrSave(MerchantWalletTable mwt);

    List<MerchantWalletTable> getList(MerchantWalletTable merchantWalletTable);

    IPage page(MerchantsDetailsTable merchantsDetailsTable);
}
