package com.internal.playment.pay.service.shortcut;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerBondCardApplyDTO;
import com.internal.playment.common.dto.MerConfirmBondCardDTO;
import com.internal.playment.common.dto.MerReGetBondCodeDTO;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.pay.service.CommonServiceInterface;

import java.util.Map;

public interface NewBondCardService extends CommonServiceInterface {
    /**
     *  绑卡申请接口
     * @return
     */
    Map<String, ParamRule> getParamMapByB4();

    /**
     * 重新获取绑卡验证码
     * @return
     */
    Map<String, ParamRule> getParamMapByB5();

    /**
     * 确认短信
     * @return
     */
    Map<String, ParamRule> getParamMapByB6();
    /**
     *  判断订单是否重复
     * @param merOrderId
     * @param ipo
     */
    void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException;


    /**
     *  保存绑卡申请记录
     * @param mbcaDTO
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB4(MerBondCardApplyDTO mbcaDTO,RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *
     * @param merchantCardTable
     * @param mrgbcDTO
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB5(MerchantCardTable merchantCardTable, MerReGetBondCodeDTO mrgbcDTO, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merchantCardTable
     * @param mcbcDTO
     * @param ipo
     * @return
     */
    MerchantCardTable saveCardInfoByB6(MerchantCardTable merchantCardTable, MerConfirmBondCardDTO mcbcDTO, InnerPrintLogObject ipo) throws NewPayException;
    /**
     *  更新绑卡申请记录
     * @param crossResponseMsgDTO
     * @param crossResponseMsg
     * @param merchantCardTable
     * @param ipo
     */
    void updateByBondCardInfo(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException;

    /**
     * 根据平台流水号获取进件成功的附属表
     * @param platformOrderId
     * @param ipo
     * @return
     */
    RegisterCollectTable getRegisterInfoTableByPlatformOrderId(String platformOrderId, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param crossResponseMsgDTO
     * @param crossResponseMsg
     * @param merchantCardTable
     * @param merchantCardTable_old
     * @param ipo
     */
    void updateByBondCardInfoByB6(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, MerchantCardTable merchantCardTable_old, InnerPrintLogObject ipo) throws NewPayException;

    /**
     *
     * @param merchantCardTable
     */
    void checkSuccessBondCardInfo(MerchantCardTable merchantCardTable,InnerPrintLogObject ipo) throws NewPayException;
}
