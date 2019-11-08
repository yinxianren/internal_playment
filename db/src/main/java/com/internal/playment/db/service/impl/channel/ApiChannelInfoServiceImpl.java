package com.internal.playment.db.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.channel.ApiChannelInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.db.service.db.channel.ChannelInfoDbService;
import lombok.AllArgsConstructor;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiChannelInfoServiceImpl implements ApiChannelInfoService, NewPayAssert {

    private final ChannelInfoDbService channelInfoDbService;


    @Override
    public List<ChannelInfoTable> batchGetByChannelId(Set<String> channelIdSet) {
        if(isHasNotElement(channelIdSet)) return null;
        LambdaQueryWrapper<ChannelInfoTable>  lambdaQueryWrapper = new QueryWrapper<ChannelInfoTable>()
                .lambda().eq(ChannelInfoTable::getStatus, StatusEnum._0.getStatus());//默认只取可用通道
        lambdaQueryWrapper.in(ChannelInfoTable::getChannelId,channelIdSet);
        return channelInfoDbService.list(lambdaQueryWrapper);
    }

    @Override
    public ChannelInfoTable getOne(ChannelInfoTable cit) {
        if(isNull(cit)) return null;
        LambdaQueryWrapper<ChannelInfoTable>  lambdaQueryWrapper = new QueryWrapper<ChannelInfoTable>()
                .lambda().eq(ChannelInfoTable::getStatus, StatusEnum._0.getStatus());//默认只取可用通道
        if( !isBlank(cit.getChannelId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getChannelId,cit.getChannelId());
        if( !isBlank(cit.getOrganizationId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getOrganizationId,cit.getOrganizationId());
        if( !isBlank(cit.getProductId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getProductId,cit.getProductId());
        if( !isBlank(cit.getBusiType()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getBusiType,cit.getBusiType());
        if( !isNull(cit.getStatus())) lambdaQueryWrapper.eq(ChannelInfoTable::getStatus,cit.getStatus());
        return channelInfoDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<ChannelInfoTable> getList(ChannelInfoTable cit) {
        if(isNull(cit)) return channelInfoDbService.list();
        LambdaQueryWrapper<ChannelInfoTable>  lambdaQueryWrapper = new QueryWrapper<ChannelInfoTable>().lambda();
        if( !isBlank(cit.getChannelId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getChannelId,cit.getChannelId());
        if( !isBlank(cit.getOrganizationId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getOrganizationId,cit.getOrganizationId());
        if( !isNull(cit.getChannelLevel()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getChannelLevel,cit.getChannelLevel());
        if( !isBlank(cit.getProductId()) ) lambdaQueryWrapper.eq(ChannelInfoTable::getProductId,cit.getProductId());
        return channelInfoDbService.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(ChannelInfoTable cit) {
        if (isNull(cit)) return false;
        return channelInfoDbService.saveOrUpdate(cit);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return channelInfoDbService.removeByIds(ids);
    }

    @Override
    public List<ChannelInfoTable> getChannels(List<String> orgIds) {
        if (isHasNotElement(orgIds)) return null;
        LambdaQueryWrapper<ChannelInfoTable> queryWrapper = new QueryWrapper<ChannelInfoTable>().lambda()
                .eq(ChannelInfoTable::getStatus, StatusEnum._0.getStatus());//默认只取可用通道;
        queryWrapper.in(ChannelInfoTable::getOrganizationId,orgIds);
        return channelInfoDbService.list(queryWrapper);
    }
}
