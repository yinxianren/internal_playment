package com.internal.playment.common.page;

import lombok.Data;

@Data
public class GlobalConfiguration {
    private String fileUploadPath;
    private boolean isProduction;
    private String paymentUrl;
    private String md5Key;
}
