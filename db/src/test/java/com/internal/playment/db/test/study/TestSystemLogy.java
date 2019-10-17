package com.internal.playment.db.test.study;

import com.internal.playment.api.entity.table.system.SystemLogTable;
import com.internal.playment.db.service.system.SystemLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午7:37
 * Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSystemLogy {

    @Autowired
    private SystemLogService systemLogService;

    @Test
    public  void  test(){
        List<SystemLogTable>  list = systemLogService.list();
        list.forEach(System.out::println);
    }
}
