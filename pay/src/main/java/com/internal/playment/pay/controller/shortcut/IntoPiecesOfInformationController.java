package com.internal.playment.pay.controller.shortcut;


import com.alibaba.dubbo.common.json.JSON;
import com.internal.playment.api.entity.dto.MerchantBasicInformationRegistrationDTO;
import com.internal.playment.api.entity.inner.InnerPrintLogObject;
import com.internal.playment.api.entity.inner.ParamRule;
import com.internal.playment.api.entity.table.system.SystemOrderTrackTable;
import com.internal.playment.api.exception.PayException;
import com.internal.playment.pay.controller.AbstractCommonController;
import com.internal.playment.pay.service.shortcut.IntoPiecesOfInformationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 上午9:42
 * Description:
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/kuaijie")
public class IntoPiecesOfInformationController  extends AbstractCommonController {

    private  final IntoPiecesOfInformationService  intoPiecesOfInformationService;

    @PostMapping("//addCusInfo")
    public String intoPiecesOfInformation(HttpServletRequest request, @RequestBody String reqParam){
        final String bussType = "基本信息登记";
        SystemOrderTrackTable  sotTable = new SystemOrderTrackTable();
        MerchantBasicInformationRegistrationDTO mbirDTO = null;
        try{
            //风控前置表数据 以及解析参数
            this.setSystemOrderTrack(request,reqParam,sotTable,bussType);
            //类型转换
            mbirDTO = JSON.parse(sotTable.getRequestMsg(),MerchantBasicInformationRegistrationDTO.class);
            //获取必要参数
            Map<String, ParamRule> mustParamList = intoPiecesOfInformationService.intoPiecesOfInformationMustParam();
            //创建日志打印对象
            InnerPrintLogObject ipo = new InnerPrintLogObject(mbirDTO.getMerId(),mbirDTO.getMerOrderId(),bussType);
            //验证必要参数
            this.mustParameterValidation(mustParamList,mbirDTO,MerchantBasicInformationRegistrationDTO.class,ipo);
            //根据商户号获取商户信息


        }catch (Exception  e){
            if(e instanceof PayException){

            }else{
                e.printStackTrace();
            }
        }finally {

            return  null;
        }

    }


}
