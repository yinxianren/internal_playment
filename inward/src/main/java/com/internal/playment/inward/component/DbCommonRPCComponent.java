package com.internal.playment.inward.component;

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
    public ApiBankRateService apiBankRateService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiBankCodeService apiBankCodeService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiRiskQuotaService apiRiskQuotaService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiChannelInfoService apiChannelInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiMerchantInfoService apiMerchantInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiMerchantCardService apiMerchantCardService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiPayOrderInfoService apiPayOrderInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiRegisterInfoService apiRegisterInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiMerchantRateService apiMerchantRateService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiChannelWalletService apiChannelWalletService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiChannelDetailsService apiChannelDetailsService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiTransOrderInfoService apiTransOrderInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiMerchantWalletService apiMerchantWalletService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiChannelHistoryService apiChannelHistoryService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiRegisterCollectService apiRegisterCollectService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiMerchantSettingService apiMerchantSettingService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiChannelExtraInfoService apiChannelExtraInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiSystemOrderTrackService apiSystemOrderTrackService;
    @Reference(version = "${application.version}",timeout = 30000)
    public  ApiOrganizationInfoService apiOrganizationInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiMerchantsDetailsService apiMerchantsDetailsService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiMerchantQuotaRiskService apiMerchantQuotaRiskService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiAgentMerchantInfoService apiAgentMerchantInfoService;
    @Reference(version = "${application.version}",timeout = 30000)
    public  ApiProductTypeSettingService apiProductTypeSettingService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiAgentMerchantWalletService apiAgentMerchantWalletService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiAgentMerchantSettingService apiAgentMerchantSettingService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiAgentMerchantsDetailsService apiAgentMerchantsDetailsService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiTerminalMerchantsWalletService apiTerminalMerchantsWalletService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiTerminalMerchantsDetailsService apiTerminalMerchantsDetailsService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiPayOrderBusinessTransactionService apiPayOrderBusinessTransactionService;
    @Reference(version = "${application.version}",timeout = 30000)
    public ApiTransOrderBusinessTransactionService apiTransOrderBusinessTransactionService;

}
