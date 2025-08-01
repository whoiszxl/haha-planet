package com.whoiszxl.starter.mybatis.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.MetaUtil;
import com.whoiszxl.starter.core.exception.ServiceException;
import com.whoiszxl.starter.mybatis.enums.DatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 数据库元数据信息工具类
 */
public class MetaUtils {

    private MetaUtils() {
    }

    /**
     * 获取数据库类型（如果获取不到数据库类型，则返回默认数据库类型）
     *
     * @param dataSource   数据源
     * @param defaultValue 默认数据库类型
     * @return 数据库类型
     * @since 1.4.1
     */
    public static DatabaseType getDatabaseTypeOrDefault(DataSource dataSource, DatabaseType defaultValue) {
        DatabaseType databaseType = getDatabaseType(dataSource);
        return null == databaseType ? defaultValue : databaseType;
    }

    /**
     * 获取数据库类型
     *
     * @param dataSource 数据源
     * @return 数据库类型
     * @since 1.4.1
     */
    public static DatabaseType getDatabaseType(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            return DatabaseType.get(databaseProductName);
        } catch (SQLException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 获取所有表信息
     *
     * @param dataSource 数据源
     * @return 表信息列表
     */
    public static List<Table> getTables(DataSource dataSource) throws SQLException {
        return getTables(dataSource, null);
    }

    /**
     * 获取所有表信息
     *
     * @param dataSource 数据源
     * @param tableName  表名称
     * @return 表信息列表
     */
    public static List<Table> getTables(DataSource dataSource, String tableName) throws SQLException {
        String querySql = "SHOW TABLE STATUS";
        List<Entity> tableEntityList;
        Db db = Db.use(dataSource);
        if (CharSequenceUtil.isNotBlank(tableName)) {
            tableEntityList = db.query("%s WHERE NAME = ?".formatted(querySql), tableName);
        } else {
            tableEntityList = db.query(querySql);
        }
        List<Table> tableList = new ArrayList<>(tableEntityList.size());
        for (Entity tableEntity : tableEntityList) {
            Table table = new Table(tableEntity.getStr("NAME"));
            table.setComment(tableEntity.getStr("COMMENT"));
            table.setEngine(tableEntity.getStr("ENGINE"));
            table.setCharset(tableEntity.getStr("COLLATION"));
            table.setCreatedAt(DateUtil.toLocalDateTime(tableEntity.getDate("CREATE_TIME")));
            table.setUpdatedAt(DateUtil.toLocalDateTime(tableEntity.getDate("UPDATE_TIME")));
            tableList.add(table);
        }
        return tableList;
    }

    /**
     * 获取所有列信息
     *
     * @param dataSource 数据源
     * @param tableName  表名称
     * @return 列信息列表
     */
    public static Collection<Column> getColumns(DataSource dataSource, String tableName) {
        cn.hutool.db.meta.Table table = MetaUtil.getTableMeta(dataSource, tableName);
        return table.getColumns();
    }
}
