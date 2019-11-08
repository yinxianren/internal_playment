package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.BankRateTable;
import com.internal.playment.db.mapper.system.AnewBankRateMapper;
import com.internal.playment.db.service.db.system.BankRateDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午3:51
 * Description:
 */
@Service
public class BankRateDbServiceImpl extends ServiceImpl<AnewBankRateMapper, BankRateTable> implements BankRateDbService {
}
