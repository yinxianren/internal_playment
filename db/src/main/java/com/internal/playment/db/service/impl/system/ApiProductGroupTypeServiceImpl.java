package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.internal.playment.api.db.system.ApiProductGroupTypeService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.ProductGroupTypeTable;
import com.internal.playment.db.service.db.system.ProductGroupTypeDbService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Service(version = "${application.version}" , timeout = 30000 )
public class ApiProductGroupTypeServiceImpl implements ApiProductGroupTypeService, NewPayAssert {

    private  final ProductGroupTypeDbService productGroupTypeDbService;


    @Override
    public boolean save(ProductGroupTypeTable pgt) {
        if(isNull(pgt)) return false;
        return  productGroupTypeDbService.save(pgt);
    }

    @Override
    public boolean update(ProductGroupTypeTable pgt) {
        if(isNull(pgt)) return false;
        LambdaUpdateWrapper<ProductGroupTypeTable> updateWrapper = new UpdateWrapper<ProductGroupTypeTable>().lambda();
        return  productGroupTypeDbService.update(updateWrapper);
    }

    @Override
    public boolean updateById(ProductGroupTypeTable pgt) {
        if(isNull(pgt)) return false;
        return  productGroupTypeDbService.updateById(pgt);
    }

    @Override
    public ProductGroupTypeTable getOne(ProductGroupTypeTable pgt) {
        if(isNull(pgt)) return null;
        LambdaQueryWrapper<ProductGroupTypeTable> queryWrapper = new QueryWrapper().lambda();

        return productGroupTypeDbService.getOne(queryWrapper);
    }

    @Override
    public List<ProductGroupTypeTable> getList(ProductGroupTypeTable pgt) {
        if(isNull(pgt)) return null;
        LambdaQueryWrapper<ProductGroupTypeTable> queryWrapper = new QueryWrapper().lambda();
        if( !isNull(pgt.getStatus()) )  queryWrapper.eq(ProductGroupTypeTable::getStatus,pgt.getStatus());
        if( !isBlank(pgt.getProductGroupId()) ) queryWrapper.eq(ProductGroupTypeTable::getProductGroupId,pgt.getProductGroupId());
        return productGroupTypeDbService.list(queryWrapper);
    }
}
