package com.internal.playment.api.db.system;


import com.internal.playment.common.table.system.BankCodeTable;

import java.util.List;

public interface ApiBankCodeService {

    BankCodeTable getOne(BankCodeTable bct);

    List<BankCodeTable> getList(BankCodeTable bct);

    boolean save(BankCodeTable bct);
}
