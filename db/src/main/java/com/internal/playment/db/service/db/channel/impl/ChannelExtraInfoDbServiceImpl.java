package com.internal.playment.db.service.db.channel.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.db.mapper.channel.AnewChannelExtraInfoMapper;
import com.internal.playment.db.service.db.channel.ChannelExtraInfoDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午10:18
 * Description:
 */
@Service
public class ChannelExtraInfoDbServiceImpl extends ServiceImpl<AnewChannelExtraInfoMapper, ChannelExtraInfoTable> implements ChannelExtraInfoDbService {
}
