package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.internal.playment.api.db.system.ApiAsyncNotifyService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.db.service.db.system.AsyncNotifyDbService;
import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiAsyncNotifyServiceImpl implements ApiAsyncNotifyService, NewPayAssert {

    private final AsyncNotifyDbService asyncNotifyDbService;
    @Override
    public List<AsyncNotifyTable> getList(AsyncNotifyTable ant) {
        if(isNull(ant)) return null;
        LambdaQueryWrapper<AsyncNotifyTable> lambdaQueryWrapper = new QueryWrapper<AsyncNotifyTable>()
                .lambda() ;
        if( !isBlank(ant.getBeginTime()))  lambdaQueryWrapper.ge(AsyncNotifyTable::getUpdateTime,ant.getBeginTime());
        if( !isBlank(ant.getEndTime()))  lambdaQueryWrapper.le(AsyncNotifyTable::getUpdateTime,ant.getEndTime());
        if( !isNull(ant.getStatus())) lambdaQueryWrapper.eq(AsyncNotifyTable::getStatus,ant.getStatus());
        return null;
    }

    @Override
    public boolean save(AsyncNotifyTable ant) {
        if(isNull(ant)) return false;
        return asyncNotifyDbService.save(ant);
    }

    @Override
    public boolean updateByKey(AsyncNotifyTable ant) {
        if(isNull(ant)) return false;
        return asyncNotifyDbService.updateById(ant);
    }

    @Override
    public boolean updateByWhereCondition(AsyncNotifyTable ant) {
        if(isNull(ant)) return false;
        LambdaUpdateWrapper<AsyncNotifyTable> updateWrapper = new UpdateWrapper<AsyncNotifyTable>().lambda();
        updateWrapper.eq(AsyncNotifyTable::getPlatformOrderId,ant.getPlatformOrderId());
        return asyncNotifyDbService.update(ant,updateWrapper);
    }
}
