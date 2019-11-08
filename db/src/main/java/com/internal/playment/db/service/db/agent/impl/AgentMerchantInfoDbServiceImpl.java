package com.internal.playment.db.service.db.agent.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
import com.internal.playment.db.mapper.agent.AnewAgentMerchantInfoMapper;
import com.internal.playment.db.service.db.agent.AgentMerchantInfoDbService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午5:36
 * Description:
 */

@Service
public class AgentMerchantInfoDbServiceImpl extends ServiceImpl<AnewAgentMerchantInfoMapper, AgentMerchantInfoTable> implements AgentMerchantInfoDbService {
}
