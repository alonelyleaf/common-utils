package com.alonelyleaf.util;


import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {

    private static final String ALOGRITHM_AES = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";


    // ---------------------------- base64 -------------------------------
    public static byte[] encodeBase64(final byte[] data) {

        return new Base64().encode(data);
    }

    public static byte[] encodeBase64(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return encodeBase64(data.getBytes(Charsets.UTF_8));
    }

    public static String encodeBase64ToString(final byte[] data) {

        return new Base64().encodeToString(data);
    }

    public static String encodeBase64ToString(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return encodeBase64ToString(data.getBytes(Charsets.UTF_8));
    }

    public static byte[] decodeBase64(final byte[] data) {

        return new Base64().decode(data);
    }

    public static byte[] decodeBase64(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return decodeBase64(data.getBytes(Charsets.UTF_8));
    }

    public static String decodeBase64ToString(final byte[] data) {

        return new String(decodeBase64(data), Charsets.UTF_8);
    }

    public static String decodeBase64ToString(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return decodeBase64ToString(data.getBytes(Charsets.UTF_8));
    }

    // ============================== 摘要加密 ==============================

    // ---------------------------- sha1 -------------------------------
    public static byte[] sha1(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.sha1(data);
    }

    public static byte[] sha1(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.sha1(data);
    }

    public static String sha1Hex(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.sha1Hex(data);
    }

    public static String sha1Hex(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.sha1Hex(data);
    }

    // ---------------------------- sha256 -------------------------------
    public static byte[] sha256(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.sha256(data);
    }

    public static byte[] sha256(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.sha256(data);
    }

    public static String sha256Hex(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.sha256Hex(data);
    }

    public static String sha256Hex(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.sha256Hex(data);
    }

    // ---------------------------- sha512 -------------------------------
    public static byte[] sha512(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.sha512(data);
    }

    public static byte[] sha512(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.sha512(data);
    }

    public static String sha512Hex(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.sha512Hex(data);
    }

    public static String sha512Hex(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.sha512Hex(data);
    }

    // ---------------------------- md5 -------------------------------
    public static byte[] md5(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.md5(data);
    }

    public static byte[] md5(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.md5(data);
    }

    public static String md5Hex(final String data) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        return DigestUtils.md5Hex(data);
    }

    public static String md5Hex(final byte[] data) {

        if (data == null || data.length == 0) {
            return null;
        }

        return DigestUtils.md5Hex(data);
    }

    // ============================== 对称加密 ==============================

    /**
     * AES加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后以base64编码
     */
    public static String encryptAes(String data, String key) {

        if (data == null || data.isEmpty() || key == null || key.isEmpty()) {
            return data;
        }

        byte[] raw = key.getBytes(Charsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALOGRITHM_AES);
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());

            return new Base64().encodeToString(encrypted);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {

            return data;
        }
    }

    /**
     * AES解密
     *
     * @param data 待解密数据，AES加密后base64编码的数据
     * @param key  密钥
     * @return
     */
    public static String decryptAes(String data, String key) {

        if (data == null || data.isEmpty() || key == null || key.isEmpty()) {
            return data;
        }

        byte[] raw = key.getBytes(Charsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALOGRITHM_AES);
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return new String(cipher.doFinal(new Base64().decode(data)), Charsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException e) {
            return data;
        }
    }
}
