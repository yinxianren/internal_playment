package com.internal.playment.db.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.channel.ApiChannelWalletService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.channel.ChannelWalletTable;
import com.internal.playment.db.service.db.channel.ChannelWalletDbService;
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
public class ApiChannelWalletServiceImpl implements ApiChannelWalletService, NewPayAssert {

   private final ChannelWalletDbService channelWalletDbService;

    @Override
    public ChannelWalletTable getOne(ChannelWalletTable cwt) {
        if(isNull(cwt)) return null;
        LambdaQueryWrapper<ChannelWalletTable> lambdaQueryWrapper = new QueryWrapper<ChannelWalletTable>().lambda();
        if( !isBlank(cwt.getChannelId())) lambdaQueryWrapper.eq(ChannelWalletTable::getChannelId,cwt.getChannelId());
        if( !isBlank(cwt.getOrganizationId())) lambdaQueryWrapper.eq(ChannelWalletTable::getOrganizationId,cwt.getOrganizationId());
        if( !isNull(cwt.getStatus())) lambdaQueryWrapper.eq(ChannelWalletTable::getStatus,cwt.getStatus());
        return channelWalletDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateOrSave(ChannelWalletTable cwt) {
        if(isNull(cwt)) return  false;
        return channelWalletDbService.saveOrUpdate(cwt);
    }

    @Override
    public List<ChannelWalletTable> getList(ChannelWalletTable cwt) {
        if(isNull(cwt)) return  channelWalletDbService.list();
        LambdaQueryWrapper<ChannelWalletTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isBlank(cwt.getChannelId())) queryWrapper.eq(ChannelWalletTable::getChannelId,cwt.getChannelId());
        return channelWalletDbService.list(queryWrapper);
    }
}
