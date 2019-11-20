package com.internal.playment.db.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.agent.AgentPrivielgesTable;
import com.internal.playment.db.mapper.agent.AnewAgmentPrivielgesMapper;
import com.internal.playment.db.service.db.agent.AgentPrivielgesDBService;
import org.springframework.stereotype.Service;

@Service
public class AgentPrivielgesDBServiceImpl extends ServiceImpl<AnewAgmentPrivielgesMapper, AgentPrivielgesTable> implements AgentPrivielgesDBService {
}
