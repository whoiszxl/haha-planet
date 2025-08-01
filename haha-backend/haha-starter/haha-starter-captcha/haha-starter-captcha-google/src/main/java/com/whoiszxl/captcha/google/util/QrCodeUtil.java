package com.whoiszxl.captcha.google.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 *
 * @author whoiszxl
 */
@Slf4j
public class QrCodeUtil {

    /**
     * 生成二维码图片（Base64编码）
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @param format  图片格式
     * @return Base64编码的图片数据
     */
    public static String generateQrCodeImage(String content, int width, int height, String format) {
        try {
            // 设置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            // 生成二维码
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 转换为BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 转换为Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, format.toLowerCase(), baos);
            byte[] imageBytes = baos.toByteArray();
            
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            log.debug("生成二维码成功，大小: {}x{}, 格式: {}", width, height, format);
            
            return "data:image/" + format.toLowerCase() + ";base64," + base64Image;

        } catch (WriterException | IOException e) {
            log.error("生成二维码失败", e);
            throw new RuntimeException("生成二维码失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成二维码图片字节数组
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @param format  图片格式
     * @return 图片字节数组
     */
    public static byte[] generateQrCodeBytes(String content, int width, int height, String format) {
        try {
            // 设置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            // 生成二维码
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 转换为BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, format.toLowerCase(), baos);
            
            log.debug("生成二维码字节数组成功，大小: {}x{}, 格式: {}", width, height, format);
            return baos.toByteArray();

        } catch (WriterException | IOException e) {
            log.error("生成二维码字节数组失败", e);
            throw new RuntimeException("生成二维码字节数组失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成二维码BufferedImage
     *
     * @param content 二维码内容
     * @param width   宽度
     * @param height  高度
     * @return BufferedImage对象
     */
    public static BufferedImage generateQrCodeBufferedImage(String content, int width, int height) {
        try {
            // 设置二维码参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            // 生成二维码
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 转换为BufferedImage
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
            log.debug("生成二维码BufferedImage成功，大小: {}x{}", width, height);
            return bufferedImage;

        } catch (WriterException e) {
            log.error("生成二维码BufferedImage失败", e);
            throw new RuntimeException("生成二维码BufferedImage失败: " + e.getMessage(), e);
        }
    }
}
