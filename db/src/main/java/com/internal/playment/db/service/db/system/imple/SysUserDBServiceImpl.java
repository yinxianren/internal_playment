package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.SysUserTable;
import com.internal.playment.db.mapper.system.AnewSysUserMapper;
import com.internal.playment.db.service.db.system.SysUserDBService;
import org.springframework.stereotype.Service;

@Service
public class SysUserDBServiceImpl extends ServiceImpl<AnewSysUserMapper, SysUserTable> implements SysUserDBService {
}
