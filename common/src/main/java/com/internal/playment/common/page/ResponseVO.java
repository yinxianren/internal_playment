package com.internal.playment.common.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class ResponseVO implements Serializable {
    private Integer code;
    private String message;
    private Object Data;
    private PageInfo pageinfo;
    private Object customData;

    public ResponseVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
