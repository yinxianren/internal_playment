package com.internal.playment.db.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.db.mapper.business.AnewRegisterInfoMapper;
import com.internal.playment.db.service.db.business.RegisterInfoDBService;
import org.springframework.stereotype.Service;

@Service
public class RegisterInfoDBServiceImpl extends ServiceImpl<AnewRegisterInfoMapper, RegisterInfoTable> implements RegisterInfoDBService {

    @Override
    public boolean replaceSave(RegisterInfoTable registerInfoTable) {
        return this.baseMapper.replaceSave(registerInfoTable);
    }
}
