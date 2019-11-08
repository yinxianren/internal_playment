package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.db.mapper.system.AnewOrganizationInfoMapper;
import com.internal.playment.db.service.db.system.OrganizationInfoDBService;
import org.springframework.stereotype.Service;

@Service
public class OrganizationInfoDBServiceImpl extends ServiceImpl<AnewOrganizationInfoMapper, OrganizationInfoTable> implements OrganizationInfoDBService {
}
