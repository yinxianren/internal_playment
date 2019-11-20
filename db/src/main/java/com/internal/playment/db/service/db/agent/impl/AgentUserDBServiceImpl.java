package com.internal.playment.db.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.agent.AgentUserTable;
import com.internal.playment.db.mapper.agent.AnewAgmentUserMapper;
import com.internal.playment.db.service.db.agent.AgentUserDBService;
import org.springframework.stereotype.Service;

@Service
public class AgentUserDBServiceImpl extends ServiceImpl<AnewAgmentUserMapper, AgentUserTable> implements AgentUserDBService {
}
