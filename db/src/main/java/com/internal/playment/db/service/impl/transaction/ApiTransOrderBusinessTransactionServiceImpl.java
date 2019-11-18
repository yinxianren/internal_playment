package com.internal.playment.db.service.impl.transaction;

import com.alibaba.dubbo.config.annotation.Service;
import com.internal.playment.api.db.transaction.ApiTransOrderBusinessTransactionService;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.db.service.db.business.PayOrderInfoDBService;
import com.internal.playment.db.service.db.business.TransOrderInfoDBService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/6
 * Time: 下午9:24
 * Description:
 */
@Transactional(rollbackFor= Exception.class)
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiTransOrderBusinessTransactionServiceImpl implements ApiTransOrderBusinessTransactionService {

    private final PayOrderInfoDBService payOrderInfoDBService;
    private final TransOrderInfoDBService transOrderInfoDBService;

    @Override
    public void updateByPayOrderCorrelationInfo(TransOrderInfoTable transOrderInfoTable, List<PayOrderInfoTable> payOrderInfoTableList) {
        transOrderInfoDBService.updateById(transOrderInfoTable);
        payOrderInfoDBService.updateBatchById(payOrderInfoTableList);
    }

    @Override
    public void updateAndSaveTransOderMsg(TransOrderInfoTable transOrderInfoTable, TransOrderInfoTable transOrderInfoTableHedging) {
        transOrderInfoDBService.save(transOrderInfoTableHedging);
        transOrderInfoDBService.updateById(transOrderInfoTable);
    }

}
