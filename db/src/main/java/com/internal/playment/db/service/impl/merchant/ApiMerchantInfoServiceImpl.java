package com.internal.playment.db.service.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.merchant.ApiMerchantInfoService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.db.service.db.merchant.MerchantInfoDbService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ApiMerchantInfoServiceImpl implements ApiMerchantInfoService, NewPayAssert {

    @Autowired
    private MerchantInfoDbService merchantInfoDbService;

    @Override
    public MerchantInfoTable getOne(MerchantInfoTable mit) {
        if(isNull(mit)) return null;
        LambdaQueryWrapper<MerchantInfoTable>  lambdaQueryWrapper=new QueryWrapper<MerchantInfoTable>().lambda();
        if( !isBlank(mit.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getMerchantId,mit.getMerchantId());
        return merchantInfoDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<MerchantInfoTable> getMerchants(MerchantInfoTable merchantInfoTable) {
        if (isNull(merchantInfoTable)) return merchantInfoDbService.list();
        LambdaQueryWrapper<MerchantInfoTable>  lambdaQueryWrapper=new QueryWrapper<MerchantInfoTable>().lambda();
        if( !isBlank(merchantInfoTable.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getMerchantId,merchantInfoTable.getMerchantId());
        if( !isNull(merchantInfoTable.getStatus()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getStatus,merchantInfoTable.getStatus());
        if( !isBlank(merchantInfoTable.getType()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getType,merchantInfoTable.getType());
        if( !isNull(merchantInfoTable.getId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getId,merchantInfoTable.getId());
        if( !isNull(merchantInfoTable.getAgentMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getAgentMerchantId,merchantInfoTable.getAgentMerchantId());
        return merchantInfoDbService.list(lambdaQueryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(MerchantInfoTable merchantInfoTable) {
        if (isNull(merchantInfoTable)) return false;
        return merchantInfoDbService.saveOrUpdate(merchantInfoTable);
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return merchantInfoDbService.removeByIds(ids);
    }


}
