package com.internal.playment.pay.service.shortcut;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerTransOrderApplyDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.pay.service.CommonServiceInterface;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/5
 * Time: 下午7:28
 * Description:
 */
public interface NewTransOrderService extends CommonServiceInterface {
    /**
     *
     * @return
     */
    Map<String, ParamRule> getParamMapByB11();

    /**
     *
     * @param merOrderId
     * @param ipo
     */
    void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merTransOrderApplyDTO
     * @param ipo
     * @return
     */
    List<PayOrderInfoTable> getPayOrderInfoByList(MerTransOrderApplyDTO merTransOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param payOrderInfoTableList
     * @param merTransOrderApplyDTO
     * @param ipo
     * @return
     */
    BigDecimal verifyOrderAmount(List<PayOrderInfoTable> payOrderInfoTableList, MerTransOrderApplyDTO merTransOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param payOrderInfoTableList
     * @param ipo
     * @return
     */
    Map<OrganizationInfoTable, List<PayOrderInfoTable>> groupPayOrderByOrganization(List<PayOrderInfoTable> payOrderInfoTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param payOrderInfoTableList
     * @param ipo
     * @return
     * @throws NewPayException
     */
    Tuple2<OrganizationInfoTable, ChannelInfoTable> verifyOrderChannelTab(List<PayOrderInfoTable> payOrderInfoTableList, MerTransOrderApplyDTO merTransOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merTransOrderApplyDTO
     * @param channelInfoTable
     * @param ipo
     * @return
     */
    TransOrderInfoTable saveOrder(MerTransOrderApplyDTO merTransOrderApplyDTO, ChannelInfoTable channelInfoTable, MerchantInfoTable merInfoTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param transOrderInfoTable
     * @param crossResponseMsg
     * @param crossResponseMsgDTO
     * @param ipo
     * @return
     */
    TransOrderInfoTable updateOrder(TransOrderInfoTable transOrderInfoTable, String crossResponseMsg, CrossResponseMsgDTO crossResponseMsgDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param payOrderInfoTableList
     * @return
     */
    List<PayOrderInfoTable> updatePayOrderStatus(List<PayOrderInfoTable> payOrderInfoTableList);

    /**
     *
     * @param transOrderInfoTable
     * @param payOrderInfoTableList
     * @param ipo
     */
    void batchUpdateTransOderCorrelationInfo(TransOrderInfoTable transOrderInfoTable, List<PayOrderInfoTable> payOrderInfoTableList, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merTransOrderApplyDTO
     * @param ipo
     */
    void checkProductTypeByB11(MerTransOrderApplyDTO merTransOrderApplyDTO, InnerPrintLogObject ipo) throws NewPayException;
}
