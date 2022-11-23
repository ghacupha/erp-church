package io.github.erp.repository.rowmapper;

/*-
 * Erp Church - Data management for religious institutions
 * Copyright © 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
        entity.setOrganizationId(converter.fromRow(row, prefix + "_organization_id", Long.class));
        return entity;
    }
}
