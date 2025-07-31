package com.whoiszxl.admin.controller.sys;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.whoiszxl.admin.cqrs.command.MenuReq;
import com.whoiszxl.admin.cqrs.query.MenuQuery;
import com.whoiszxl.admin.cqrs.response.MenuResponse;
import com.whoiszxl.admin.service.IMenuService;
import com.whoiszxl.starter.core.constants.StringConstants;
import com.whoiszxl.starter.core.utils.URLUtils;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.starter.crud.enums.Api;
import com.whoiszxl.starter.crud.utils.ValidateGroup;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Tag(name = "系统菜单 API")
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "/system/menu", api = {Api.TREE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE})
public class MenuController extends BaseController<IMenuService, MenuResponse, MenuResponse, MenuQuery, MenuReq> {

    @Override
    public R<Long> add(@Validated(ValidateGroup.Crud.Add.class) @RequestBody MenuReq req) {
        this.checkPath(req);
        return super.add(req);
    }

    @Override
    public R<Void> update(@Validated(ValidateGroup.Crud.Update.class) @RequestBody MenuReq req, @PathVariable Long id) {
        this.checkPath(req);
        return super.update(req, id);
    }

    /**
     * 检查路由地址格式
     *
     * @param req 创建或修改信息
     */
    private void checkPath(MenuReq req) {
        Boolean isExternal = ObjectUtil.defaultIfNull(req.getIsFrame(), false);
        String path = req.getPath();
        ValidationUtils.throwIf(Boolean.TRUE.equals(isExternal) && !URLUtils
                .isHttpUrl(path), "路由地址格式错误，请以 http:// 或 https:// 开头");
        // 非外链菜单参数修正
        if (Boolean.FALSE.equals(isExternal)) {
            ValidationUtils.throwIf(URLUtils.isHttpUrl(path), "路由地址格式错误");
            req.setPath(StrUtil.isBlank(path) ? path : StrUtil.prependIfMissing(path, StringConstants.SLASH));
            req.setName(StrUtil.removePrefix(req.getName(), StringConstants.SLASH));
            req.setComponent(StrUtil.removePrefix(req.getComponent(), StringConstants.SLASH));
        }
    }
}

