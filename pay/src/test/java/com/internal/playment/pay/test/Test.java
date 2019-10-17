package com.internal.playment.pay.test;

import com.internal.playment.api.entity.dto.MerchantBasicInformationRegistrationDTO;
import com.internal.playment.api.entity.enums.ResponseCodeEnum;
import com.internal.playment.api.entity.table.system.SystemOrderTrackTable;
import com.internal.playment.api.tools.PayAssert;
import com.internal.playment.api.tools.PayUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 上午11:44
 * Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test  implements PayAssert, PayUtils {

    @org.junit.Test
    public void test_01() throws Exception{
        Field[] fields = MerchantBasicInformationRegistrationDTO.class.getDeclaredFields();
        for(int i=0; i<fields.length;i++){
            String fieldName = fields[i].getName();
            System.out.println(fieldName);
        }
    }


    public static void main(String[] args) {
        Field[] fields = MerchantBasicInformationRegistrationDTO.class.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<fields.length;i++){
            String fieldName = fields[i].getName();
            sb.append(String.format(",\"%s\"",fieldName));
        }
        System.out.println(sb.toString());
    }
}
