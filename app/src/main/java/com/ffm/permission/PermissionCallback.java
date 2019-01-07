package com.ffm.permission;


public interface PermissionCallback {
    void onPermissionResult(boolean granted, boolean neverAsk);
}