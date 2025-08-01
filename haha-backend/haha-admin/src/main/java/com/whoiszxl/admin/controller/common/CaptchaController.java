package com.whoiszxl.admin.controller.common;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.whoiszxl.admin.cqrs.response.CaptchaResponse;
import com.whoiszxl.captcha.google.model.GoogleCaptchaResult;
import com.whoiszxl.captcha.google.service.GoogleCaptchaService;
import com.whoiszxl.captcha.graphic.model.CaptchaResult;
import com.whoiszxl.captcha.graphic.service.GraphicCaptchaService;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.starter.log.core.annotation.Log;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whoiszxl
 */
@Slf4j
@SaIgnore
@Tag(name = "验证码 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/captcha")
public class CaptchaController {

    private final GraphicCaptchaService graphicCaptchaService;

    private final GoogleCaptchaService googleCaptchaService;

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

    @Log(ignore = true)
    @GetMapping("/google")
    @Operation(summary = "生成Google验证码", description = "为当前用户生成Google验证码密钥和二维码")
    public R<Object> generateGoogleCaptcha() {
        try {
            // 获取当前登录用户信息
            LoginAdmin loginAdmin = LoginHelper.getLoginAdmin();
            Long currentUserId = loginAdmin.getId();
            String username = loginAdmin.getUsername();

            // 生成Google验证码密钥和二维码
            GoogleCaptchaResult result = googleCaptchaService.generateSecret(String.valueOf(currentUserId), username);
            
            return R.ok(result);
        } catch (Exception e) {
            log.error("生成Google验证码失败", e);
            return R.fail("生成Google验证码失败: " + e.getMessage());
        }
    }

    @Log(ignore = true)
    @PostMapping("/google/validate")
    @Operation(summary = "验证Google验证码", description = "验证当前用户输入的Google验证码")
    public R<Object> validateGoogleCaptcha(
            @Parameter(description = "验证码", required = true)
            @RequestParam String code) {
        try {
            // 获取当前登录用户ID
            String currentUserId = StpUtil.getLoginIdAsString();
            
            // 验证Google验证码
            boolean isValid = googleCaptchaService.validate(currentUserId, code);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("valid", isValid);
            responseData.put("message", isValid ? "验证成功" : "验证失败");
            
            return R.ok(responseData);
        } catch (Exception e) {
            log.error("验证Google验证码失败, code: {}", code, e);
            return R.fail("验证Google验证码失败: " + e.getMessage());
        }
    }

    @Log(ignore = true)
    @GetMapping("/google/status")
    @Operation(summary = "检查Google验证码绑定状态", description = "检查当前用户是否已绑定Google验证器")
    public R<Object> checkGoogleCaptchaStatus() {
        try {
            // 获取当前登录用户ID
            String currentUserId = StpUtil.getLoginIdAsString();
            
            boolean isBound = googleCaptchaService.isBound(currentUserId);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bound", isBound);
            responseData.put("message", isBound ? "已绑定" : "未绑定");
            
            return R.ok(responseData);
        } catch (Exception e) {
            log.error("检查Google验证码绑定状态失败", e);
            return R.fail("检查绑定状态失败: " + e.getMessage());
        }
    }

    @Log(ignore = true)
    @GetMapping("/google/status/public")
    @Operation(summary = "公开检查Google验证码绑定状态", description = "通过用户名检查用户是否已绑定Google验证器（登录前使用）")
    public R<Object> checkGoogleCaptchaStatusByUsername(
            @Parameter(description = "用户名", required = true)
            @RequestParam String username) {
        try {
            // 这里需要通过用户名查找用户ID
            // 由于这是公开接口，只返回是否绑定的状态，不返回敏感信息
            boolean isBound = googleCaptchaService.isBoundByUsername(username);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bound", isBound);
            responseData.put("message", isBound ? "该用户已绑定Google验证器" : "该用户未绑定Google验证器");
            
            return R.ok(responseData);
        } catch (Exception e) {
            log.error("通过用户名检查Google验证码绑定状态失败, username: {}", username, e);
            return R.fail("检查绑定状态失败: " + e.getMessage());
        }
    }

    @Log(ignore = true)
    @PostMapping("/google/backup-codes")
    @Operation(summary = "生成备用恢复码", description = "为当前用户生成备用恢复码")
    public R<Object> generateBackupCodes(
            @Parameter(description = "生成数量")
            @RequestParam(defaultValue = "10") int count) {
        try {
            // 获取当前登录用户ID
            String currentUserId = StpUtil.getLoginIdAsString();
            
            String[] backupCodes = googleCaptchaService.generateBackupCodes(currentUserId, count);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("backupCodes", backupCodes);
            responseData.put("count", backupCodes.length);
            
            return R.ok(responseData);
        } catch (Exception e) {
            log.error("生成备用恢复码失败, count: {}", count, e);
            return R.fail("生成备用恢复码失败: " + e.getMessage());
        }
    }

    @Log(ignore = true)
    @PostMapping("/google/backup-codes/validate")
    @Operation(summary = "验证备用恢复码", description = "验证当前用户输入的备用恢复码")
    public R<Object> validateBackupCode(
            @Parameter(description = "备用恢复码", required = true)
            @RequestParam String backupCode) {
        try {
            // 获取当前登录用户ID
            String currentUserId = StpUtil.getLoginIdAsString();
            
            boolean isValid = googleCaptchaService.validateBackupCode(currentUserId, backupCode);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("valid", isValid);
            responseData.put("message", isValid ? "验证成功" : "验证失败");
            
            return R.ok(responseData);
        } catch (Exception e) {
            log.error("验证备用恢复码失败, backupCode: {}", backupCode, e);
            return R.fail("验证备用恢复码失败: " + e.getMessage());
        }
    }

    @Log(ignore = true)
    @DeleteMapping("/google/unbind")
    @Operation(summary = "解绑Google验证器", description = "解绑当前用户的Google验证器")
    public R<Object> unbindGoogleCaptcha() {
        try {
            // 获取当前登录用户ID
            String currentUserId = StpUtil.getLoginIdAsString();
            
            googleCaptchaService.removeSecret(currentUserId);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "解绑成功");
            
            return R.ok(responseData);
        } catch (Exception e) {
            log.error("解绑Google验证器失败", e);
            return R.fail("解绑Google验证器失败: " + e.getMessage());
        }
    }

}
