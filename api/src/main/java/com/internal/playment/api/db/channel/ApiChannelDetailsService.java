package com.internal.playment.api.db.channel;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.table.channel.ChannelDetailsTable;

public interface ApiChannelDetailsService {

    ChannelDetailsTable getOne(ChannelDetailsTable cdt);

    boolean updateOrSave(ChannelDetailsTable cdt);

    IPage page(ChannelDetailsTable cdt);
}
