package com.whoiszxl.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.captcha.graphic.model.CaptchaResult;
import com.whoiszxl.captcha.graphic.service.GraphicCaptchaService;
import com.whoiszxl.common.constants.RedisPrefixConstants;
import com.whoiszxl.messaging.mail.model.MailSendResult;
import com.whoiszxl.messaging.mail.service.MailService;
import com.whoiszxl.model.response.CaptchaResponse;
import com.whoiszxl.starter.core.config.ProjectProperties;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * C端验证码 API
 * @author whoiszxl
 */
@Slf4j
@Tag(name = "C端验证码 API")
@SaIgnore
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final GraphicCaptchaService graphicCaptchaService;
    private final ProjectProperties projectProperties;
    private final RedissonUtil redissonUtil;
    private final MailService mailService;
    
    // 邮箱验证码配置常量
    private static final int EMAIL_CAPTCHA_LENGTH = 6;
    private static final long EMAIL_CAPTCHA_EXPIRATION_MINUTES = 5L;
    private static final String EMAIL_TEMPLATE_PATH = "mail/captcha.ftl";
    
    // 短信验证码配置常量
    private static final int SMS_CAPTCHA_LENGTH = 6;
    private static final long SMS_CAPTCHA_EXPIRATION_MINUTES = 5L;

    @Operation(summary = "获取图片验证码", description = "获取图片验证码（Base64编码，带图片格式：data:image/gif;base64）")
    @GetMapping("/image")
    public R<CaptchaResponse> getImageCaptcha() {
        String uuid = IdUtil.fastUUID();
        String captchaKey = RedisPrefixConstants.Member.MEMBER_CAPTCHA_IMAGE_KEY + uuid;
        CaptchaResult captchaResult = graphicCaptchaService.generate();

        return R.ok(CaptchaResponse.builder()
                .uuid(captchaResult.getKey())
                .img(captchaResult.getImageBase64())
                .expireTime(captchaResult.getExpireTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build());
    }

    @Operation(summary = "获取邮箱验证码", description = "发送验证码到指定邮箱")
    @GetMapping("/mail")
    public R<String> getMailCaptcha(
            @NotBlank(message = "邮箱不能为空")
            @Pattern(regexp = RegexPool.EMAIL, message = "邮箱格式错误")
            String email
    ) {

        // 生成验证码
        String captcha = RandomUtil.randomNumbers(EMAIL_CAPTCHA_LENGTH);
        
        // 准备模板变量
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("siteUrl", projectProperties.getUrl());
        templateVariables.put("siteTitle", "哈哈星球");
        templateVariables.put("siteCopyright", "哈哈星球 版权所有");
        templateVariables.put("captcha", captcha);
        templateVariables.put("expiration", EMAIL_CAPTCHA_EXPIRATION_MINUTES);
        
        // 使用 mailService 发送模板邮件
        String subject = String.format("【%s】邮箱验证码", projectProperties.getName());
        MailSendResult result = mailService.sendTemplate(
            email, 
            subject, 
            EMAIL_TEMPLATE_PATH, 
            templateVariables
        );
        
        // 检查发送结果
        if (!result.isSuccess()) {
            log.error("邮件发送失败: {}", result.getErrorMessage());
            return R.fail("邮件发送失败，请稍后重试");
        }
        
        // 保存验证码到 Redis
        String captchaKey = RedisPrefixConstants.Member.MEMBER_CAPTCHA_EMAIL_KEY + email;
        redissonUtil.set(captchaKey, captcha, Duration.ofMinutes(EMAIL_CAPTCHA_EXPIRATION_MINUTES));
        
        log.info("邮箱验证码发送成功 - Email: {}, MessageId: {}", email, result.getMessageId());
        return R.ok(String.format("发送成功，验证码有效期 %s 分钟", EMAIL_CAPTCHA_EXPIRATION_MINUTES));
    }

    @Operation(summary = "获取短信验证码", description = "发送验证码到指定手机号")
    @GetMapping("/sms")
    public R<String> getSmsCaptcha(
            @NotBlank(message = "手机号不能为空") 
            @Pattern(regexp = RegexPool.MOBILE, message = "手机号格式错误") 
            String phone
    ) {
        // 生成验证码
        String captcha = RandomUtil.randomNumbers(SMS_CAPTCHA_LENGTH);
        
        // 发送验证码
        SmsBlend smsBlend = SmsFactory.getBySupplier(SupplierConstant.ALIBABA);
        Map<String, String> messageMap = MapUtil.newHashMap(2, true);
        messageMap.put("code", captcha);
        SmsResponse smsResponse = smsBlend.sendMessage(phone, (LinkedHashMap<String, String>) messageMap);
        CheckUtils.throwIf(!smsResponse.isSuccess(), "验证码发送失败");
        
        // 保存验证码
        String captchaKey = RedisPrefixConstants.Member.MEMBER_CAPTCHA_PHONE_KEY + phone;
        redissonUtil.set(captchaKey, captcha, Duration.ofMinutes(SMS_CAPTCHA_EXPIRATION_MINUTES));
        return R.ok("发送成功，验证码有效期 %s 分钟".formatted(SMS_CAPTCHA_EXPIRATION_MINUTES));
    }
}
