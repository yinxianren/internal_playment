package com.internal.playment.db.service.db.system.imple;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.db.mapper.system.AnewAsyncNotifyMapper;
import com.internal.playment.db.service.db.system.AsyncNotifyDbService;
import org.springframework.stereotype.Service;

@Service
public class AsyncNotifyDbServiceImpl extends ServiceImpl<AnewAsyncNotifyMapper, AsyncNotifyTable> implements AsyncNotifyDbService {
}
