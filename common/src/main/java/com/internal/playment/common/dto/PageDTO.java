package com.internal.playment.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PageDTO implements Serializable {

    private Integer pageNum = 1;
    private Integer paegSize = 10;
    private String beginTime;
    private String endTime;

    public PageDTO (Integer pageNum,Integer paegSize){
        this.pageNum = pageNum;
        this.paegSize = paegSize;
    }

}
