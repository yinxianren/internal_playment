package com.internal.playment.api.db.terminal;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;

import java.util.List;

public interface ApiTerminalMerchantsDetailsService {

    boolean save(TerminalMerchantsDetailsTable tmd);

    IPage page(TerminalMerchantsDetailsTable terminalMerchantsDetailsTable);

    List<TerminalMerchantsDetailsTable> listGroupByTerminalId();

}
