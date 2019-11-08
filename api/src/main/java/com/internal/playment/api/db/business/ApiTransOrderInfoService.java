package com.internal.playment.api.db.business;


import com.internal.playment.common.table.business.TransOrderInfoTable;

import java.util.List;

public interface ApiTransOrderInfoService {

    TransOrderInfoTable getOne(TransOrderInfoTable tit);

    List<TransOrderInfoTable> getList(TransOrderInfoTable tit);

    boolean save(TransOrderInfoTable tit);

    boolean updateById(TransOrderInfoTable tit);

}
