package com.internal.playment.inward.service.channel;

import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.inner.PayUtil;
import com.internal.playment.common.tuple.Tuple2;

public interface ChannelCommonServiceInterface extends PayUtil, NewPayAssert {

    RequestCrossMsgDTO packageRequestCrossMsgDTO(Tuple2 tuple);

}
