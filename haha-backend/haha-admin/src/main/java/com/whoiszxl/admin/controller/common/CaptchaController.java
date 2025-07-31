package com.whoiszxl.admin.controller.common;

import cn.dev33.satoken.annotation.SaIgnore;
import com.whoiszxl.admin.cqrs.response.CaptchaResponse;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.captcha.graphic.model.CaptchaResult;
import com.whoiszxl.captcha.graphic.service.GraphicCaptchaService;
import com.whoiszxl.common.properties.CaptchaProperties;
import com.whoiszxl.starter.log.core.annotation.Log;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

/**
 * @author whoiszxl
 */
@SaIgnore
@Tag(name = "验证码 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/captcha")
public class CaptchaController {

    private final GraphicCaptchaService graphicCaptchaService;

    private final CaptchaProperties captchaProperties;
    private final RedissonUtil redissonUtil;

    @Log(ignore = true)
    @GetMapping("/image")
    @Operation(summary = "获取图片验证码", description = "获取一个Base64格式的验证码")
    public R<Object> getImageCaptcha() {
        //1. 生成图片验证码
        CaptchaResult captchaResult = graphicCaptchaService.generate();
        //2. 将验证码和 uuid 及过期时间存入 Redis，并返回前端
        return R.ok(CaptchaResponse.builder()
                .uuid(captchaResult.getKey())
                .img(captchaResult.getImageBase64())
                .expireTime(captchaResult.getExpireTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build());
    }

}
