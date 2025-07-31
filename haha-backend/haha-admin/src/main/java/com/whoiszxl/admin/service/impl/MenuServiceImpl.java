package com.whoiszxl.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.whoiszxl.admin.cqrs.command.MenuReq;
import com.whoiszxl.admin.cqrs.query.MenuQuery;
import com.whoiszxl.admin.cqrs.response.MenuResponse;
import com.whoiszxl.admin.entity.Menu;
import com.whoiszxl.admin.mapper.MenuMapper;
import com.whoiszxl.admin.service.IMenuService;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.common.enums.MenuTypeEnum;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu, MenuResponse, MenuResponse, MenuQuery, MenuReq> implements IMenuService {


    @Override
    public Long add(MenuReq req) {
        String title = req.getTitle();
        CheckUtils.throwIf(this.isNameExists(title, req.getParentId(), null), "新增失败，[{}] 已存在", title);

        if (MenuTypeEnum.DIR.equals(req.getType())) {
            req.setComponent(StrUtil.blankToDefault(req.getComponent(), "Layout"));
        }
        return super.add(req);
    }


    @Override
    public void update(MenuReq req, Long id) {
        String title = req.getTitle();
        CheckUtils.throwIf(this.isNameExists(title, req.getParentId(), id), "修改失败，[{}] 已存在", title);
        Menu oldMenu = super.getById(id);
        CheckUtils.throwIfNotEqual(req.getType(), oldMenu.getType(), "不允许修改菜单类型");
        super.update(req, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        baseMapper.lambdaUpdate().in(Menu::getParentId, ids).remove();
        super.delete(ids);
    }

    @Override
    public Set<String> listPermissionByAdminId(Long adminId) {
        return baseMapper.selectPermissionByAdminId(adminId);
    }

    @Override
    public List<Menu> listByRoleCode(String roleCode) {
        return baseMapper.selectByRoleCode(roleCode);
    }

    @Override
    public List<MenuResponse> listAll() {
        return super.list(new MenuQuery(DisEnableStatusEnum.ENABLE.getValue()), null);
    }

    private boolean isNameExists(String name, Long parentId, Long id) {
        return baseMapper.lambdaQuery()
                .eq(Menu::getTitle, name)
                .eq(Menu::getParentId, parentId)
                .ne(null != id, Menu::getId, id)
                .exists();
    }
}
