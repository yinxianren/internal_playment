package com.internal.playment.db.service.impl.channel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.channel.ApiProductTypeSettingService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.ProductSettingTable;
import com.internal.playment.db.service.db.system.ProductTypeSettingDBService;
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
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiProductTypeSettingServiceImpl implements ApiProductTypeSettingService, NewPayAssert {

    @Autowired
    private ProductTypeSettingDBService productTypeSettingDBService;

    public Boolean SaveOrUpdate(ProductSettingTable productSettingTable){
        if(isNull(productSettingTable)) return false;
        return productTypeSettingDBService.saveOrUpdate(productSettingTable);
    }

    public Boolean removeById(List<Long> ids){
        if(isHasNotElement(ids))  return false;
        return productTypeSettingDBService.removeByIds(ids);
    }

    public List<ProductSettingTable> list(ProductSettingTable productSettingTable){
        if(isNull(productSettingTable)) return null;
        LambdaQueryWrapper<ProductSettingTable> lambdaQueryWrapper = new QueryWrapper<ProductSettingTable>().lambda();
        if( !isBlank(productSettingTable.getOrganizationId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationId());
        if( isHasElement(productSettingTable.getOrganizationIds()))  lambdaQueryWrapper.in(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationIds());
        if( !isBlank(productSettingTable.getProductId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getProductId,productSettingTable.getProductId());
        if( !isNull(productSettingTable.getStatus()) ) lambdaQueryWrapper.eq(ProductSettingTable::getStatus,productSettingTable.getStatus());
        return productTypeSettingDBService.list(lambdaQueryWrapper);
    }

    @Override
    public ProductSettingTable getOne(ProductSettingTable productSettingTable) {
        if(isNull(productSettingTable)) return null;
        LambdaQueryWrapper<ProductSettingTable> lambdaQueryWrapper = new QueryWrapper<ProductSettingTable>()
                .lambda().eq(ProductSettingTable::getStatus, StatusEnum._0.getStatus());//默认只取可用的

        if( !isBlank(productSettingTable.getOrganizationId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getOrganizationId,productSettingTable.getOrganizationId());
        if( !isBlank(productSettingTable.getProductName()) ) lambdaQueryWrapper.eq(ProductSettingTable::getProductName,productSettingTable.getProductName());
        if( !isBlank(productSettingTable.getProductId()) ) lambdaQueryWrapper.eq(ProductSettingTable::getProductId,productSettingTable.getProductId());
        return productTypeSettingDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public Boolean batchUpdate(List<ProductSettingTable> productSettingTableList) {
        if(isHasNotElement(productSettingTableList)) return false;
        return productTypeSettingDBService.updateBatchById(productSettingTableList);
    }
}
