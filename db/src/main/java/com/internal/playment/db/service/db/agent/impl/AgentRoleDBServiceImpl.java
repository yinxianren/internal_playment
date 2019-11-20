package com.internal.playment.db.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.agent.AgentRoleTable;
import com.internal.playment.db.mapper.agent.AnewAgmentRoleMapper;
import com.internal.playment.db.service.db.agent.AgentRoleDBService;
import org.springframework.stereotype.Service;

@Service
public class AgentRoleDBServiceImpl extends ServiceImpl<AnewAgmentRoleMapper, AgentRoleTable> implements AgentRoleDBService {
}
