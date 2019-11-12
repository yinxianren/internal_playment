package com.internal.playment.api.db.channel;


import com.internal.playment.common.table.system.ProductSettingTable;

import java.util.List;

public interface ApiProductTypeSettingService {

     Boolean SaveOrUpdate(ProductSettingTable productSettingTable);

     Boolean removeById(List<Long> id);

     List<ProductSettingTable> list(ProductSettingTable productSettingTable);

     ProductSettingTable  getOne(ProductSettingTable productSettingTable);

     Boolean batchUpdate(List<ProductSettingTable> productSettingTableList);

     Boolean delByOrganizationId(String organizationId);
}
