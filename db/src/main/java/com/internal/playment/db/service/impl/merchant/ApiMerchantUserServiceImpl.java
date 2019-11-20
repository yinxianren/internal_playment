package com.internal.playment.db.service.impl.merchant;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.merchant.ApiMerchantUserService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantUserTable;
import com.internal.playment.db.service.db.merchant.MerchantUserDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}",timeout = 30000)
public class ApiMerchantUserServiceImpl implements ApiMerchantUserService, NewPayAssert {

    @Autowired
    private MerchantUserDBService merchantUserDBService;

    @Override
    public boolean saveOrUpdate(MerchantUserTable merchantUserTable) {
        if (isNull(merchantUserTable)) return false;
        return merchantUserDBService.saveOrUpdate(merchantUserTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return false;
        return merchantUserDBService.removeByIds(ids);
    }

    @Override
    public IPage page(MerchantUserTable merchantUserTable, PageDTO pageDTO) {
        LambdaQueryWrapper<MerchantUserTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return merchantUserDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<MerchantUserTable> getList(MerchantUserTable merchantUserTable) {
        if (isNull(merchantUserTable)) return merchantUserDBService.list();
        LambdaQueryWrapper<MerchantUserTable> queryWrapper = new LambdaQueryWrapper<>();
        return merchantUserDBService.list(queryWrapper);
    }
}
