package io.github.erp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Placeholder.
 */
@Table("placeholder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Placeholder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("placeholder_index")
    private String placeholderIndex;

    @Column("placeholder_value")
    private String placeholderValue;

    @Transient
    @JsonIgnoreProperties(value = { "archetype" }, allowSetters = true)
    private Placeholder archetype;

    @Column("archetype_id")
    private Long archetypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Placeholder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceholderIndex() {
        return this.placeholderIndex;
    }

    public Placeholder placeholderIndex(String placeholderIndex) {
        this.setPlaceholderIndex(placeholderIndex);
        return this;
    }

    public void setPlaceholderIndex(String placeholderIndex) {
        this.placeholderIndex = placeholderIndex;
    }

    public String getPlaceholderValue() {
        return this.placeholderValue;
    }

    public Placeholder placeholderValue(String placeholderValue) {
        this.setPlaceholderValue(placeholderValue);
        return this;
    }

    public void setPlaceholderValue(String placeholderValue) {
        this.placeholderValue = placeholderValue;
    }

    public Placeholder getArchetype() {
        return this.archetype;
    }

    public void setArchetype(Placeholder placeholder) {
        this.archetype = placeholder;
        this.archetypeId = placeholder != null ? placeholder.getId() : null;
    }

    public Placeholder archetype(Placeholder placeholder) {
        this.setArchetype(placeholder);
        return this;
    }

    public Long getArchetypeId() {
        return this.archetypeId;
    }

    public void setArchetypeId(Long placeholder) {
        this.archetypeId = placeholder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Placeholder)) {
            return false;
        }
        return id != null && id.equals(((Placeholder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Placeholder{" +
            "id=" + getId() +
            ", placeholderIndex='" + getPlaceholderIndex() + "'" +
            ", placeholderValue='" + getPlaceholderValue() + "'" +
            "}";
    }
}
