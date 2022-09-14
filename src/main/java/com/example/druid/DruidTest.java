package com.example.druid;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;
import java.util.Map;

/**
 * @author LTJ
 * @date 2022/8/12
 */
public class DruidTest {
    public static void main(String[] args) {
        List<SQLStatement> sqlStatements=null;
        try {
             sqlStatements = SQLUtils.parseStatements("select * from train_log a,da_alarm_log b where id=1", JdbcConstants.SQL_SERVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SQLServerSchemaStatVisitor sqlServerSchemaStatVisitor = new SQLServerSchemaStatVisitor();
        for (SQLStatement sqlStatement : sqlStatements) {
            sqlStatement.accept(sqlServerSchemaStatVisitor);
            Map<TableStat.Name, TableStat> tables = sqlServerSchemaStatVisitor.getTables();
            for (Map.Entry<TableStat.Name, TableStat> entry : tables.entrySet()) {
                TableStat.Name tableName = entry.getKey();
                TableStat value = entry.getValue();
                String s = value.toString();
                System.out.println(StrUtil.format("tableName:{} --- {}", tableName, s));
            }
        }
    }
}

