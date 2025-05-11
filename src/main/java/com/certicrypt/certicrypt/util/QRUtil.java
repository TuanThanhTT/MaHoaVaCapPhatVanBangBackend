package com.certicrypt.certicrypt.util;

import com.certicrypt.certicrypt.DTO.response.VerifyDegree;
import com.certicrypt.certicrypt.models.RSAKeys;
import com.certicrypt.certicrypt.repository.RSAKeysRepository;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


public class QRUtil {



    public static BufferedImage generateQRCodeImageWithLogo(String data, int width, int height, BufferedImage logoImage) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2); // Quiet zone
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // Khôi phục lỗi cao (~30%)

        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        // Giảm kích thước logo còn 1/8 (nếu vẫn lỗi thì thử 1/10)
        int logoSize = Math.min(width, height) / 8;

        Image tmp = logoImage.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);
        BufferedImage resizedLogo = new BufferedImage(logoSize, logoSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedLogo.createGraphics();
        g.drawImage(tmp, 0, 0, null);
        g.dispose();

        // KHÔNG dùng khung tròn để tránh che nhiều hơn
        int x = (width - logoSize) / 2;
        int y = (height - logoSize) / 2;

        Graphics2D g2 = qrImage.createGraphics();
        g2.setComposite(AlphaComposite.SrcOver);
        g2.drawImage(resizedLogo, x, y, null);
        g2.dispose();

        return qrImage;
    }

    public static VerifyDegree verifyDegreeImage(File inputImageFile, RSAKeysRepository raKeysRepository) {
        try {
            // Quét QR code từ ảnh để lấy keyId
            BufferedImage image = ImageIO.read(inputImageFile);
            int keyId = extractKeyIdFromQRCode(image);  // Lấy keyId từ QR code
            System.out.println("rsakey id la: " + keyId);
            if (keyId == -1) {

                return new VerifyDegree(null,"","Không hợp lệ: Không tìm thấy keyId trong QR code");
            }

            // Lấy RSAKey từ cơ sở dữ liệu theo keyId
            RSAKeys rsaKeys = raKeysRepository.findById(keyId).orElse(null);
            if (rsaKeys == null) {
                return new VerifyDegree(null, "", "Không hợp lệ: Không tìm thấy RSAKey trong cơ sở dữ liệu");
            }

            // Lấy khóa công khai từ RSAKey
            String publicKeyStr = rsaKeys.getRsaPublicKey();
            System.out.println("khóa công khai la: " + publicKeyStr);
            byte[] publicBytes = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Lấy chữ ký từ RSAKey
            String signature = rsaKeys.getRsaSignature();

            // Giải mã thông điệp từ ảnh văn bằng bằng LSB
            String message = extractMessageFromImage(inputImageFile);  // Giải mã thông điệp từ ảnh
            System.out.println("thông điệp là: " + message);
            if (message == null || message.isEmpty()) {
                return new VerifyDegree(null,"", "Không hợp lệ: Không thể giải mã thông điệp từ ảnh");
            }

            // Kiểm tra chữ ký với khóa công khai
            boolean isValid = RSAUtil.verify(message, signature, publicKey);
            if (isValid) {
                return new VerifyDegree(keyId, message,  "Thông điệp hợp lệ");
            } else {
                return new VerifyDegree(null,"", "Không hợp lệ: Chữ ký sai");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new VerifyDegree(null,"", "Lỗi xử lý: " + e.getMessage());
        }
    }


    public static int extractKeyIdFromQRCode(BufferedImage image) {
        try {
            // Tạo đối tượng ZXing để đọc QR code
            MultiFormatReader reader = new MultiFormatReader();

            // Thiết lập các tùy chọn giải mã
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            reader.setHints(hints);

            // Chuyển đổi BufferedImage thành LuminanceSource để ZXing có thể đọc
            BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
            HybridBinarizer binarizer = new HybridBinarizer(source);

            // Đọc và giải mã QR code
            Result result = reader.decode(new com.google.zxing.BinaryBitmap(binarizer));

            // Lấy dữ liệu giải mã từ QR code
            String qrData = result.getText();

            // Giải mã JSON từ QR code để lấy keyId
            JSONObject json = new JSONObject(qrData);
            if (json.has("keyId")) {
                return json.getInt("keyId");  // Giả sử dữ liệu QR code có dạng {"keyId": <id>}
            } else {
                return -1;  // Không tìm thấy keyId trong dữ liệu QR code
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            return -1;  // Trả về -1 nếu không tìm thấy mã QR hoặc có lỗi trong giải mã
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String extractMessageFromImage(File inputImageFile) throws Exception {
        BufferedImage image = ImageIO.read(inputImageFile);
        int width = image.getWidth();
        int height = image.getHeight();

        StringBuilder binaryBuilder = new StringBuilder();

        outerLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int blue = rgb & 0xFF;
                int lsb = blue & 1;
                binaryBuilder.append(lsb);
            }
        }

        String binaryMessage = binaryBuilder.toString();

        // Lấy độ dài thông điệp (32 bit đầu tiên)
        if (binaryMessage.length() < 32) {
            throw new IllegalStateException("Không đủ dữ liệu để đọc độ dài thông điệp (cần ít nhất 32 bit).");
        }

        String lengthBinary = binaryMessage.substring(0, 32);
        int messageLength = Integer.parseInt(lengthBinary, 2); // độ dài tính theo byte
        int totalMessageBits = messageLength * 8;

        if (binaryMessage.length() < 32 + totalMessageBits) {
            throw new IllegalStateException("Không đủ dữ liệu để đọc thông điệp. Đã đọc: "
                    + (binaryMessage.length() - 32) + " bit, cần: " + totalMessageBits + " bit.");
        }

        // Trích xuất thông điệp từ binary
        String messageBinary = binaryMessage.substring(32, 32 + totalMessageBits);

        byte[] messageBytes = new byte[messageLength];
        for (int i = 0; i < messageLength; i++) {
            String byteStr = messageBinary.substring(i * 8, (i + 1) * 8);
            if (byteStr.length() < 8) {
                throw new IllegalStateException("Byte thứ " + i + " không đủ 8 bit: '" + byteStr + "'");
            }
            try {
                messageBytes[i] = (byte) Integer.parseInt(byteStr, 2);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Không thể chuyển đổi byte " + i + ": '" + byteStr + "'", e);
            }
        }

        return new String(messageBytes, StandardCharsets.UTF_8);
    }


    public static boolean verify(String data, String signatureBase64, PublicKey publicKey) {
        try {
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(publicKey);
            publicSignature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
            return publicSignature.verify(signatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
