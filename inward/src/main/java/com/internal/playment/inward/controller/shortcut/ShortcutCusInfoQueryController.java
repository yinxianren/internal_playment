package com.internal.playment.inward.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.internal.playment.common.dto.BusinessCusInfoQueryDTO;
import com.internal.playment.common.dto.BusinessOrderQueryDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.inward.component.Md5Component;
import com.internal.playment.inward.controller.NewAbstractCommonController;
import com.internal.playment.inward.service.shurtcut.ShortcutCusInfoQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class ShortcutCusInfoQueryController extends NewAbstractCommonController {

    private final Md5Component md5Component;
    private final ShortcutCusInfoQueryService shortcutCusInfoQueryService;

    @PostMapping(value = "/queryCusInfo", produces = "text/html;charset=UTF-8")
    public String businessOrderQuery(HttpServletRequest request, @RequestBody(required = false) String param) {
        final String bussType = "【查询子商户登记状态信息】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        BusinessCusInfoQueryDTO businessCusInfoQueryDTO = null;
        SystemOrderTrackTable sotTable = null;
        InnerPrintLogObject ipo = null;
        MerchantInfoTable merInfoTable = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            businessCusInfoQueryDTO = JSON.parse(sotTable.getRequestMsg(), BusinessCusInfoQueryDTO.class);
            sotTable.setMerId(businessCusInfoQueryDTO.getMerId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(businessCusInfoQueryDTO.getMerId(),businessCusInfoQueryDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = shortcutCusInfoQueryService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = shortcutCusInfoQueryService.getParamMapByBusinessOrderQuery();
            //参数校验
            this.verify(paramRuleMap,businessCusInfoQueryDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查询
            Map<String,Object> map = shortcutCusInfoQueryService.query(businessCusInfoQueryDTO,ipo);
            //生成返回结果
            respResult = shortcutCusInfoQueryService.responseMsg(map,merInfoTable);
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
                log.info(printErrorMsg);
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? "" : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            sotTable.setPlatformPrintLog(printErrorMsg);
            respResult = shortcutCusInfoQueryService.responseMsg(businessCusInfoQueryDTO,merInfoTable,errorCode,errorMsg);
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            shortcutCusInfoQueryService.saveSysLog(sotTable);
            return  null == respResult ? "系统内部错误！" : respResult;
        }

    }


}
