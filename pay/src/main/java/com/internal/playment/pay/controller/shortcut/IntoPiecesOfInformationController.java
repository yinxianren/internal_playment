package com.internal.playment.pay.controller.shortcut;


import com.internal.playment.api.entity.dto.MerchantBasicInformationRegistrationDTO;
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
            this.setSystemOrderTrack(request,reqParam,sotTable,bussType);

        }catch (Exception  e){
            if(e instanceof PayException){

            }else{
                e.printStackTrace();
            }
        }finally {

            return  null;
        }

    }

    public static void main(String[] args) {

        SystemOrderTrackTable  sotTable = new SystemOrderTrackTable();
        Field[] fields = sotTable.getClass().getDeclaredFields();
        for(int i=0; i<fields.length;i++){
            String fieldName = fields[i].getName();
            System.out.println(fieldName);
        }

    }


}
