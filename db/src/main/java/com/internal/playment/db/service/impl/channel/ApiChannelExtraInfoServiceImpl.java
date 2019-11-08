package com.internal.playment.db.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.channel.ApiChannelExtraInfoService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.db.service.db.channel.ChannelExtraInfoDbService;
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
public class ApiChannelExtraInfoServiceImpl implements ApiChannelExtraInfoService, NewPayAssert {

    private final ChannelExtraInfoDbService channelExtraInfoDbService;


    @Override
    public ChannelExtraInfoTable getOne(ChannelExtraInfoTable cei) {
        if( isNull(cei) )  return null;
        LambdaQueryWrapper<ChannelExtraInfoTable> lambdaQueryWrapper = new QueryWrapper<ChannelExtraInfoTable>()
                .lambda().eq(ChannelExtraInfoTable::getStatus, StatusEnum._0.getStatus());//默认只取启用
        if( !isBlank(cei.getOrganizationId()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getOrganizationId,cei.getOrganizationId());
        if( !isBlank(cei.getBussType()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getBussType,cei.getBussType());
        return channelExtraInfoDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<ChannelExtraInfoTable> list(ChannelExtraInfoTable cei) {
        if( isNull(cei) )  return channelExtraInfoDbService.list();
        LambdaQueryWrapper<ChannelExtraInfoTable> lambdaQueryWrapper = new QueryWrapper<ChannelExtraInfoTable>().lambda();
        if( !isBlank(cei.getOrganizationId()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getOrganizationId,cei.getOrganizationId());
        if( !isBlank(cei.getBussType()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getBussType,cei.getBussType());
        if( !isNull(cei.getStatus()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getStatus,cei.getStatus());
        if( !isBlank(cei.getExtraChannelName()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getExtraChannelName,cei.getExtraChannelName());
        if( !isBlank(cei.getRequestUrl()) ) lambdaQueryWrapper.eq(ChannelExtraInfoTable::getRequestUrl,cei.getRequestUrl());
        return channelExtraInfoDbService.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(ChannelExtraInfoTable channelExtraInfoTable) {
        if (isNull(channelExtraInfoTable)) return  false;
        return channelExtraInfoDbService.saveOrUpdate(channelExtraInfoTable);
    }

    @Override
    public Boolean removeByIds(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return channelExtraInfoDbService.removeByIds(ids);
    }
}
