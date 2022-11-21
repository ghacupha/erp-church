package io.github.erp.service.dto;

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
            "}";
    }
}
