package com.internal.playment.api.db.system;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.internal.playment.common.table.system.SystemOrderTrackTable;

public interface ApiSystemOrderTrackService {

    boolean save(SystemOrderTrackTable sot);

    IPage page(SystemOrderTrackTable systemOrderTrackTable, Page page);

}
