package com.internal.playment.db.service.impl.merchant;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.merchant.ApiMerchantRoleService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.merchant.MerchantRoleTable;
import com.internal.playment.db.service.db.merchant.MerchantRoleDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version="${application.version}",timeout = 30000)
public class ApiMerchantRoleServiceImpl implements ApiMerchantRoleService, NewPayAssert {

    @Autowired
    private MerchantRoleDBService merchantRoleDBService;
    @Override
    public boolean saveOrUpdate(MerchantRoleTable merchantRoleTable) {
        if (isNull(merchantRoleTable)) return false;
        return merchantRoleDBService.saveOrUpdate(merchantRoleTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isNull(isHasNotElement(ids))) return false;
        return merchantRoleDBService.removeByIds(ids);
    }

    @Override
    public IPage page(MerchantRoleTable merchantRoleTable, PageDTO pageDTO) {
        LambdaQueryWrapper<MerchantRoleTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        return merchantRoleDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<MerchantRoleTable> getList(MerchantRoleTable merchantRoleTable) {
        if (isNull(merchantRoleTable)) return merchantRoleDBService.list();
        LambdaQueryWrapper<MerchantRoleTable> queryWrapper = new LambdaQueryWrapper<>();
        if (!isBlank(merchantRoleTable.getBelongto())) queryWrapper.eq(MerchantRoleTable::getBelongto,merchantRoleTable.getBelongto());
        return merchantRoleDBService.list(queryWrapper);
    }
}
