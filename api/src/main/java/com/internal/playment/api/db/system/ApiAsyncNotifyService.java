package com.internal.playment.api.db.system;

import com.internal.playment.common.table.system.AsyncNotifyTable;

import java.util.List;

public interface ApiAsyncNotifyService {

    List<AsyncNotifyTable>  getList(AsyncNotifyTable ant);

    boolean save(AsyncNotifyTable ant);

    boolean updateByKey(AsyncNotifyTable ant);

    boolean updateByWhereCondition(AsyncNotifyTable ant);
}
