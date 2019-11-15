package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.SysGroupTable;
import com.internal.playment.db.mapper.system.AnewSysGroupMapper;
import com.internal.playment.db.service.db.system.SysGroupDBService;
import org.springframework.stereotype.Service;

@Service
public class SysGroupDBServiceImpl extends ServiceImpl<AnewSysGroupMapper, SysGroupTable> implements SysGroupDBService {
}
