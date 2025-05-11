package com.certicrypt.certicrypt.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {
    // Sinh cặp khóa
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // Độ dài khóa
        return keyGen.generateKeyPair();
    }

    // Ký một thông điệp
    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes("UTF-8"));
        byte[] signatureBytes = sign.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    // Xác minh chữ ký
    public static boolean verify(String message, String base64Signature, PublicKey publicKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(publicKey);
        sign.update(message.getBytes("UTF-8"));
        byte[] signatureBytes = Base64.getDecoder().decode(base64Signature);
        return sign.verify(signatureBytes);
    }

    // Giải mã PublicKey từ chuỗi Base64
    public static PublicKey decodePublicKey(String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}
