package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.SysRoleTable;
import com.internal.playment.db.mapper.system.AnewSysRoleMapper;
import com.internal.playment.db.service.db.system.SysRoleDBService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleDBServiceImpl extends ServiceImpl<AnewSysRoleMapper, SysRoleTable> implements SysRoleDBService {
}
