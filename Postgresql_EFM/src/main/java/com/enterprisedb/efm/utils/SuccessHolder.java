package com.enterprisedb.efm.utils;

public class SuccessHolder {
    private volatile boolean success = false;

    public boolean isSuccess() {
        return this.success;
    }

    public void success() {
        this.success = true;
    }
}
