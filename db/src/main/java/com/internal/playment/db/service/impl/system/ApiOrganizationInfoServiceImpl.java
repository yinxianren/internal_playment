package com.internal.playment.db.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.internal.playment.api.db.system.ApiOrganizationInfoService;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.db.service.db.system.OrganizationInfoDBService;
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
public class ApiOrganizationInfoServiceImpl implements ApiOrganizationInfoService, NewPayAssert {


    private OrganizationInfoDBService organizationInfoDBService;

    @Override
    public List<OrganizationInfoTable> getAll(OrganizationInfoTable oit) {
        if (isNull(oit)) return organizationInfoDBService.list();
        LambdaQueryWrapper<OrganizationInfoTable> queryWrapper = new QueryWrapper<OrganizationInfoTable>().lambda();
        if ( !isBlank(oit.getOrganizationName()))  queryWrapper.eq(OrganizationInfoTable::getOrganizationName, oit.getOrganizationName());
        if ( !isNull(oit.getId()) )  queryWrapper.eq(OrganizationInfoTable::getId, oit.getId());
        if ( isHasElement(oit.getOrganizationIdColl()) )  queryWrapper.in(OrganizationInfoTable::getOrganizationIdColl, oit.getOrganizationIdColl());
        if ( !isNull(oit.getStatus()))  queryWrapper.eq(OrganizationInfoTable::getStatus, oit.getStatus());
        return organizationInfoDBService.list(queryWrapper);
    }

    @Override
    public OrganizationInfoTable getOne(OrganizationInfoTable oit) {
        if (isNull(oit)) return  null;
        LambdaQueryWrapper<OrganizationInfoTable> queryWrapper = new QueryWrapper<OrganizationInfoTable>().lambda();
        if ( !isBlank(oit.getOrganizationId()))  queryWrapper.eq(OrganizationInfoTable::getOrganizationId, oit.getOrganizationId());
        if ( !isNull(oit.getStatus()) )  queryWrapper.eq(OrganizationInfoTable::getStatus, oit.getStatus());
        return organizationInfoDBService.getOne(queryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(OrganizationInfoTable organizationInfoTable) {
        if (isNull(organizationInfoTable)) return false;
        return organizationInfoDBService.saveOrUpdate(organizationInfoTable);
    }

    @Override
    public Boolean remove(List<String> ids) {
        if (isHasNotElement(ids)) return false;
        return organizationInfoDBService.removeByIds(ids);
    }
}
