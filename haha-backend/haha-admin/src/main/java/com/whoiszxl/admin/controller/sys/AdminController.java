package com.whoiszxl.admin.controller.sys;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.whoiszxl.admin.cqrs.command.AdminRequest;
import com.whoiszxl.admin.cqrs.command.AdminPasswordResetCommand;
import com.whoiszxl.admin.cqrs.command.RoleUpdateCommand;
import com.whoiszxl.admin.cqrs.query.AdminQuery;
import com.whoiszxl.admin.cqrs.response.AdminDetailResponse;
import com.whoiszxl.admin.service.IAdminService;
import com.whoiszxl.common.constants.RegexConstants;
import com.whoiszxl.common.utils.SecureUtils;
import com.whoiszxl.starter.core.utils.ExceptionUtils;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.starter.crud.enums.Api;
import com.whoiszxl.starter.crud.utils.ValidateGroup;
import com.whoiszxl.starter.web.model.R;
import com.whoiszxl.storage.core.domain.StorageObject;
import com.whoiszxl.storage.core.domain.UploadRequest;
import com.whoiszxl.storage.core.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Tag(name = "管理员 API")
@Validated
@RestController
@CrudRequestMapping(value = "/system/admin", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
@RequiredArgsConstructor
public class AdminController extends BaseController<IAdminService, AdminDetailResponse, AdminDetailResponse, AdminQuery, AdminRequest> {

    private final StorageService storageService;

    @Override
    public R<Long> add(@Validated(ValidateGroup.Crud.Add.class) @RequestBody AdminRequest req) {
        String rawPassword = ExceptionUtils.exToNull(() ->
                SecureUtils.decryptByRsaPrivateKey(req.getPassword()));

        ValidationUtils.throwIfNull(rawPassword, "密码解密失败");
        ValidationUtils.throwIf(!ReUtil
                .isMatch(RegexConstants.PASSWORD, rawPassword), "密码长度为 8-32 个字符，支持大小写字母、数字、特殊字符，至少包含字母和数字");
        req.setPassword(rawPassword);
        return super.add(req);
    }

    @Operation(summary = "重置密码", description = "重置用户登录密码")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("system:user:reset-password")
    @PatchMapping("/{id}/password")
    public R<Void> resetPassword(@Validated @RequestBody AdminPasswordResetCommand command, @PathVariable Long id) {
        String rawNewPassword = ExceptionUtils.exToNull(() ->
                SecureUtils.decryptByRsaPrivateKey(command.getNewPassword()));
        ValidationUtils.throwIfNull(rawNewPassword, "新密码解密失败");
        ValidationUtils.throwIf(!ReUtil
                .isMatch(RegexConstants.PASSWORD, rawNewPassword), "密码长度为 8-32 个字符，支持大小写字母、数字、特殊字符，至少包含字母和数字");
        command.setNewPassword(rawNewPassword);
        baseService.resetPassword(command, id);
        return R.ok("重置密码成功");
    }

    @Operation(summary = "分配角色", description = "为用户新增或移除角色")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("system:user:updateRole")
    @PatchMapping("/{id}/role")
    public R<Void> updateRole(@Validated @RequestBody RoleUpdateCommand roleUpdateCommand, @PathVariable Long id) {
        baseService.updateRole(roleUpdateCommand, id);
        return R.ok("分配成功");
    }

    @Operation(summary = "上传头像", description = "上传用户头像")
    @SaCheckPermission("system:user:avatar")
    @PostMapping("/avatar")
    public R<Map<String, String>> uploadAvatar(@RequestParam("avatarFile") MultipartFile file) {
        try {
            // 参数校验
            ValidationUtils.throwIfNull(file, "头像文件不能为空");
            ValidationUtils.throwIf(file.isEmpty(), "头像文件不能为空");
            
            // 文件类型校验
            String contentType = file.getContentType();
            ValidationUtils.throwIf(!StrUtil.startWith(contentType, "image/"), "只支持图片格式");
            
            // 文件大小校验 (限制5MB)
            long maxSize = 5 * 1024 * 1024;
            ValidationUtils.throwIf(file.getSize() > maxSize, "头像文件大小不能超过5MB");
            
            // 获取当前登录用户ID
            Long currentUserId = StpUtil.getLoginIdAsLong();
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = StrUtil.subAfter(originalFilename, ".", true);
            String fileName = "avatar/" + currentUserId + "/" + UUID.randomUUID() + "." + fileExtension;
            
            // 构建上传请求
            UploadRequest uploadRequest = new UploadRequest()
                    .setKey(fileName)
                    .setFileName(originalFilename)
                    .setContentType(contentType)
                    .setContent(file.getInputStream())
                    .setContentLength(file.getSize());
            
            // 上传到S3
            StorageObject storageObject = storageService.upload(uploadRequest);
            
            // 更新用户头像
            baseService.updateAvatar(currentUserId, fileName);
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("avatar", storageObject.getUrl());
            
            return R.ok(result);
            
        } catch (IOException e) {
            throw new RuntimeException("头像上传失败", e);
        }
    }

    @Operation(summary = "获取头像预签名URL", description = "获取头像的预签名访问URL")
    @SaCheckPermission("system:user:avatar")
    @PostMapping("/avatar/presigned-url")
    public R<Map<String, String>> getAvatarPresignedUrl(@RequestBody Map<String, String> request) {
        try {
            String avatarKey = request.get("avatarKey");
            // 参数校验
            ValidationUtils.throwIfBlank(avatarKey, "头像键不能为空");
            
            // 生成预签名URL，有效期1小时
            Duration expiration = Duration.ofHours(1);
            URL presignedUrl = storageService.generatePresignedUrl("haha", avatarKey, expiration);
            
            Map<String, String> result = new HashMap<>();
            result.put("presignedUrl", presignedUrl.toString());
            result.put("expiresIn", String.valueOf(expiration.getSeconds()));
            
            return R.ok(result);
            
        } catch (Exception e) {
            throw new RuntimeException("获取预签名URL失败", e);
        }
    }
}

