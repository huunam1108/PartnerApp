package com.example.utils;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by framgia on 25/11/2016.
 */

public class PkgCertWhiteLists {
    private Map<String, String> mWhiteLists = new HashMap<>();

    public boolean add(String pkgName, String sha256) {
        if (pkgName == null) return false;
        if (sha256 == null) return false;
        sha256 = sha256.replaceAll(" ", "");
        if (sha256.length() != 64) {
            return false;
        }
        // SHA-256 -> 32 bytes -> 64 chars
        sha256 = sha256.toUpperCase();
        if (sha256.replaceAll("[0-9A-F]+", "").length() != 0) {
            // found non hex char
            return false;
        }
        mWhiteLists.put(pkgName, sha256);
        return true;
    }

    public boolean test(Context ctx, String pkgName) {
        // Get the correct hash value which corresponds to pkgName.
        String correctHash = mWhiteLists.get(pkgName);
        // Compare the actual hash value of pkgName with the correct hash value.
        return PkgCert.test(ctx, pkgName, correctHash);
    }
}
