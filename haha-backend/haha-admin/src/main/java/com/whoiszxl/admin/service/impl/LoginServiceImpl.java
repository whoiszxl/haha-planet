package com.whoiszxl.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whoiszxl.admin.cqrs.response.MenuResponse;
import com.whoiszxl.admin.cqrs.response.RouteResponse;
import com.whoiszxl.admin.entity.Admin;
import com.whoiszxl.admin.entity.Menu;
import com.whoiszxl.admin.service.IMenuService;
import com.whoiszxl.common.constants.SysConstants;
import com.whoiszxl.common.enums.MenuTypeEnum;
import com.whoiszxl.common.properties.RsaProperties;
import com.whoiszxl.admin.service.IAdminService;
import com.whoiszxl.admin.service.ILoginService;
import com.whoiszxl.admin.service.IRoleService;
import com.whoiszxl.captcha.google.service.GoogleCaptchaService;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.common.utils.SecureUtils;
import com.whoiszxl.starter.core.utils.BeanUtil;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.crud.annotation.TreeField;
import com.whoiszxl.starter.crud.utils.TreeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements ILoginService {

    private final IAdminService adminService;

    private final IRoleService roleService;

    private final IMenuService menuService;

    private final PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private GoogleCaptchaService googleCaptchaService;

    @Override
    public String login(String username, String password) {
        //1. 校验管理员是否存在
        Admin admin = adminService.getOne(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, username));
        ValidationUtils.throwIfNull(admin, "管理员不存在");

        //2. 校验密码是否正确
        ValidationUtils.throwIf(!passwordEncoder.matches(password, admin.getPassword()), "密码错误");

        //3. 进行登录，并设置权限
        return this.login(admin);
    }

    @Override
    public String login(String username, String password, String googleCode) {
        //1. 校验管理员是否存在
        Admin admin = adminService.getOne(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getUsername, username));
        ValidationUtils.throwIfNull(admin, "管理员不存在");

        //2. 校验密码是否正确
        ValidationUtils.throwIf(!passwordEncoder.matches(password, admin.getPassword()), "密码错误");

        //3. 检查是否绑定Google验证码，如果绑定了则需要验证
        if (googleCaptchaService != null) {
            String userId = String.valueOf(admin.getId());
            boolean isBound = googleCaptchaService.isBound(userId);
            
            if (isBound) {
                // 已绑定Google验证码，必须提供验证码
                ValidationUtils.throwIf(!StringUtils.hasText(googleCode), "请输入Google验证码");
                
                // 验证Google验证码
                boolean isValid = googleCaptchaService.validate(userId, googleCode);
                ValidationUtils.throwIf(!isValid, "Google验证码错误");
                
                log.info("用户 {} Google验证码验证成功", username);
            } else {
                log.debug("用户 {} 未绑定Google验证码，跳过验证", username);
            }
        }

        //4. 进行登录，并设置权限
        return this.login(admin);
    }

    private String login(Admin admin) {
        Long adminId = admin.getId();
        LoginAdmin loginAdmin = BeanUtil.copyProperties(admin, LoginAdmin.class);
        loginAdmin.setPermissions(roleService.listPermissionByAdminId(adminId));
        loginAdmin.setRoles(roleService.listByAdminId(adminId));
        loginAdmin.setRoleCodes(roleService.listRoleCodeByAdminId(adminId));
        return LoginHelper.login(loginAdmin);
    }

    @Override
    public List<RouteResponse> buildRouteTree(Long adminId) {
        Set<String> roleCodeSet = roleService.listRoleCodeByAdminId(adminId);
        if(CollUtil.isEmpty(roleCodeSet)) {
            return new ArrayList<>(0);
        }


        Set<Menu> menuSet = new LinkedHashSet<>();
        if(roleCodeSet.contains(SysConstants.ADMIN_ROLE_CODE)) {
            menuSet.addAll(menuService.list());
        }else {
            roleCodeSet.forEach(roleCode -> {
                menuSet.addAll(menuService.listByRoleCode(roleCode));
            });
        }

        List<Menu> menuList = menuSet.stream()
                .filter(m -> !MenuTypeEnum.BUTTON.equals(m.getType())).toList();
        List<MenuResponse> menuResponseList = BeanUtil.copyToList(menuList, MenuResponse.class);

        TreeField treeField = MenuResponse.class.getDeclaredAnnotation(TreeField.class);
        TreeNodeConfig treeNodeConfig = TreeUtils.genTreeNodeConfig(treeField);

        List<Tree<Long>> treeList = TreeUtils.build(menuResponseList, treeNodeConfig, (m, tree) -> {
            tree.setId(m.getId());
            tree.setParentId(m.getParentId());
            tree.setName(m.getTitle());
            tree.setWeight(m.getSort());
            tree.putExtra("type", m.getType().getValue());
            tree.putExtra("path", m.getPath());
            tree.putExtra("name", m.getName());
            tree.putExtra("component", m.getComponent());
            tree.putExtra("redirect", m.getRedirect());
            tree.putExtra("icon", m.getIcon());
            tree.putExtra("isExternal", m.getIsFrame());
            tree.putExtra("isCache", m.getIsCache());
            tree.putExtra("isHidden", m.getIsVisible());
            tree.putExtra("permission", m.getPermission());
        });
        return BeanUtil.copyToList(treeList, RouteResponse.class);
    }
}
