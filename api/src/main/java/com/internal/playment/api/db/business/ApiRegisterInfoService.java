package com.internal.playment.api.db.business;


import com.internal.playment.common.table.business.RegisterInfoTable;

public interface ApiRegisterInfoService {

    RegisterInfoTable getOne(RegisterInfoTable rit);

    boolean replaceSave(RegisterInfoTable rit);

    boolean save(RegisterInfoTable rit);

    boolean updateByKey(RegisterInfoTable rit);

    boolean saveOrUpdate(RegisterInfoTable rit);
}
