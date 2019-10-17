package com.internal.playment.db.impl.assist;

import com.alibaba.dubbo.config.annotation.Service;
import com.internal.playment.api.db.assist.AssistSystemLogService;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午6:54
 * Description:
 */

@Service(version = "${application.version}")
public class AssistSystemLogServiceImpl implements AssistSystemLogService {


    @Override
    public String test() {
        return "AssistSystemLogServiceImpl";
    }
}
