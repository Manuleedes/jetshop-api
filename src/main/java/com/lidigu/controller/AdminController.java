package com.lidigu.controller;

import com.lidigu.entity.Product;
import com.lidigu.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/products")
    public List<Product> listProducts() {
        return adminService.listProducts();
    }

    @PostMapping("/add_product")
    public Map<String, Object> addProduct(@RequestParam String product_name,
            @RequestParam String product_description,
            @RequestParam BigDecimal product_price,
            @RequestParam(required = false) BigDecimal product_discount_price,
            @RequestParam Integer product_stock_quantity,
            @RequestParam Integer product_category_id,
            @RequestParam Integer product_brand_id,
            @RequestParam(required = false) BigDecimal product_weight,
            @RequestParam(required = false) String product_dimensions,
            @RequestParam(required = false) String product_color,
            @RequestParam(required = false) String product_size,
            @RequestParam(required = false) Double product_rating,
            @RequestParam(required = false) Integer product_total_reviews,
            @RequestParam(value = "product_image_url", required = false) MultipartFile image,
            @RequestParam(value = "product_thumbnail_url", required = false) MultipartFile thumbnail) {
        return adminService.addProduct(product_name, product_description, product_price, product_discount_price,
                product_stock_quantity, product_category_id, product_brand_id, product_weight, product_dimensions,
                product_color, product_size, product_rating, product_total_reviews, image, thumbnail);
    }

    @GetMapping("/export_csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        adminService.exportCsv(response);
    }

    @PostMapping("/import_csv")
    public Map<String, Object> importCsv(@RequestParam("csv_file") MultipartFile file) {
        return adminService.importCsv(file);
    }
}
