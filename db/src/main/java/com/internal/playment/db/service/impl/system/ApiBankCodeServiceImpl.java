package com.internal.playment.db.service.impl.system;

import com.internal.playment.api.db.system.ApiBankCodeService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.BankCodeTable;
import com.internal.playment.db.service.db.system.BankCodeDbService;
import lombok.AllArgsConstructor;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiBankCodeServiceImpl implements ApiBankCodeService, NewPayAssert {

    private final BankCodeDbService bankCodeDbService;

    @Override
    public BankCodeTable getOne(BankCodeTable bct) {
        return null;
    }

    @Override
    public List<BankCodeTable> getList(BankCodeTable bct) {
        return null;
    }

    @Override
    public boolean save(BankCodeTable bct) {
        return false;
    }
}
