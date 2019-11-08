package com.internal.playment.db.service.db.terminal.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;
import com.internal.playment.db.mapper.terminal.AnewTerminalMerchantDetailsMapper;
import com.internal.playment.db.service.db.terminal.TerminalMerchantsDetailsDBService;
import org.springframework.stereotype.Service;

@Service
public class TerminalMerchantsDetailsDBServiceImpl extends ServiceImpl<AnewTerminalMerchantDetailsMapper, TerminalMerchantsDetailsTable> implements TerminalMerchantsDetailsDBService {
}
