package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.AgentSysLogTable;
import com.internal.playment.db.mapper.system.AnewAgentSysLogMapper;
import com.internal.playment.db.service.db.system.AgentSysLogDBService;
import org.springframework.stereotype.Service;

@Service
public class AgentSysLogTableImpl extends ServiceImpl<AnewAgentSysLogMapper, AgentSysLogTable> implements AgentSysLogDBService {
}
