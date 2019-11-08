package com.internal.playment.api.db.merchant;


import com.internal.playment.common.table.merchant.MerchantRateTable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 下午7:41
 * Description:
 */
public interface ApiMerchantRateService {

    MerchantRateTable getOne(MerchantRateTable mr);

    boolean save(MerchantRateTable mr);

    Boolean batchSaveOrUpdate(List<MerchantRateTable> rateTables);

    List<MerchantRateTable> getList(MerchantRateTable mer);

}
