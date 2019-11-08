package com.internal.playment.api.db.channel;


import com.internal.playment.common.table.channel.ChannelExtraInfoTable;

import java.util.List;

public interface ApiChannelExtraInfoService {

    ChannelExtraInfoTable getOne(ChannelExtraInfoTable cei);

    List<ChannelExtraInfoTable> list(ChannelExtraInfoTable cei);

    Boolean saveOrUpdate(ChannelExtraInfoTable channelExtraInfoTable);

    Boolean removeByIds(List<String> ids);
}
