package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.internal.playment.db.mapper.system.AnewSysPrivilegesMapper;
import com.internal.playment.db.service.db.system.SysPrivilegesDBService;
import org.springframework.stereotype.Service;

@Service
public class SysPrivilegesDBServiceImpl extends ServiceImpl<AnewSysPrivilegesMapper, SysPrivilegesTable> implements SysPrivilegesDBService {
}
