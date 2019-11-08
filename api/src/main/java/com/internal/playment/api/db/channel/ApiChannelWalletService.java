package com.internal.playment.api.db.channel;


import com.internal.playment.common.table.channel.ChannelWalletTable;

import java.util.List;

public interface ApiChannelWalletService {

    ChannelWalletTable getOne(ChannelWalletTable cwt);

    boolean updateOrSave(ChannelWalletTable cwt);

    List<ChannelWalletTable> getList(ChannelWalletTable cwt);
}
