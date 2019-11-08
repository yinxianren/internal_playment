package com.internal.playment.db.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.system.ApiBankRateService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.BankRateTable;
import com.internal.playment.db.service.db.system.BankRateDbService;
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
public class ApiBankRateServiceImpl implements ApiBankRateService, NewPayAssert {

    private final BankRateDbService bankRateDbService;

    @Override
    public BankRateTable getOne(BankRateTable brt) {
        if(isNull(brt)) return null;
        LambdaQueryWrapper<BankRateTable> lambdaQueryWrapper =new QueryWrapper<BankRateTable>().lambda();
        if( !isBlank(brt.getBankCode()) ) lambdaQueryWrapper.eq(BankRateTable::getBankCode,brt.getBankCode());
        if( !isBlank(brt.getOrganizationId()) ) lambdaQueryWrapper.eq(BankRateTable::getOrganizationId,brt.getOrganizationId());
        if( !isBlank(brt.getProductId()) ) lambdaQueryWrapper.eq(BankRateTable::getProductId,brt.getProductId());
        return bankRateDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<BankRateTable> getList(BankRateTable brt) {
        if(isNull(brt)) return null;
        LambdaQueryWrapper<BankRateTable> lambdaQueryWrapper =new QueryWrapper<BankRateTable>().lambda();
        if( !isBlank(brt.getBankCode()) ) lambdaQueryWrapper.eq(BankRateTable::getBankCode,brt.getBankCode());
        if( !isBlank(brt.getOrganizationId()) ) lambdaQueryWrapper.eq(BankRateTable::getOrganizationId,brt.getOrganizationId());
        if( !isBlank(brt.getProductId()) ) lambdaQueryWrapper.eq(BankRateTable::getProductId,brt.getProductId());
        return bankRateDbService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(BankRateTable brt) {
        if(isNull(brt)) return false;
        return bankRateDbService.save(brt);
    }
}
