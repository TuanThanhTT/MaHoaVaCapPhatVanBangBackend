package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.DTO.request.InfoDegreeRequest;

import com.certicrypt.certicrypt.DTO.response.MessageCheckDegree;
import com.certicrypt.certicrypt.DTO.response.VerifyDegree;
import com.certicrypt.certicrypt.models.Degree;

import com.certicrypt.certicrypt.models.DegreeImg;
import com.certicrypt.certicrypt.models.RSAKeys;
import com.certicrypt.certicrypt.models.User;
import com.certicrypt.certicrypt.repository.DegreeImgRepository;
import com.certicrypt.certicrypt.repository.DegreeRepository;

import com.certicrypt.certicrypt.repository.RSAKeysRepository;

import com.certicrypt.certicrypt.repository.UserRepository;
import com.certicrypt.certicrypt.service.DegreeQuerylogService;
import com.certicrypt.certicrypt.service.FileExportService;
import com.certicrypt.certicrypt.service.UserService;
import com.certicrypt.certicrypt.util.QRUtil;
import com.certicrypt.certicrypt.util.RSAUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class FileExportServiceImpl implements FileExportService {

    private final DegreeImgRepository degreeImgRepository;
    private final DegreeRepository degreeRepository;
    private final UserService userService;
    private final RSAKeysRepository raKeysRepository;
    private final DegreeQuerylogService degreeQuerylogService;
    private static final Logger logger = LoggerFactory.getLogger(FileExportServiceImpl.class);



    public FileExportServiceImpl(DegreeImgRepository degreeImgRepository, UserService userService, DegreeRepository degreeRepository, RSAKeysRepository raKeysRepository, DegreeQuerylogService degreeQuerylogService) {
        this.degreeImgRepository = degreeImgRepository;
        this.degreeRepository = degreeRepository;
        this.raKeysRepository = raKeysRepository;
        this.degreeQuerylogService = degreeQuerylogService;
        this.userService = userService;

    }


    private void drawText(Graphics2D g2d, String text, int startX, int y, int width, int height, Font font, Color color) {
        g2d.setFont(font);
        g2d.setColor(color);

        // Tăng giá trị y lên thêm 5px để điều chỉnh vị trí
        y += 15;  // Vẽ xuống dưới thêm 5px nữa

        // Tính chiều rộng của văn bản
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        // Tính vị trí x để căn giữa văn bản trong khung
        int x = startX + (width - textWidth) / 2;

        // Vẽ văn bản đã căn giữa
        g2d.drawString(text, x, y);
    }
    public  String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }



    public String createDegreeImg(InfoDegreeRequest data) throws IOException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String currentDateTime = sdf.format(new Date());
//        String fileName = "degree_" + currentDateTime + ".png";
//        String outputFilePath = "Upload/degreeimg/" + fileName;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS"); // Thêm mili giây
        String currentDateTime = sdf.format(new Date());

        // Tạo một mã ngẫu nhiên rút gọn từ UUID (hoặc có thể dùng Random nếu muốn)
        String randomCode = UUID.randomUUID().toString().substring(0, 6); // 6 ký tự là đủ tránh trùng

        String fileName = "degree_" + currentDateTime + "_" + randomCode + ".png";
        String outputFilePath = "Upload/degreeimg/" + fileName;

        try {
            // Load template và font
            BufferedImage baseImage = ImageIO.read(new File("Upload/baseimg/MauVanBang.png"));
            BufferedImage logoImage = ImageIO.read(new File("Upload/baseimg/logofinish.png"));

            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Windows\\Fonts\\times.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);

            Font font20 = baseFont.deriveFont(Font.BOLD, 20f);
            Font font19 = baseFont.deriveFont(Font.BOLD, 19f);
            Font font13 = baseFont.deriveFont(Font.PLAIN, 13f);
            Font font10 = baseFont.deriveFont(Font.PLAIN, 10f);

            Graphics2D g2d = baseImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // ========== VẼ CÁC THÔNG TIN TRÊN VĂN BẰNG ==========
            g2d.setFont(font20);
            drawText(g2d, "THE DEGREE OF " + (data.getDegreeClassification().equalsIgnoreCase("kỹ sư") ? "ENGINEER" : "BACHELOR"), 33, 96, 322, 22, font20, new Color(0xD4, 0x0A, 0x0A));
            drawText(g2d, "BẰNG " + (data.getDegreeClassification().equalsIgnoreCase("kỹ sư") ? "KỸ SƯ" : "CỬ NHÂN"), 408, 93, 327, 25, font20, new Color(0xD4, 0x0A, 0x0A));

            g2d.setFont(font19);
            drawText(g2d, data.getMajorNameEng(), 31, 150, 327, 20, font19, Color.BLACK);
            drawText(g2d, data.getMajor(), 409, 152, 324, 27, font19, Color.BLACK);

            g2d.setFont(font13);
            drawText(g2d, removeDiacritics(data.getFullName()), 93, 239, 261, 17, font13, Color.BLACK);
            drawText(g2d, data.getFullName(), 463, 239, 266, 15, font13, Color.BLACK);

            drawText(g2d, data.getBirthDate(), 117, 263, 207, 14, font13, Color.BLACK);
            drawText(g2d, data.getBirthDate(), 488, 262, 219, 15, font13, Color.BLACK);

            String degreeTypeEng = switch (data.getDegreeType().toLowerCase()) {
                case "xuất sắc" -> "Excellent";
                case "giỏi" -> "Very Good";
                case "khá" -> "Good";
                case "trung bình" -> "Average";
                default -> "N/A";
            };
            drawText(g2d, degreeTypeEng, 154, 287, 143, 15, font13, Color.BLACK);
            drawText(g2d, data.getDegreeType(), 513, 287, 173, 18, font13, Color.BLACK);

            String year = String.valueOf(data.getYearGraduation());
            drawText(g2d, year, 151, 308, 148, 16, font13, Color.BLACK);
            drawText(g2d, year, 519, 309, 157, 16, font13, Color.BLACK);

            // ========== MÃ HÓA RSA + TẠO QR CODE ==========
            String message = "Trường Đại học Đồng Tháp - " + data.getFullName() + " - " + data.getMajor() + " - " + data.getYearGraduation();

            KeyPair keyPair = RSAUtil.generateKeyPair();
            String signature = RSAUtil.sign(message, keyPair.getPrivate());
            String publicKeyStr = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            RSAKeys rsaKeys = new RSAKeys();
            Degree degree = degreeRepository.findDegreeById(data.getDegreeId());
            if (degree != null) {
                rsaKeys.setDegree(degree);
                rsaKeys.setRsaPublicKey(publicKeyStr);
                rsaKeys.setRsaSignature(signature);
                rsaKeys = raKeysRepository.save(rsaKeys);  // có keyId sau khi lưu

                try {
                    // Dữ liệu QR Code: chứa keyId
                    JSONObject qrData = new JSONObject();
                    qrData.put("keyId", rsaKeys.getKeyId());

                    String qrJson = qrData.toString();

                    BufferedImage qrImage = QRUtil.generateQRCodeImageWithLogo(qrJson, 150, 150, logoImage);
                    // Vẽ dòng chữ mã hóa ID
                    drawText(g2d, "Signature ID: " + rsaKeys.getKeyId(), 40, baseImage.getHeight() - 25, 800, 14, font10, Color.GRAY);
                    g2d.drawImage(qrImage, 200, 350, null); // Tọa độ này tùy chỉnh theo vị trí trên văn bằng
                    ImageIO.write(qrImage, "png", new File("qr_debug.png"));
                } catch (Exception qrEx) {
                    System.err.println("Lỗi tạo mã QR: " + qrEx.getMessage());
                    qrEx.printStackTrace();
                }
            }

            // ========== NHÚNG THÔNG ĐIỆP VÀO ẢNH BẰNG LSB ==========
            baseImage = embedLSB(baseImage, message);

            // ========== LƯU ẢNH ==========
            File outputFile = new File(outputFilePath);
            if (!outputFile.getParentFile().exists()) {
                System.out.println("Tạo thư mục: " + outputFile.getParentFile().getAbsolutePath());
                outputFile.getParentFile().mkdirs();
            }
            ImageIO.write(baseImage, "PNG", outputFile);
            g2d.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lô tạo ảnh văn bằng");
        }

        return outputFilePath;
    }
    private BufferedImage embedLSB(BufferedImage image, String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Thông điệp không được null hoặc rỗng");
        }

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length;

        // Kiểm tra kích thước ảnh
        int availableBits = image.getWidth() * image.getHeight();
        int requiredBits = 4 * 8 + messageLength * 8; // 32 bit tiền tố + thông điệp
        if (requiredBits > availableBits) {
            throw new IllegalArgumentException("Thông điệp quá dài để nhúng vào ảnh!");
        }

        // Chuyển độ dài thành binary 32 bit
        String lengthBinary = String.format("%32s", Integer.toBinaryString(messageLength)).replace(' ', '0');
        StringBuilder messageBinary = new StringBuilder();
        for (byte b : messageBytes) {
            messageBinary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        String dataToEmbed = lengthBinary + messageBinary;
        int bitIndex = 0;
        int dataLength = dataToEmbed.length();

        for (int y = 0; y < image.getHeight() && bitIndex < dataLength; y++) {
            for (int x = 0; x < image.getWidth() && bitIndex < dataLength; x++) {
                int pixel = image.getRGB(x, y);
                int blue = pixel & 0xFF;

                // Lấy bit tiếp theo từ dữ liệu
                int bit = Character.getNumericValue(dataToEmbed.charAt(bitIndex));

                // Thay đổi bit LSB của kênh blue
                blue = (blue & 0xFE) | bit;

                // Cập nhật pixel
                pixel = (pixel & 0xFFFFFF00) | blue;
                image.setRGB(x, y, pixel);

                bitIndex++;
            }
        }

        return image;
    }
    @Transactional
    @Override
    public String exportDegree(Integer id) {
       try{

           Degree degree = degreeRepository.findDegreeById(id);
           if(degree == null){
               throw new Exception("Văn bằng này không tồn tại!");
               //return "";
           }

           InfoDegreeRequest infoDegreeRequest = new InfoDegreeRequest();
           infoDegreeRequest.setDegreeId(degree.getDegreeId());
           infoDegreeRequest.setDegreeType(degree.getDegreeType());
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
           infoDegreeRequest.setBirthDate(degree.getStudent().getBirthDay().format(formatter));
           infoDegreeRequest.setFullName(degree.getStudent().getFullName());
           infoDegreeRequest.setMajor(degree.getStudent().getMajor().getMajorName());
           infoDegreeRequest.setMajorNameEng(degree.getStudent().getMajor().getMajorNameEng());
           infoDegreeRequest.setYearGraduation(degree.getIssueDate().getYear());
           infoDegreeRequest.setDegreeClassification(degree.getDegreeClassification());

           String filePath = createDegreeImg(infoDegreeRequest);


           if (filePath != null && !filePath.trim().isEmpty()) {
               DegreeImg degreeImg = new DegreeImg();
               degreeImg.setDegree(degree);
               degreeImg.setCreateDate(LocalDateTime.now());
               degreeImg.setFilePath(filePath);
               degreeImgRepository.save(degreeImg);
               return filePath;
           }


       }catch (Exception e){

           logger.error("Lỗi khi thêm mới văn bằng: {}", e.getMessage(), e);

       }

        return "";
    }

    @Transactional
    @Override
    public MessageCheckDegree checkValidDegree(File fileDegree, HttpServletRequest request) {
        try{
            VerifyDegree result =  QRUtil.verifyDegreeImage(fileDegree, raKeysRepository);

            //lay ra thong tin van bang tu keyId;
            RSAKeys rsaKeys = raKeysRepository.findById(result.getKeyId()).orElse(null);
            if(rsaKeys != null){

                String username = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userService.getUserByUsername(username);

                degreeQuerylogService.addDegreeQueryLog(user, rsaKeys.getDegree().getDegreeId(),request, result.getStatus());

            }

            return new MessageCheckDegree(result.getStatus(),result.getMessage());

        }catch (Exception e){
            logger.error("Lỗi khi kiểm tra văn bằng: {}", e.getMessage(), e);
            return new MessageCheckDegree("Lỗi","Đã xảy ra lỗi trong quá trình kiểm tra văn bằng.");
        }
    }
}
