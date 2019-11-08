package com.internal.playment.db.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.db.mapper.channel.AnewChannelInfoMapper;
import com.internal.playment.db.service.db.channel.ChannelInfoDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午3:47
 * Description:
 */
@Service
public class ChannelInfoDbServiceImpl extends ServiceImpl<AnewChannelInfoMapper, ChannelInfoTable> implements ChannelInfoDbService {
}
