package com.internal.playment.api.db.channel;


import com.internal.playment.common.table.channel.ChannelHistoryTable;

import java.util.Collection;

public interface ApiChannelHistoryService {

    ChannelHistoryTable getOne(ChannelHistoryTable ch);

    boolean save(ChannelHistoryTable ch);

    boolean saveOrUpdateBatch(Collection<ChannelHistoryTable> entityList);
}
