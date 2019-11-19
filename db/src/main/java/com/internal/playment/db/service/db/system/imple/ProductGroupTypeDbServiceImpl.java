package com.internal.playment.db.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.internal.playment.common.table.system.ProductGroupTypeTable;
import com.internal.playment.db.mapper.system.AnewProductGroupTypeMapper;
import com.internal.playment.db.service.db.system.ProductGroupTypeDbService;
import org.springframework.stereotype.Service;

@Service
public class ProductGroupTypeDbServiceImpl extends ServiceImpl<AnewProductGroupTypeMapper, ProductGroupTypeTable>
        implements ProductGroupTypeDbService {


}
