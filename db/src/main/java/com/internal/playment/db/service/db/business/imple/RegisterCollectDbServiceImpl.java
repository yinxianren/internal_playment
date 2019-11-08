package com.internal.playment.db.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.db.mapper.business.AnewRegisterCollectMapper;
import com.internal.playment.db.service.db.business.RegisterCollectDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:40
 * Description:
 */
@Service
public class RegisterCollectDbServiceImpl extends ServiceImpl<AnewRegisterCollectMapper, RegisterCollectTable> implements RegisterCollectDbService {
}
