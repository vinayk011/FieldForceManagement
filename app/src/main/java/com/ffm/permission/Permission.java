package com.ffm.permission;

import android.Manifest;



public enum Permission {

    CAMERA(Manifest.permission.CAMERA),
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    WRITE_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    CALL_PHONE(Manifest.permission.CALL_PHONE);

    String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public static Permission stringToPermission(String stringPermission) {
        for (Permission permission : Permission.values()) {
            if (stringPermission.equalsIgnoreCase(permission.toString()))
                return permission;
        }

        return null;
    }

    @Override
    public String toString() {
        return permission;
    }
}