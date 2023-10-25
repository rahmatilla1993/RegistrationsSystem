package com.example.accountingsystem.payload;

import lombok.Getter;

@Getter
public class ApiResponse {
    private Object data;
    private boolean success;

    public ApiResponse(Object data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
