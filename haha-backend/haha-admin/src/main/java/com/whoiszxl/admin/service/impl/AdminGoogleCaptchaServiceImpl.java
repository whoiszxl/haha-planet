package com.whoiszxl.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.whoiszxl.admin.entity.Admin;
import com.whoiszxl.admin.mapper.AdminMapper;
import com.whoiszxl.captcha.google.properties.GoogleCaptchaProperties;
import com.whoiszxl.captcha.google.service.impl.GoogleCaptchaServiceImpl;
import com.whoiszxl.captcha.google.storage.GoogleCaptchaStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Admin模块的Google验证码服务实现
 * 重写isBoundByUsername方法以支持通过用户名查询绑定状态
 *
 * @author whoiszxl
 */
@Slf4j
@Service
public class AdminGoogleCaptchaServiceImpl extends GoogleCaptchaServiceImpl {

    @Autowired
    private AdminMapper adminMapper;

    public AdminGoogleCaptchaServiceImpl(GoogleCaptchaProperties properties, GoogleAuthenticator googleAuthenticator, GoogleCaptchaStorage storage) {
        super(properties, googleAuthenticator, storage);
    }

    @Override
    public boolean isBoundByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            log.warn("用户名不能为空");
            return false;
        }

        try {
            // 通过用户名查找用户
            LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Admin::getUsername, username);
            Admin adminUser = adminMapper.selectOne(queryWrapper);
            
            if (adminUser == null) {
                log.warn("用户不存在: {}", username);
                return false;
            }

            // 通过用户ID检查绑定状态
            return isBound(String.valueOf(adminUser.getId()));
            
        } catch (Exception e) {
            log.error("通过用户名检查Google验证码绑定状态失败, username: {}", username, e);
            return false;
        }
    }
}
