package com.internal.playment.api.db.business;


import com.internal.playment.common.table.business.MerchantCardTable;

import java.util.List;
import java.util.Set;

public interface ApiMerchantCardService {

    MerchantCardTable getOne(MerchantCardTable mct);

    List<MerchantCardTable>  getList(MerchantCardTable mct);

    List<MerchantCardTable> getListByPlatformOrderId(Set<String> platformOrderIds, MerchantCardTable mct);

    boolean updateById(MerchantCardTable mct);

    boolean updateByWhereCondition(MerchantCardTable mct);

    boolean bachUpdateById(List<MerchantCardTable> list);

    boolean save(MerchantCardTable mct);
}
