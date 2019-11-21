package com.internal.playment.common.page;


import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/2
 * Time: 9:34
 * Project: Management
 * Package: com.rxh.pojo
 */
@Data
public class Page implements Serializable {
    private int pageNum;
    private int pageSize;
    private Map<String, String> orderBy;
    private SearchInfo searchInfo;
    private Object object;
}
