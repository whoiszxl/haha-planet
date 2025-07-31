package com.whoiszxl.admin.service.impl;

import cn.crane4j.annotation.AutoOperate;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.admin.cqrs.command.LogResp;
import com.whoiszxl.admin.cqrs.query.AdminLogQuery;
import com.whoiszxl.admin.cqrs.response.LogDetailResp;
import com.whoiszxl.admin.cqrs.response.dashboard.DashboardAccessTrendResp;
import com.whoiszxl.admin.cqrs.response.dashboard.DashboardPopularModuleResp;
import com.whoiszxl.admin.cqrs.response.dashboard.DashboardTotalResp;
import com.whoiszxl.admin.cqrs.response.log.LoginLogExportResp;
import com.whoiszxl.admin.cqrs.response.log.OperationLogExportResp;
import com.whoiszxl.admin.entity.AdminLog;
import com.whoiszxl.admin.mapper.AdminLogMapper;
import com.whoiszxl.admin.service.IAdminLogService;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.crud.model.SortQuery;
import com.whoiszxl.starter.crud.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements IAdminLogService {

    private final AdminLogMapper baseMapper;

    @Override
    public PageResponse<LogResp> page(AdminLogQuery query, PageQuery pageQuery) {
        QueryWrapper<AdminLog> queryWrapper = this.buildQueryWrapper(query);
        IPage<LogResp> page = baseMapper.selectLogPage(pageQuery.toPage(), queryWrapper);
        return PageResponse.build(page);
    }

    @Override
    @AutoOperate(type = LogDetailResp.class)
    public LogDetailResp get(Long id) {
        AdminLog logDO = baseMapper.selectById(id);
        CheckUtils.throwIfNotExists(logDO, "AdminLog", "ID", id);
        return BeanUtil.copyProperties(logDO, LogDetailResp.class);
    }

    @Override
    public void exportLoginLog(AdminLogQuery query, SortQuery sortQuery, HttpServletResponse response) {
        List<LoginLogExportResp> list = BeanUtil.copyToList(this.list(query, sortQuery), LoginLogExportResp.class);
        ExcelUtils.export(list, "导出登录日志数据", LoginLogExportResp.class, response);
    }

    @Override
    public void exportOperationLog(AdminLogQuery query, SortQuery sortQuery, HttpServletResponse response) {
        List<OperationLogExportResp> list = BeanUtil.copyToList(this
                .list(query, sortQuery), OperationLogExportResp.class);
        ExcelUtils.export(list, "导出操作日志数据", OperationLogExportResp.class, response);
    }

    @Override
    public DashboardTotalResp getDashboardTotal() {
        return baseMapper.selectDashboardTotal();
    }

    @Override
    public List<DashboardAccessTrendResp> listDashboardAccessTrend(Integer days) {
        return baseMapper.selectListDashboardAccessTrend(days);
    }

    @Override
    public List<DashboardPopularModuleResp> listDashboardPopularModule() {
        return baseMapper.selectListDashboardPopularModule();
    }

    @Override
    public List<Map<String, Object>> listDashboardGeoDistribution() {
        return baseMapper.selectListDashboardGeoDistribution();
    }

    /**
     * 查询列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 列表信息
     */
    private List<LogResp> list(AdminLogQuery query, SortQuery sortQuery) {
        QueryWrapper<AdminLog> queryWrapper = this.buildQueryWrapper(query);
        this.sort(queryWrapper, sortQuery);
        return baseMapper.selectLogList(queryWrapper);
    }

    /**
     * 设置排序
     *
     * @param queryWrapper 查询条件封装对象
     * @param sortQuery    排序查询条件
     */
    private void sort(QueryWrapper<AdminLog> queryWrapper, SortQuery sortQuery) {
        Sort sort = Opt.ofNullable(sortQuery).orElseGet(SortQuery::new).getSort();
        for (Sort.Order order : sort) {
            if (null != order) {
                String property = order.getProperty();
                queryWrapper.orderBy(true, order.isAscending(), CharSequenceUtil.toUnderlineCase(property));
            }
        }
    }

    /**
     * 构建 QueryWrapper
     *
     * @param query 查询条件
     * @return QueryWrapper
     */
    private QueryWrapper<AdminLog> buildQueryWrapper(AdminLogQuery query) {
        String description = query.getDescription();
        String module = query.getModule();
        String ip = query.getIp();
        String createUserString = query.getCreateUserString();
        Integer status = query.getStatus();
        List<Date> createdAtList = query.getCreateTime();
        return new QueryWrapper<AdminLog>().and(StrUtil.isNotBlank(description), q -> q.like("t1.description", description)
                        .or()
                        .like("t1.module", description))
                .eq(StrUtil.isNotBlank(module), "t1.module", module)
                .and(StrUtil.isNotBlank(ip), q -> q.like("t1.ip", ip).or().like("t1.address", ip))
                .and(StrUtil.isNotBlank(createUserString), q -> q.like("t2.username", createUserString)
                        .or()
                        .like("t2.nickname", createUserString))
                .eq(null != status, "t1.status", status)
                .between(CollUtil.isNotEmpty(createdAtList), "t1.created_at", CollUtil.getFirst(createdAtList), CollUtil
                        .getLast(createdAtList));
    }
}
