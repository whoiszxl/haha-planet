package com.whoiszxl.captcha.graphic.controller;

import com.whoiszxl.captcha.graphic.model.CaptchaResult;
import com.whoiszxl.captcha.graphic.model.CaptchaValidateRequest;
import com.whoiszxl.captcha.graphic.service.GraphicCaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * 图形验证码控制器
 *
 * @author whoiszxl
 */
@RestController
@RequestMapping("/api/captcha/graphic")
@RequiredArgsConstructor
@Validated
@ConditionalOnProperty(prefix = "haha.captcha.graphic", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GraphicCaptchaController {

    private final GraphicCaptchaService graphicCaptchaService;

    /**
     * 生成验证码
     */
    @GetMapping("/generate")
    public ResponseEntity<CaptchaResult> generate() {
        CaptchaResult result = graphicCaptchaService.generate();
        return ResponseEntity.ok(result);
    }

    /**
     * 生成验证码（指定key）
     */
    @GetMapping("/generate/{key}")
    public ResponseEntity<CaptchaResult> generate(@PathVariable @NotBlank String key) {
        CaptchaResult result = graphicCaptchaService.generate(key);
        return ResponseEntity.ok(result);
    }

    /**
     * 验证验证码
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody @Valid CaptchaValidateRequest request) {
        boolean isValid = graphicCaptchaService.validate(request);
        return ResponseEntity.ok(isValid);
    }

    /**
     * 验证验证码（简化接口）
     */
    @PostMapping("/validate/{key}/{value}")
    public ResponseEntity<Boolean> validate(
            @PathVariable @NotBlank String key,
            @PathVariable @NotBlank String value) {
        boolean isValid = graphicCaptchaService.validate(key, value);
        return ResponseEntity.ok(isValid);
    }

    /**
     * 删除验证码
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<Void> remove(@PathVariable @NotBlank String key) {
        graphicCaptchaService.remove(key);
        return ResponseEntity.ok().build();
    }
}