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
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AppUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String designation;

    @NotNull(message = "must not be null")
    private UUID identifier;

    private Boolean isCorporateAccount;

    private AppUserDTO organization;

    private UserDTO systemUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public Boolean getIsCorporateAccount() {
        return isCorporateAccount;
    }

    public void setIsCorporateAccount(Boolean isCorporateAccount) {
        this.isCorporateAccount = isCorporateAccount;
    }

    public AppUserDTO getOrganization() {
        return organization;
    }

    public void setOrganization(AppUserDTO organization) {
        this.organization = organization;
    }

    public UserDTO getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(UserDTO systemUser) {
        this.systemUser = systemUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUserDTO)) {
            return false;
        }

        AppUserDTO appUserDTO = (AppUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserDTO{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", isCorporateAccount='" + getIsCorporateAccount() + "'" +
            ", organization=" + getOrganization() +
            ", systemUser=" + getSystemUser() +
            "}";
    }
}
