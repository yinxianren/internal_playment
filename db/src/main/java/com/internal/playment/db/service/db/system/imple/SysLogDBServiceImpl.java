package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.SysLogTable;
import com.internal.playment.db.mapper.system.AnewSysLogMapper;
import com.internal.playment.db.service.db.system.SysLogDBService;
import org.springframework.stereotype.Service;

@Service
public class SysLogDBServiceImpl extends ServiceImpl<AnewSysLogMapper, SysLogTable> implements SysLogDBService {
}
