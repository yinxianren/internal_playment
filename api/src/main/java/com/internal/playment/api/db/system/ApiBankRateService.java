package com.internal.playment.api.db.system;


import com.internal.playment.common.table.system.BankRateTable;

import java.util.List;

public interface ApiBankRateService {

    BankRateTable getOne(BankRateTable brt);

    List<BankRateTable> getList(BankRateTable brt);

    boolean save(BankRateTable brt);
}
