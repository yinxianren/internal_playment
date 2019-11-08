package com.internal.playment.api.db.system;


import com.internal.playment.common.table.system.MerchantSettingTable;

import java.util.List;

public interface ApiMerchantSettingService {

    List<MerchantSettingTable> getList(MerchantSettingTable mst);

    Boolean batchSaveOrUpdate(List<MerchantSettingTable> merchantSettingTables);

    Boolean remove(MerchantSettingTable mst);

}
