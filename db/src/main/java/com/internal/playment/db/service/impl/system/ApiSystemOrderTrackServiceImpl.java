package com.internal.playment.db.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public IPage page(SystemOrderTrackTable systemOrderTrackTable,Page page) {
        if (isNull(systemOrderTrackTable)) return new Page();
        LambdaQueryWrapper<SystemOrderTrackTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(systemOrderTrackTable.getMerOrderId())) queryWrapper.eq(SystemOrderTrackTable::getMerOrderId,systemOrderTrackTable.getMerOrderId());
        if (!isBlank(systemOrderTrackTable.getPlatformOrderId())) queryWrapper.eq(SystemOrderTrackTable::getPlatformOrderId,systemOrderTrackTable.getPlatformOrderId());
        if (!isBlank(systemOrderTrackTable.getMerId())) queryWrapper.eq(SystemOrderTrackTable::getMerId,systemOrderTrackTable.getMerId());
        if (!isBlank(systemOrderTrackTable.getBeginTime())) queryWrapper.gt(SystemOrderTrackTable::getCreateTime,systemOrderTrackTable.getBeginTime());
        if (!isBlank(systemOrderTrackTable.getEndTime())) queryWrapper.lt(SystemOrderTrackTable::getCreateTime,systemOrderTrackTable.getEndTime());
        queryWrapper.orderByDesc(SystemOrderTrackTable::getTradeTime);
        IPage iPage = new Page(page.getCurrent(),page.getSize());
        return systemOrderTrackDbService.page(iPage,queryWrapper);
    }


}
