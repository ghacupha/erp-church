package io.github.erp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AppUserSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("designation", table, columnPrefix + "_designation"));

        columns.add(Column.aliased("system_user_id", table, columnPrefix + "_system_user_id"));
        columns.add(Column.aliased("organization_id", table, columnPrefix + "_organization_id"));
        return columns;
    }
}
