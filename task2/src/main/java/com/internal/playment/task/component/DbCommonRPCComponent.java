package com.internal.playment.task.component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.internal.playment.api.db.agent.ApiAgentMerchantInfoService;
import com.internal.playment.api.db.agent.ApiAgentMerchantSettingService;
import com.internal.playment.api.db.agent.ApiAgentMerchantWalletService;
import com.internal.playment.api.db.agent.ApiAgentMerchantsDetailsService;
import com.internal.playment.api.db.business.*;
import com.internal.playment.api.db.channel.*;
import com.internal.playment.api.db.merchant.*;
import com.internal.playment.api.db.system.*;
import com.internal.playment.api.db.terminal.ApiTerminalMerchantsDetailsService;
import com.internal.playment.api.db.terminal.ApiTerminalMerchantsWalletService;
import com.internal.playment.api.db.transaction.ApiPayOrderBusinessTransactionService;
import com.internal.playment.api.db.transaction.ApiTransOrderBusinessTransactionService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/21
 * Time: 上午9:06
 * Description:
 */

@Component
public  class DbCommonRPCComponent {

    @Reference(version = "${application.version}",timeout = 30000)
    public ApiPayOrderInfoService apiPayOrderInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiTransOrderInfoService apiTransOrderInfoService;

}
