package io.github.erp.repository.rowmapper;

import io.github.erp.domain.Placeholder;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Placeholder}, with proper type conversions.
 */
@Service
public class PlaceholderRowMapper implements BiFunction<Row, String, Placeholder> {

    private final ColumnConverter converter;

    public PlaceholderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Placeholder} stored in the database.
     */
    @Override
    public Placeholder apply(Row row, String prefix) {
        Placeholder entity = new Placeholder();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPlaceholderIndex(converter.fromRow(row, prefix + "_placeholder_index", String.class));
        entity.setPlaceholderValue(converter.fromRow(row, prefix + "_placeholder_value", String.class));
        entity.setArchetypeId(converter.fromRow(row, prefix + "_archetype_id", Long.class));
        return entity;
    }
}
