package com.internal.playment.db.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.api.entity.table.system.SystemLogTable;
import com.internal.playment.db.mapper.system.SystemLogMapper;
import com.internal.playment.db.service.system.SystemLogService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午7:34
 * Description:
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLogTable> implements SystemLogService {
}
