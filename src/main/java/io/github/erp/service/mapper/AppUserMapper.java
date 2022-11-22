package io.github.erp.service.mapper;

import io.github.erp.domain.AppUser;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.User;
import io.github.erp.service.dto.AppUserDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "systemUser", source = "systemUser", qualifiedByName = "userLogin")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "placeholderPlaceholderValueSet")
    @Mapping(target = "organization", source = "organization", qualifiedByName = "appUserDesignation")
    AppUserDTO toDto(AppUser s);

    @Mapping(target = "removePlaceholder", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("placeholderPlaceholderValue")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "placeholderValue", source = "placeholderValue")
    PlaceholderDTO toDtoPlaceholderPlaceholderValue(Placeholder placeholder);

    @Named("placeholderPlaceholderValueSet")
    default Set<PlaceholderDTO> toDtoPlaceholderPlaceholderValueSet(Set<Placeholder> placeholder) {
        return placeholder.stream().map(this::toDtoPlaceholderPlaceholderValue).collect(Collectors.toSet());
    }

    @Named("appUserDesignation")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "designation", source = "designation")
    AppUserDTO toDtoAppUserDesignation(AppUser appUser);
}
