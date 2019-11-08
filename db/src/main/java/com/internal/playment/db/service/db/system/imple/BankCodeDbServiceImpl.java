package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.BankCodeTable;
import com.internal.playment.db.mapper.system.AnewBankCodeMapper;
import com.internal.playment.db.service.db.system.BankCodeDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午3:51
 * Description:
 */
@Service
public class BankCodeDbServiceImpl extends ServiceImpl<AnewBankCodeMapper, BankCodeTable> implements BankCodeDbService {
}
