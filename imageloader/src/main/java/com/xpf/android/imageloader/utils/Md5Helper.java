package com.xpf.android.imageloader.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xpf on 2017/10/22 :)
 * Function:MD5 辅助类,对字符串取 MD5
 */
public class Md5Helper {

    /**
     * 使用 MD5 算法对传入的 key 进行加密并返回
     */
    private static MessageDigest mDigest = null;

    static {
        try {
            mDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对 key 进行 MD5 加密，如果无 MD5 加密算法，则直接使用 key 对应的 hash 值
     *
     * @param key
     * @return
     */
    public static String toMD5(String key) {
        String cacheKey;
        // 获取 MD5 算法失败时，直接使用 key 对应的 hash 值
        if (mDigest == null) {
            return String.valueOf(key.hashCode());
        }
        mDigest.update(key.getBytes());
        cacheKey = bytesToHexString(mDigest.digest());
        return cacheKey;
    }

    /**
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }

        return sb.toString();
    }
}
