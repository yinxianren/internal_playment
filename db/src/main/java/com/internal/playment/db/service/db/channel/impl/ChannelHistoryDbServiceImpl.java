package com.internal.playment.db.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.channel.ChannelHistoryTable;
import com.internal.playment.db.mapper.channel.AnewChannelHistoryMapper;
import com.internal.playment.db.service.db.channel.ChannelHistoryDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 上午9:57
 * Description:
 */
@Service
public class ChannelHistoryDbServiceImpl extends ServiceImpl<AnewChannelHistoryMapper, ChannelHistoryTable> implements ChannelHistoryDbService {
}
