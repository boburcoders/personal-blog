package com.company.PersonalBlog.service.mapper;

import com.company.PersonalBlog.dto.UsersDto;
import com.company.PersonalBlog.models.Users;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "message", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Users toEntity(UsersDto dto);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "message", ignore = true)
    @Mapping(target = "comments", ignore = true)
    UsersDto toDto(Users entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Users updateUser(@MappingTarget Users entity, UsersDto dto);




}
