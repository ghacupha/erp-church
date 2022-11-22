package io.github.erp.repository.rowmapper;

import io.github.erp.domain.AppUser;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AppUser}, with proper type conversions.
 */
@Service
public class AppUserRowMapper implements BiFunction<Row, String, AppUser> {

    private final ColumnConverter converter;

    public AppUserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AppUser} stored in the database.
     */
    @Override
    public AppUser apply(Row row, String prefix) {
        AppUser entity = new AppUser();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDesignation(converter.fromRow(row, prefix + "_designation", String.class));
        entity.setSystemUserId(converter.fromRow(row, prefix + "_system_user_id", Long.class));
        entity.setOrganizationId(converter.fromRow(row, prefix + "_organization_id", Long.class));
        return entity;
    }
}
