package com.internal.playment.db.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.api.db.system.ApiUserLoginIpService;
import com.internal.playment.common.dto.PageDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.UserLoginIpTable;
import com.internal.playment.db.service.db.system.UserLoginIpDBService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "${application.version}" , timeout = 30000)
public class ApiUserLoginIpServiceImpl implements ApiUserLoginIpService, NewPayAssert {

    @Autowired
    private UserLoginIpDBService userLoginIpDBService;

    @Override
    public boolean saveOrUpdate(UserLoginIpTable userLoginIpTable) {
        if (isNull(userLoginIpTable)) return false;
        return userLoginIpDBService.saveOrUpdate(userLoginIpTable);
    }

    @Override
    public boolean delByIds(List<Long> ids) {
        if (isHasNotElement(ids)) return false;
        return userLoginIpDBService.removeByIds(ids);
    }

    @Override
    public IPage page(UserLoginIpTable userLoginIpTable, PageDTO pageDTO) {
        LambdaQueryWrapper<UserLoginIpTable> queryWrapper = new LambdaQueryWrapper<>();
        IPage iPage = new Page(pageDTO.getPageNum(),pageDTO.getPaegSize());
        if (!isBlank(userLoginIpTable.getCustomerId())) queryWrapper.eq(UserLoginIpTable::getCustomerId,userLoginIpTable.getCustomerId());
        if (!isBlank(userLoginIpTable.getLoginIp())) queryWrapper.eq(UserLoginIpTable::getLoginIp,userLoginIpTable.getLoginIp());
        return userLoginIpDBService.page(iPage,queryWrapper);
    }
}
