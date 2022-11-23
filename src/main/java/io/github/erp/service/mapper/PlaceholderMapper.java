package io.github.erp.service.mapper;

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

import io.github.erp.domain.AppUser;
import io.github.erp.domain.Placeholder;
import io.github.erp.service.dto.AppUserDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Placeholder} and its DTO {@link PlaceholderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaceholderMapper extends EntityMapper<PlaceholderDTO, Placeholder> {
    @Mapping(target = "archetype", source = "archetype", qualifiedByName = "placeholderPlaceholderValue")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "appUserDesignation")
    PlaceholderDTO toDto(Placeholder s);

    @Named("placeholderPlaceholderValue")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "placeholderValue", source = "placeholderValue")
    PlaceholderDTO toDtoPlaceholderPlaceholderValue(Placeholder placeholder);

    @Named("appUserDesignation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    AppUserDTO toDtoAppUserDesignation(AppUser appUser);
}
