package com.internal.playment.api.db.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.table.business.PayOrderInfoTable;

import java.util.List;

public interface ApiPayOrderInfoService {

    PayOrderInfoTable getOne(PayOrderInfoTable pit);

    List<PayOrderInfoTable> getList(PayOrderInfoTable pit);

    boolean save(PayOrderInfoTable pit);

    boolean updateByPrimaryKey(PayOrderInfoTable pit);

    IPage page(PayOrderInfoTable pit);
}
