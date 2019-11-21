package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.UserLoginIpTable;
import com.internal.playment.db.mapper.system.AnewUserLoginIpMapper;
import com.internal.playment.db.service.db.system.UserLoginIpDBService;
import org.springframework.stereotype.Service;

@Service
public class UserLoginIpServiceImpl extends ServiceImpl<AnewUserLoginIpMapper, UserLoginIpTable> implements UserLoginIpDBService {
}
