package com.whoiszxl.captcha.graphic.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import com.whoiszxl.captcha.graphic.model.CaptchaResult;
import com.whoiszxl.captcha.graphic.model.CaptchaValidateRequest;
import com.whoiszxl.captcha.graphic.properties.GraphicCaptchaProperties;
import com.whoiszxl.captcha.graphic.service.GraphicCaptchaService;
import com.whoiszxl.captcha.graphic.storage.CaptchaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * 图形验证码服务实现
 *
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GraphicCaptchaServiceImpl implements GraphicCaptchaService {

    private final GraphicCaptchaProperties properties;
    private final CaptchaStorage captchaStorage;

    @Override
    public CaptchaResult generate() {
        return generate(IdUtil.fastSimpleUUID());
    }

    @Override
    public CaptchaResult generate(String key) {
        try {
            // 创建验证码对象
            Captcha captcha = createCaptcha();
            
            // 获取验证码文本和图片
            String text = captcha.text();
            String imageBase64 = captcha.toBase64();
            
            // 存储验证码
            captchaStorage.store(key, text, properties.getExpireTime());
            
            // 构建结果
            LocalDateTime now = LocalDateTime.now();
            CaptchaResult result = CaptchaResult.builder()
                    .key(key)
                    .imageBase64(imageBase64)
                    .createTime(now)
                    .expireTime(now.plus(properties.getExpireTime()))
                    .build();
            
            log.debug("生成验证码成功: key={}, type={}", key, properties.getType());
            return result;
            
        } catch (Exception e) {
            log.error("生成验证码失败: key={}", key, e);
            throw new RuntimeException("生成验证码失败", e);
        }
    }

    @Override
    public boolean validate(CaptchaValidateRequest request) {
        return validate(request.getKey(), request.getValue(), request.isIgnoreCase(), request.isRemoveAfterValidate());
    }

    @Override
    public boolean validate(String key, String value) {
        return validate(key, value, true, true);
    }

    private boolean validate(String key, String value, boolean ignoreCase, boolean removeAfterValidate) {
        if (StrUtil.isBlank(key) || StrUtil.isBlank(value)) {
            log.warn("验证码验证失败: 参数为空, key={}, value={}", key, value);
            return false;
        }

        try {
            String storedValue = captchaStorage.get(key);
            if (StrUtil.isBlank(storedValue)) {
                log.warn("验证码验证失败: 验证码不存在或已过期, key={}", key);
                return false;
            }

            boolean isValid;
            if (ignoreCase) {
                isValid = storedValue.equalsIgnoreCase(value);
            } else {
                isValid = storedValue.equals(value);
            }

            if (removeAfterValidate) {
                captchaStorage.remove(key);
            }

            log.debug("验证码验证结果: key={}, valid={}", key, isValid);
            return isValid;
            
        } catch (Exception e) {
            log.error("验证码验证异常: key={}", key, e);
            return false;
        }
    }

    @Override
    public void remove(String key) {
        captchaStorage.remove(key);
        log.debug("移除验证码: key={}", key);
    }

    @Override
    public void clearExpired() {
        captchaStorage.clearExpired();
        log.debug("清理过期验证码");
    }

    /**
     * 创建验证码对象
     */
    private Captcha createCaptcha() {
        Captcha captcha;
        
        switch (properties.getType()) {
            case ARITHMETIC:
                captcha = new ArithmeticCaptcha(properties.getWidth(), properties.getHeight());
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(properties.getWidth(), properties.getHeight());
                ((ChineseCaptcha) captcha).setLen(properties.getLength());
                break;
            case ENGLISH:
                captcha = new SpecCaptcha(properties.getWidth(), properties.getHeight(), properties.getLength());
                ((SpecCaptcha) captcha).setCharType(Captcha.TYPE_ONLY_CHAR);
                break;
            case NUMBER:
                captcha = new SpecCaptcha(properties.getWidth(), properties.getHeight(), properties.getLength());
                ((SpecCaptcha) captcha).setCharType(Captcha.TYPE_ONLY_NUMBER);
                break;
            case MIXED:
            default:
                captcha = new SpecCaptcha(properties.getWidth(), properties.getHeight(), properties.getLength());
                ((SpecCaptcha) captcha).setCharType(Captcha.TYPE_DEFAULT);
                break;
        }
        
        // 设置字体
        if (StrUtil.isNotBlank(properties.getFontName())) {
            Font font = new Font(properties.getFontName(), Font.PLAIN, properties.getFontSize());
            captcha.setFont(font);
        }
        
        return captcha;
    }
}