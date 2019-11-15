package com.internal.playment.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PageDTO implements Serializable {

    private Integer pageNum;
    private Integer paegSize;
    private String beginTime;
    private String endTime;

    private PageDTO (Integer pageNum,Integer paegSize){
        this.pageNum = pageNum;
        this.paegSize = paegSize;
    }

}
