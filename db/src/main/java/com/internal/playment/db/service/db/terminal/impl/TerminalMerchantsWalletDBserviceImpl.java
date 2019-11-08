package com.internal.playment.db.service.db.terminal.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.db.mapper.terminal.AnewTerminalMerchantsWalletMapper;
import com.internal.playment.db.service.db.terminal.TerminalMerchantsWalletDBservice;
import org.springframework.stereotype.Service;

@Service
public class TerminalMerchantsWalletDBserviceImpl extends ServiceImpl<AnewTerminalMerchantsWalletMapper, TerminalMerchantsWalletTable> implements TerminalMerchantsWalletDBservice {
}
