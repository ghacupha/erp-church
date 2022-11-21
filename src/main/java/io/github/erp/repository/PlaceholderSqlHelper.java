package io.github.erp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PlaceholderSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("placeholder_index", table, columnPrefix + "_placeholder_index"));
        columns.add(Column.aliased("placeholder_value", table, columnPrefix + "_placeholder_value"));

        columns.add(Column.aliased("archetype_id", table, columnPrefix + "_archetype_id"));
        return columns;
    }
}
