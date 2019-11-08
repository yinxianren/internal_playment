package com.internal.playment.db.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.channel.ChannelWalletTable;
import com.internal.playment.db.mapper.channel.AnewChannelWalletMapper;
import com.internal.playment.db.service.db.channel.ChannelWalletDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午1:53
 * Description:
 */
@Service
public class ChannelWalletDbServiceImpl extends ServiceImpl<AnewChannelWalletMapper, ChannelWalletTable> implements ChannelWalletDbService {
}
