package com.lidigu.controller;

import com.lidigu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("all_product_list.php")
    public Object getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return productService.getAllProducts(page, limit);
    }

    @PostMapping("get_productDetails.php")
    public Object getProductDetails(@RequestParam String product_id,
            @RequestParam(required = false) String user_id) {
        return productService.getProductDetails(product_id, user_id);
    }

    @GetMapping("list_products_by_brand.php")
    public Object listProductsByBrand(@RequestParam Integer brand_id) {
        return productService.listProductsByBrand(brand_id);
    }

    @GetMapping("list_products_by_category.php")
    public Object listProductsByCategory(@RequestParam Integer category_id) {
        return productService.listProductsByCategory(category_id);
    }
}
