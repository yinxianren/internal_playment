package com.internal.playment.db.service.impl.merchant;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.merchant.ApiMerchantPrivilegesService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantPrivilegesTable;
import com.internal.playment.db.service.db.merchant.MerchantPrivilegesDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version ="${application.version}",timeout = 30000)
public class ApiMerchantPrivilegesServiceImpl implements ApiMerchantPrivilegesService, NewPayAssert {

    @Autowired
    private MerchantPrivilegesDBService merchantPrivilegesDBService;

    @Override
    public boolean saveOrUpdate(MerchantPrivilegesTable merchantPrivilegesTable) {
        if (isNull(merchantPrivilegesTable)) return false;
        return merchantPrivilegesDBService.saveOrUpdate(merchantPrivilegesTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if(isHasNotElement(ids)) return false;
        return merchantPrivilegesDBService.removeByIds(ids);
    }

    @Override
    public IPage page(MerchantPrivilegesTable merchantPrivilegesTable, PageDTO pageDTO) {
        if (isNull(merchantPrivilegesTable)) return new Page();
        LambdaQueryWrapper<MerchantPrivilegesTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return merchantPrivilegesDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<MerchantPrivilegesTable> getList(MerchantPrivilegesTable merchantPrivilegesTable) {
        if (isNull(merchantPrivilegesTable)) return merchantPrivilegesDBService.list();
        LambdaQueryWrapper<MerchantPrivilegesTable> queryWrapper = new LambdaQueryWrapper<>();
        return merchantPrivilegesDBService.list(queryWrapper);
    }
}
