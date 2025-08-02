package com.whoiszxl.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.whoiszxl.admin.cqrs.command.AdminPasswordResetCommand;
import com.whoiszxl.admin.cqrs.command.AdminRequest;
import com.whoiszxl.admin.cqrs.command.RoleUpdateCommand;
import com.whoiszxl.admin.cqrs.query.AdminQuery;
import com.whoiszxl.admin.cqrs.response.AdminDetailResponse;
import com.whoiszxl.admin.entity.Admin;
import com.whoiszxl.admin.entity.Dept;
import com.whoiszxl.admin.mapper.AdminMapper;
import com.whoiszxl.admin.service.*;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.crud.service.CommonAdminService;
import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends BaseServiceImpl<AdminMapper, Admin, AdminDetailResponse, AdminDetailResponse, AdminQuery, AdminRequest> implements IAdminService, CommonAdminService {

    private final IAdminRoleService adminRoleService;

    private final IDeptService deptService;

    private final IRoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final OnlineAdminService onlineAdminService;



    @Override
    public PageResponse<AdminDetailResponse> page(AdminQuery query, PageQuery pageQuery) {
        QueryWrapper<Admin> queryWrapper = this.buildQueryWrapper(query);
        IPage<Admin> page = baseMapper.selectAdminPage(pageQuery.toPage(), queryWrapper);

        PageResponse<AdminDetailResponse> pageResponse = PageResponse.build(page, this.listClass);
        pageResponse.getList().forEach(this::fill);
        return pageResponse;
    }

    @Override
    protected void fill(Object obj) {
        super.fill(obj);
        if(obj instanceof AdminDetailResponse detail) {
            List<Long> roleIdList = detail.getRoleIds();
            if (CollUtil.isNotEmpty(roleIdList)) {
                detail.setRoleNames(roleService.listRoleNameByRoleIdList(roleIdList));
            }
        }
    }

    @Override
    protected QueryWrapper<Admin> buildQueryWrapper(AdminQuery query) {
        String description = query.getDescription();
        Integer status = query.getStatus();
        List<Date> createdAtList = query.getCreatedAt();
        Long deptId = query.getDeptId();
        return new QueryWrapper<Admin>().and(StrUtil.isNotBlank(description), q -> q.like("t1.username", description)
                        .or()
                        .like("t1.nickname", description)
                        .or()
                        .like("t1.real_name", description))
                .eq(null != status, "t1.status", status)
                .between(CollUtil.isNotEmpty(createdAtList), "t1.created_at", CollUtil.getFirst(createdAtList), CollUtil
                        .getLast(createdAtList))
                .and(null != deptId, q -> {
                    List<Long> deptIdList = deptService.listChildren(deptId)
                            .stream()
                            .map(Dept::getId)
                            .collect(Collectors.toList());
                    deptIdList.add(deptId);
                    q.in("t1.dept_id", deptIdList);
                })
                .and( q -> q.eq("t1.is_deleted", 0));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdminRequest req, Long id) {
        final String errorMsgTemplate = "修改失败，[{}] 已存在";
        String username = req.getUsername();
        CheckUtils.throwIf(this.isNameExists(username, id), errorMsgTemplate, username);
        String email = req.getEmail();
        CheckUtils.throwIf(StrUtil.isNotBlank(email) && this.isEmailExists(email, id), errorMsgTemplate, email);
        String mobile = req.getMobile();
        CheckUtils.throwIf(StrUtil.isNotBlank(mobile) && this.isMobileExists(mobile, id), errorMsgTemplate, mobile);
        DisEnableStatusEnum newStatus = req.getStatus();
        CheckUtils.throwIf(DisEnableStatusEnum.DISABLE.equals(newStatus)
                && ObjectUtil.equal(id, LoginHelper.getAdminId()), "不允许禁用当前用户");

        // 更新信息
        Admin newAdmin = BeanUtil.toBean(req, Admin.class);
        newAdmin.setId(id);
        baseMapper.updateById(newAdmin);
        // 保存用户和角色关联
        boolean isSaveUserRoleSuccess = adminRoleService.saveRoleRelation(req.getRoleIds(), id);
        // 如果功能权限或数据权限有变更，则清除关联的在线用户（重新登录以获取最新角色权限）
        if (DisEnableStatusEnum.DISABLE.equals(newStatus) || isSaveUserRoleSuccess) {
            onlineAdminService.cleanByUserId(id);
        }
    }

    @Override
    public String getNicknameById(Long id) {
        return baseMapper.selectById(id).getNickname();
    }

    @Override
    public Long add(Admin admin) {
        admin.setStatus(DisEnableStatusEnum.ENABLE);
        baseMapper.insert(admin);
        return admin.getId();
    }

    @Override
    protected void afterAdd(AdminRequest req, Admin admin) {
        // 管理员添加成功后，需要在添加管理员和角色的关联关系
        adminRoleService.saveRoleRelation(req.getRoleIds(), admin.getId());
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword, Long id) {
        CheckUtils.throwIfEqual(newPassword, oldPassword, "新密码不能与当前密码相同");
        Admin admin = super.getById(id);
        String password = admin.getPassword();
        if (StrUtil.isNotBlank(password)) {
            CheckUtils.throwIf(!passwordEncoder.matches(oldPassword, password), "当前密码错误");
        }
        // 更新密码和密码重置时间
        admin.setPassword(newPassword);
        baseMapper.updateById(admin);
        onlineAdminService.cleanByUserId(admin.getId());
    }

    @Override
    public void updateMobile(String newMobile, String oldPassword, Long id) {
        Admin admin = super.getById(id);
        CheckUtils.throwIf(!passwordEncoder.matches(oldPassword, admin.getPassword()), "当前密码错误");
        CheckUtils.throwIf(this.isMobileExists(newMobile, id), "手机号已绑定其他账号，请更换其他手机号");
        CheckUtils.throwIfEqual(newMobile, admin.getMobile(), "新手机号不能与当前手机号相同");

        baseMapper.lambdaUpdate().set(Admin::getMobile, newMobile).eq(Admin::getId, id).update();
    }

    @Override
    public void updateEmail(String newEmail, String oldPassword, Long id) {
        Admin admin = super.getById(id);
        CheckUtils.throwIf(!passwordEncoder.matches(oldPassword, admin.getPassword()), "当前密码错误");
        CheckUtils.throwIf(this.isEmailExists(newEmail, id), "邮箱已绑定其他账号，请更换其他邮箱");
        CheckUtils.throwIfEqual(newEmail, admin.getEmail(), "新邮箱不能与当前邮箱相同");

        baseMapper.lambdaUpdate().set(Admin::getEmail, newEmail).eq(Admin::getId, id).update();
    }

    @Override
    public void resetPassword(AdminPasswordResetCommand req, Long id) {
        Admin admin = super.getById(id);
        admin.setPassword(req.getNewPassword());
        baseMapper.updateById(admin);
    }

    @Override
    public void updateRole(RoleUpdateCommand command, Long adminId) {
        super.getById(adminId);
        adminRoleService.saveRoleRelation(command.getRoleIds(), adminId);
    }

    @Override
    public Admin getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public Admin getByMobile(String mobile) {
        return baseMapper.selectByMobile(mobile);
    }

    @Override
    public Admin getByEmail(String email) {
        return baseMapper.selectByEmail(email);
    }

    @Override
    public Long countByDeptIds(List<Long> deptIds) {
        return baseMapper.lambdaQuery().in(Admin::getDeptId, deptIds).count();
    }

    @Override
    public Boolean isPasswordExpired(LocalDateTime pwdResetTime) {
        return null;
    }




    private boolean isNameExists(String name, Long id) {
        return baseMapper.lambdaQuery()
                .eq(Admin::getUsername, name)
                .ne(null != id, Admin::getId, id)
                .exists();
    }

    /**
     * 邮箱是否存在
     *
     * @param email 邮箱
     * @param id    ID
     * @return 是否存在
     */
    private boolean isEmailExists(String email, Long id) {
        Long count = baseMapper.selectCountByEmail(email, id);
        return null != count && count > 0;
    }

    /**
     * 手机号码是否存在
     *
     * @param mobile 手机号码
     * @param id    ID
     * @return 是否存在
     */
    private boolean isMobileExists(String mobile, Long id) {
        Long count = baseMapper.selectCountByMobile(mobile, id);
        return null != count && count > 0;
    }

    @Override
    public void updateAvatar(Long userId, String avatarUrl) {
        Admin admin = new Admin();
        admin.setId(userId);
        admin.setAvatar(avatarUrl);
        updateById(admin);
    }

}
