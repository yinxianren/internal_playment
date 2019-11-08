package com.internal.playment.api.db.system;


import com.internal.playment.common.table.system.OrganizationInfoTable;

import java.util.List;

public interface ApiOrganizationInfoService {

     List<OrganizationInfoTable> getAll(OrganizationInfoTable organizationInfoTable);

     OrganizationInfoTable getOne(OrganizationInfoTable organizationInfoTable);

     Boolean saveOrUpdate(OrganizationInfoTable organizationInfoTable);

     Boolean remove(List<String> ids);

}
