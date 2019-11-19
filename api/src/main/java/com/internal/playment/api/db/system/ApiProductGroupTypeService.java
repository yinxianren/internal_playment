package com.internal.playment.api.db.system;

import com.internal.playment.common.table.system.ProductGroupTypeTable;

import java.util.List;

public interface ApiProductGroupTypeService {

    boolean save(ProductGroupTypeTable pgt);

    boolean update(ProductGroupTypeTable pgt);

    boolean updateById(ProductGroupTypeTable pgt);

    ProductGroupTypeTable getOne(ProductGroupTypeTable pgt);

    List<ProductGroupTypeTable> getList(ProductGroupTypeTable pgt);
}
