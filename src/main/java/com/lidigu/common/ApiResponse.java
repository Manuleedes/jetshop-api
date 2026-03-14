package com.lidigu.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private String message;
    private String flag;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String status, String message, String flag, T data) {
        this.status = status;
        this.message = message;
        this.flag = flag;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", "Success", "1", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, "1", data);
    }

    public static ApiResponse<Void> error(String message) {
        return new ApiResponse<>("error", message, "0", null);
    }

    // Traditional PHP style response for specific compatibility
    public static Map<String, Object> phpSuccess(String key, Object value) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Success");
        response.put("flag", "1");
        response.put(key, value);
        return response;
    }

    public static Map<String, Object> phpError(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("flag", "0");
        return response;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
