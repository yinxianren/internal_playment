package com.internal.playment.api.db.merchant;


import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;

import java.util.List;

public interface ApiMerchantQuotaRiskService {

    MerchantQuotaRiskTable getOne(MerchantQuotaRiskTable mqr);

    List<MerchantQuotaRiskTable> getList(MerchantQuotaRiskTable mqr);

    boolean save(MerchantQuotaRiskTable mqr);

    boolean updateByPrimaryKey(MerchantQuotaRiskTable mqr);

}
