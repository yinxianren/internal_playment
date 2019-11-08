package com.internal.playment.api.db.terminal;


import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;

import java.util.List;

public interface ApiTerminalMerchantsWalletService {

    TerminalMerchantsWalletTable getOne(TerminalMerchantsWalletTable tmw);

    boolean updateOrSave(TerminalMerchantsWalletTable tmw);

    List<TerminalMerchantsWalletTable> getList(TerminalMerchantsWalletTable terminalMerchantsWalletTable);

}
