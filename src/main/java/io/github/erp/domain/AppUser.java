package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @Transient
    private User systemUser;

    @Transient
    @JsonIgnoreProperties(value = { "archetype" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "systemUser", "placeholders", "organization" }, allowSetters = true)
    private AppUser organization;

    @Column("system_user_id")
    private Long systemUserId;

    @Column("organization_id")
    private Long organizationId;

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

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public AppUser placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public AppUser addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public AppUser removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
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

    public Long getSystemUserId() {
        return this.systemUserId;
    }

    public void setSystemUserId(Long user) {
        this.systemUserId = user;
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(Long appUser) {
        this.organizationId = appUser;
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
            "}";
    }
}
