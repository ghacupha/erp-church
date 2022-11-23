package io.github.erp.service.dto;

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

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.Placeholder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlaceholderDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String placeholderIndex;

    private String placeholderValue;

    private PlaceholderDTO archetype;

    private AppUserDTO organization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceholderIndex() {
        return placeholderIndex;
    }

    public void setPlaceholderIndex(String placeholderIndex) {
        this.placeholderIndex = placeholderIndex;
    }

    public String getPlaceholderValue() {
        return placeholderValue;
    }

    public void setPlaceholderValue(String placeholderValue) {
        this.placeholderValue = placeholderValue;
    }

    public PlaceholderDTO getArchetype() {
        return archetype;
    }

    public void setArchetype(PlaceholderDTO archetype) {
        this.archetype = archetype;
    }

    public AppUserDTO getOrganization() {
        return organization;
    }

    public void setOrganization(AppUserDTO organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlaceholderDTO)) {
            return false;
        }

        PlaceholderDTO placeholderDTO = (PlaceholderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, placeholderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlaceholderDTO{" +
            "id=" + getId() +
            ", placeholderIndex='" + getPlaceholderIndex() + "'" +
            ", placeholderValue='" + getPlaceholderValue() + "'" +
            ", archetype=" + getArchetype() +
            ", organization=" + getOrganization() +
            "}";
    }
}
