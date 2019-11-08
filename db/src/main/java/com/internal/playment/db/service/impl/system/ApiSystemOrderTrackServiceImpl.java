package com.internal.playment.db.service.impl.system;

import com.internal.playment.api.db.system.ApiSystemOrderTrackService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.db.service.db.system.SystemOrderTrackDbService;
import lombok.AllArgsConstructor;
import com.alibaba.dubbo.config.annotation.Service;
/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiSystemOrderTrackServiceImpl implements ApiSystemOrderTrackService, NewPayAssert {

    private final SystemOrderTrackDbService systemOrderTrackDbService;

    @Override
    public boolean save(SystemOrderTrackTable sot) {
        if(isNull(sot)) return false;
        return systemOrderTrackDbService.save(sot);
    }


}
