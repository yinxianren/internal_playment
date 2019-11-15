package com.internal.playment.pay.controller.shortcut;


import com.alibaba.dubbo.common.json.JSON;
import com.internal.playment.common.dto.BusinessUnBondCardDTO;
import com.internal.playment.common.dto.MerTransOrderApplyDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.pay.component.Md5Component;
import com.internal.playment.pay.controller.NewAbstractCommonController;
import com.internal.playment.pay.service.shortcut.NewUnBondCardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewUnBondCardController extends NewAbstractCommonController {

    private final NewUnBondCardService newUnBondCardService;
    private final Md5Component md5Component;

    /**
     *解绑消费银行卡
     * @return
     */
    @PostMapping(value = "/unBondCard", produces = "text/html;charset=UTF-8")
    public String unBondCard(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【解绑消费银行卡】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        BusinessUnBondCardDTO businessUnBondCardDTO;
        SystemOrderTrackTable sotTable = null;
        InnerPrintLogObject ipo = null ;
        MerchantInfoTable merInfoTable = null;
        try{
            //0.解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            businessUnBondCardDTO = JSON.parse(sotTable.getRequestMsg(), BusinessUnBondCardDTO.class);
            sotTable.setMerId(businessUnBondCardDTO.getMerId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(businessUnBondCardDTO.getMerId(), businessUnBondCardDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newUnBondCardService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newUnBondCardService.getParamMapByUnBondCard();


        }catch (Exception e){

        }finally {

            return  null;
        }
    }

}
