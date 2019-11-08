package com.internal.playment.api.db.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.table.merchant.MerchantsDetailsTable;

public interface ApiMerchantsDetailsService {

    boolean save(MerchantsDetailsTable mdt);

    IPage page(MerchantsDetailsTable merchantsDetailsTable);

}
