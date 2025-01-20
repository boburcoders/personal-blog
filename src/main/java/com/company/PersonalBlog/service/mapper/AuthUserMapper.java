package com.company.PersonalBlog.service.mapper;

import com.company.PersonalBlog.dto.AuthUserDto;
import com.company.PersonalBlog.models.AuthUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {

    @Mapping(target = "authId", source = "id")
    AuthUserDto toDto(AuthUser authUser);

    AuthUser toEntity(AuthUserDto authUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AuthUser updateUser(@MappingTarget AuthUser entity, AuthUserDto dto);
}
