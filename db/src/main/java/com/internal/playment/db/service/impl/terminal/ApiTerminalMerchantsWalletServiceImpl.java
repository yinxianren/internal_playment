package com.internal.playment.db.service.impl.terminal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.terminal.ApiTerminalMerchantsWalletService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.db.service.db.terminal.TerminalMerchantsWalletDBservice;
import lombok.AllArgsConstructor;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午4:33   timeout
 * Description:
 */
@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiTerminalMerchantsWalletServiceImpl implements ApiTerminalMerchantsWalletService, NewPayAssert {

    private final TerminalMerchantsWalletDBservice terminalMerchantsWalletDBservice;

    @Override
    public TerminalMerchantsWalletTable getOne(TerminalMerchantsWalletTable tmw) {
        if(isNull(tmw)) return null;
        LambdaQueryWrapper<TerminalMerchantsWalletTable> lambdaQueryWrapper =new QueryWrapper<TerminalMerchantsWalletTable>().lambda();
        if( !isBlank(tmw.getMerchantId()))  lambdaQueryWrapper.eq(TerminalMerchantsWalletTable::getMerchantId,tmw.getMerchantId());
        if( !isBlank(tmw.getTerminalMerId())) lambdaQueryWrapper.eq(TerminalMerchantsWalletTable::getTerminalMerId,tmw.getTerminalMerId());
        if( !isNull(tmw.getStatus()))  lambdaQueryWrapper.eq(TerminalMerchantsWalletTable::getStatus,tmw.getStatus());
        return terminalMerchantsWalletDBservice.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateOrSave(TerminalMerchantsWalletTable tmw) {
        if(isNull(tmw)) return false;
        return terminalMerchantsWalletDBservice.saveOrUpdate(tmw);
    }

    @Override
    public List<TerminalMerchantsWalletTable> getList(TerminalMerchantsWalletTable tmw) {
        if (isNull(tmw)) return terminalMerchantsWalletDBservice.list();
        LambdaQueryWrapper<TerminalMerchantsWalletTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isBlank(tmw.getMerchantId())) queryWrapper.eq(TerminalMerchantsWalletTable::getMerchantId,tmw.getMerchantId());
        if( !isBlank(tmw.getTerminalMerId())) queryWrapper.eq(TerminalMerchantsWalletTable::getTerminalMerId,tmw.getTerminalMerId());
        if( !isNull(tmw.getStatus()))  queryWrapper.eq(TerminalMerchantsWalletTable::getStatus,tmw.getStatus());
        return terminalMerchantsWalletDBservice.list(queryWrapper);
    }
}
