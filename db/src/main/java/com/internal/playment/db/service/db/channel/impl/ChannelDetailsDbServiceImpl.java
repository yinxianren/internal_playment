package com.internal.playment.db.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.channel.ChannelDetailsTable;
import com.internal.playment.db.mapper.channel.AnewChannelDetailsMapper;
import com.internal.playment.db.service.db.channel.ChannelDetailsDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午1:53
 * Description:
 */
@Service
public class ChannelDetailsDbServiceImpl extends ServiceImpl<AnewChannelDetailsMapper, ChannelDetailsTable>implements ChannelDetailsDbService {
}
