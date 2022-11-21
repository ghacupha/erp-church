package io.github.erp.service.mapper;

import io.github.erp.domain.Placeholder;
import io.github.erp.service.dto.PlaceholderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Placeholder} and its DTO {@link PlaceholderDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlaceholderMapper extends EntityMapper<PlaceholderDTO, Placeholder> {
    @Mapping(target = "archetype", source = "archetype", qualifiedByName = "placeholderPlaceholderValue")
    PlaceholderDTO toDto(Placeholder s);

    @Named("placeholderPlaceholderValue")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "placeholderValue", source = "placeholderValue")
    PlaceholderDTO toDtoPlaceholderPlaceholderValue(Placeholder placeholder);
}
