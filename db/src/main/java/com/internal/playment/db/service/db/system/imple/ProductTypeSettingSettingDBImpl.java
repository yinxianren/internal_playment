package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.ProductSettingTable;
import com.internal.playment.db.mapper.system.AnewProductTypeSettingMapper;
import com.internal.playment.db.service.db.system.ProductTypeSettingDBService;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeSettingSettingDBImpl extends ServiceImpl<AnewProductTypeSettingMapper, ProductSettingTable> implements ProductTypeSettingDBService {
}
