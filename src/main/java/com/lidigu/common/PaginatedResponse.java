package com.lidigu.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PaginatedResponse<T> {
    private String status;
    private List<T> data;

    @JsonProperty("total_products")
    private long totalProducts;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("current_page")
    private int currentPage;

    public PaginatedResponse(String status, List<T> data, long totalProducts, int totalPages, int currentPage) {
        this.status = status;
        this.data = data;
        this.totalProducts = totalProducts;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }

    public static <T> PaginatedResponse<T> success(List<T> data, long totalProducts, int totalPages, int currentPage) {
        return new PaginatedResponse<>("success", data, totalProducts, totalPages, currentPage);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
