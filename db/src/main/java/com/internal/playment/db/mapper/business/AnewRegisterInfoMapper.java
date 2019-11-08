package com.internal.playment.db.mapper.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.internal.playment.common.table.business.RegisterInfoTable;

public interface AnewRegisterInfoMapper extends BaseMapper<RegisterInfoTable> {

    boolean replaceSave(RegisterInfoTable registerInfoTable);

}
