package com.shake.product.mapper;

import com.shake.product.model.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: productMapper
 * @Description
 * @Date 2023/10/26
 */
@Mapper
public interface productMapper {

    /**
     * 依照產品名稱查找產品
     * @param name
     * @return
     */
    ProductEntity selectProductByName(@Param("name") String name);

    Integer insertProduct();

}
