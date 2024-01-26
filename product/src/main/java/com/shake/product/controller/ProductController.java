package com.shake.product.controller;

import com.shake.product.model.entity.ProductEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Shake
 * @Description ProductController
 * @Date 2023/11/23
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/item")
    public ResponseEntity<ProductEntity> addProduct(){


        return null;
    }
}
