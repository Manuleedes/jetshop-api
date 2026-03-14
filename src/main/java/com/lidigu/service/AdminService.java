package com.lidigu.service;

import com.lidigu.entity.Product;
import com.lidigu.repository.ProductRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private ProductRepository productRepository;

    private final String UPLOAD_DIR = "uploads/";

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Map<String, Object> addProduct(String product_name, String product_description, BigDecimal product_price,
            BigDecimal product_discount_price, Integer product_stock_quantity,
            Integer product_category_id, Integer product_brand_id, BigDecimal product_weight,
            String product_dimensions, String product_color, String product_size,
            Double product_rating, Integer product_total_reviews,
            MultipartFile image, MultipartFile thumbnail) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = new Product();
            product.setProductId("PROD" + System.currentTimeMillis());
            product.setProductName(product_name);
            product.setProductDescription(product_description);
            product.setProductPrice(product_price);
            product.setProductDiscountPrice(product_discount_price);
            product.setProductStockQuantity(product_stock_quantity);
            product.setProductCategoryId(product_category_id);
            product.setProductBrandId(product_brand_id);
            product.setProductWeight(product_weight);
            product.setProductDimensions(product_dimensions);
            product.setProductColor(product_color);
            product.setProductSize(product_size);
            product.setProductRating(product_rating != null ? BigDecimal.valueOf(product_rating) : BigDecimal.ZERO);
            product.setProductTotalReviews(product_total_reviews != null ? product_total_reviews : 0);
            product.setProductIsActive(true);
            product.setProductCreatedAt(LocalDateTime.now());
            product.setProductUpdatedAt(LocalDateTime.now());

            if (image != null && !image.isEmpty()) {
                product.setProductImageUrl(saveFile(image));
            }
            if (thumbnail != null && !thumbnail.isEmpty()) {
                product.setProductThumbnailUrl(saveFile(thumbnail));
            }

            productRepository.save(product);
            response.put("status", "success");
            response.put("flag", "product_added");
            response.put("message", "Product added successfully");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("flag", "error");
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }

    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=products_export.csv");

        List<Product> products = productRepository.findAll();
        PrintWriter writer = response.getWriter();
        writer.println("product_id,product_name,product_price,stock_quantity");

        for (Product p : products) {
            writer.println(String.format("%s,%s,%s,%d",
                    p.getProductId(), p.getProductName(), p.getProductPrice(), p.getProductStockQuantity()));
        }
    }

    public Map<String, Object> importCsv(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int rowCount = 0;
            while ((line = reader.readLine()) != null) {
                if (rowCount == 0) {
                    rowCount++;
                    continue;
                }
                String[] column = line.split(",");
                if (column.length >= 17) {
                    Product product = new Product();
                    product.setProductId("PROD" + System.currentTimeMillis() + "_" + rowCount);
                    product.setProductName(column[0]);
                    product.setProductDescription(column[1]);
                    product.setProductPrice(new BigDecimal(column[2]));
                    product.setProductDiscountPrice(new BigDecimal(column[3]));
                    product.setProductStockQuantity(Integer.parseInt(column[4]));
                    product.setProductCategoryId(Integer.parseInt(column[5]));
                    product.setProductBrandId(Integer.parseInt(column[6]));
                    product.setProductWeight(new BigDecimal(column[7]));
                    product.setProductDimensions(column[8]);
                    product.setProductColor(column[9]);
                    product.setProductSize(column[10]);
                    product.setProductRating(new BigDecimal(column[11]));
                    product.setProductTotalReviews(Integer.parseInt(column[12]));
                    product.setProductImageUrl(column[13]);
                    product.setProductThumbnailUrl(column[14]);
                    product.setProductIsFeatured(Integer.parseInt(column[15]) == 1);
                    product.setProductIsActive(Integer.parseInt(column[16]) == 1);
                    product.setProductCreatedAt(LocalDateTime.now());
                    product.setProductUpdatedAt(LocalDateTime.now());
                    productRepository.save(product);
                }
                rowCount++;
            }
            response.put("status", "success");
            response.put("flag", "csv_imported");
            response.put("message", (rowCount - 1) + " products imported successfully!");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("flag", "error");
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return UPLOAD_DIR + fileName;
    }
}
