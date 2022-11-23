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

import io.github.erp.domain.User;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link User}, with proper type conversions.
 */
@Service
public class UserRowMapper implements BiFunction<Row, String, User> {

    private final ColumnConverter converter;

    public UserRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link User} stored in the database.
     */
    @Override
    public User apply(Row row, String prefix) {
        User entity = new User();
        entity.setId(row.get(prefix + "_id", Long.class));
        entity.setLogin(converter.fromRow(row, prefix + "_login", String.class));
        entity.setPassword(converter.fromRow(row, prefix + "_password", String.class));
        entity.setFirstName(converter.fromRow(row, prefix + "_first_name", String.class));
        entity.setLastName(converter.fromRow(row, prefix + "_last_name", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setActivated(Boolean.TRUE.equals(converter.fromRow(row, prefix + "_activated", Boolean.class)));
        entity.setLangKey(converter.fromRow(row, prefix + "_lang_key", String.class));
        entity.setImageUrl(converter.fromRow(row, prefix + "_image_url", String.class));
        entity.setActivationKey(converter.fromRow(row, prefix + "_activation_key", String.class));
        entity.setResetKey(converter.fromRow(row, prefix + "_reset_key", String.class));
        entity.setResetDate(converter.fromRow(row, prefix + "_reset_date", Instant.class));
        return entity;
    }
}
