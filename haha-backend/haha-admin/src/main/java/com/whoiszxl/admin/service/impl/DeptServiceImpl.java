package com.whoiszxl.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.admin.cqrs.command.DeptRequest;
import com.whoiszxl.admin.cqrs.query.DeptQuery;
import com.whoiszxl.admin.cqrs.response.DeptResponse;
import com.whoiszxl.admin.entity.Dept;
import com.whoiszxl.admin.mapper.DeptMapper;
import com.whoiszxl.admin.service.IDeptService;
import com.whoiszxl.starter.crud.model.SortQuery;
import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.starter.crud.utils.TreeUtils;
import com.whoiszxl.starter.mybatis.enums.DatabaseType;
import com.whoiszxl.starter.mybatis.utils.MetaUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Service
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept, DeptResponse, DeptResponse, DeptQuery, DeptRequest> implements IDeptService {

    @Override
    public List<Dept> listChildren(Long id) {
        DatabaseType databaseType = MetaUtils.getDatabaseTypeOrDefault(SpringUtil
                .getBean(DynamicRoutingDataSource.class), DatabaseType.MYSQL);
        return baseMapper.lambdaQuery().apply(databaseType.findInSet(id, "ancestors")).list();
    }
}
