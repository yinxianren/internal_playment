package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.SysConstantTable;
import com.internal.playment.db.mapper.system.AnewSysConstantMapper;
import com.internal.playment.db.service.db.system.SysConstantDBService;
import org.springframework.stereotype.Service;

@Service
public class SysConstantDBServiceImpl extends ServiceImpl<AnewSysConstantMapper, SysConstantTable> implements SysConstantDBService {
}
