package com.internal.playment.api.db.business;


import com.internal.playment.common.table.business.RegisterCollectTable;

import java.util.List;

public interface ApiRegisterCollectService {

    RegisterCollectTable getOne(RegisterCollectTable rct);

    List<RegisterCollectTable> getList(RegisterCollectTable rct);

    boolean save(RegisterCollectTable rct);

    boolean updateByPrimaryKey(RegisterCollectTable rct);
}
