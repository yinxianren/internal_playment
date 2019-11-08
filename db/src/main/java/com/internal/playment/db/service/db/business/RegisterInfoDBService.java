package com.internal.playment.db.service.db.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.internal.playment.common.table.business.RegisterInfoTable;

public interface RegisterInfoDBService extends IService<RegisterInfoTable> {

    boolean replaceSave(RegisterInfoTable registerInfoTable);
}
