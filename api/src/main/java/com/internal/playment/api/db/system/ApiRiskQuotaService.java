package com.internal.playment.api.db.system;


import com.internal.playment.common.table.system.RiskQuotaTable;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 上午10:56
 * Description:
 */
public interface ApiRiskQuotaService {

    List<RiskQuotaTable> getListByTimeType(Set<String> timeTypeSet, RiskQuotaTable rq);

    List<RiskQuotaTable>  getListByChMerId(Set<String> meridChannelidSet, RiskQuotaTable rq);

    boolean save(RiskQuotaTable rq);

    boolean updateByPrimaryKey(RiskQuotaTable rq);
}
