package com.internal.playment.db.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.channel.ApiChannelHistoryService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.channel.ChannelHistoryTable;
import com.internal.playment.db.service.db.channel.ChannelHistoryDbService;
import lombok.AllArgsConstructor;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiChannelHistoryServiceImpl implements ApiChannelHistoryService, NewPayAssert {

    private final ChannelHistoryDbService channelHistoryDbService;


    @Override
    public ChannelHistoryTable getOne(ChannelHistoryTable ch) {
        if(isNull(ch)) return null;
        LambdaQueryWrapper<ChannelHistoryTable> lambdaQueryWrapper = new QueryWrapper<ChannelHistoryTable>().lambda();
        if( !isBlank(ch.getMerchantId()) ) lambdaQueryWrapper.eq(ChannelHistoryTable::getMerchantId,ch.getMerchantId());
        if( !isBlank(ch.getTerminalMerId()) ) lambdaQueryWrapper.eq(ChannelHistoryTable::getTerminalMerId,ch.getTerminalMerId());
        if( !isNull(ch.getCreateTime()) ) lambdaQueryWrapper.eq(ChannelHistoryTable::getCreateTime,ch.getCreateTime());
        return channelHistoryDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(ChannelHistoryTable ch) {
        if(isNull(ch)) return false;
        return channelHistoryDbService.save(ch);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<ChannelHistoryTable> entityList) {
        if(isHasNotElement(entityList)) return false;
        return channelHistoryDbService.saveOrUpdateBatch(entityList);
    }


}
