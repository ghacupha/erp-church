package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A AppUser.
 */
@Table("app_user")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appuser")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("designation")
    private String designation;

    @NotNull(message = "must not be null")
    @Column("identifier")
    private UUID identifier;

    @Column("is_corporate_account")
    private Boolean isCorporateAccount;

    @Transient
    @JsonIgnoreProperties(value = { "organization", "systemUser" }, allowSetters = true)
    private AppUser organization;

    @Transient
    private User systemUser;

    @Column("organization_id")
    private Long organizationId;

    @Column("system_user_id")
    private Long systemUserId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public AppUser designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public AppUser identifier(UUID identifier) {
        this.setIdentifier(identifier);
        return this;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public Boolean getIsCorporateAccount() {
        return this.isCorporateAccount;
    }

    public AppUser isCorporateAccount(Boolean isCorporateAccount) {
        this.setIsCorporateAccount(isCorporateAccount);
        return this;
    }

    public void setIsCorporateAccount(Boolean isCorporateAccount) {
        this.isCorporateAccount = isCorporateAccount;
    }

    public AppUser getOrganization() {
        return this.organization;
    }

    public void setOrganization(AppUser appUser) {
        this.organization = appUser;
        this.organizationId = appUser != null ? appUser.getId() : null;
    }

    public AppUser organization(AppUser appUser) {
        this.setOrganization(appUser);
        return this;
    }

    public User getSystemUser() {
        return this.systemUser;
    }

    public void setSystemUser(User user) {
        this.systemUser = user;
        this.systemUserId = user != null ? user.getId() : null;
    }

    public AppUser systemUser(User user) {
        this.setSystemUser(user);
        return this;
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(Long appUser) {
        this.organizationId = appUser;
    }

    public Long getSystemUserId() {
        return this.systemUserId;
    }

    public void setSystemUserId(Long user) {
        this.systemUserId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", isCorporateAccount='" + getIsCorporateAccount() + "'" +
            "}";
    }
}
