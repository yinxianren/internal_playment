package com.internal.playment.inward.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.internal.playment.common.dto.BusinessOrderQueryDTO;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.inward.component.Md5Component;
import com.internal.playment.inward.controller.NewAbstractCommonController;
import com.internal.playment.inward.service.shurtcut.ShortcutOrderQueryService;
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
public class ShortcutOrderQueryController extends NewAbstractCommonController {

    private final ShortcutOrderQueryService shortcutOrderQueryService;
    private final Md5Component md5Component;

    /**
     *  查询订单处理结果
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/queryOrder", produces = "text/html;charset=UTF-8")
    public String businessOrderQuery(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【查询订单处理结果】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        BusinessOrderQueryDTO businessOrderQueryDTO = null;
        SystemOrderTrackTable sotTable = null;
        InnerPrintLogObject ipo = null;
        MerchantInfoTable merInfoTable = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            businessOrderQueryDTO = JSON.parse(sotTable.getRequestMsg(),BusinessOrderQueryDTO.class);
            sotTable.setMerId(businessOrderQueryDTO.getMerId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(businessOrderQueryDTO.getMerId(),businessOrderQueryDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = shortcutOrderQueryService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = shortcutOrderQueryService.getParamMapByBusinessOrderQuery();
            //参数校验
            this.verify(paramRuleMap,businessOrderQueryDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查询订单
            Map<String,Object> map = shortcutOrderQueryService.query(businessOrderQueryDTO,ipo);
            //生成返回结果
            respResult = shortcutOrderQueryService.responseMsg(map,merInfoTable);
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
            respResult = shortcutOrderQueryService.responseMsg(businessOrderQueryDTO,merInfoTable,errorCode,errorMsg);
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            shortcutOrderQueryService.saveSysLog(sotTable);
            return  null == respResult ? "系统内部错误！" : respResult;
        }
    }

}
