package com.company.PersonalBlog.service.mapper;

import com.company.PersonalBlog.dto.UsersDto;
import com.company.PersonalBlog.models.Users;
import com.company.PersonalBlog.repository.UserRepository;
import com.company.PersonalBlog.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;


@Mapper(componentModel = "spring")

public abstract class UserMapper {

    @Autowired
    @Lazy
    protected UserServiceImpl userService;

    @Mapping(target = "message", ignore = true)
    @Mapping(target = "comments", ignore = true)
    public abstract Users toEntity(UsersDto dto);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "message", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Named(value = "toDto")
    public abstract UsersDto toDto(Users entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Users updateUser(@MappingTarget Users entity, UsersDto dto);


    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<UsersDto> toDtoUserList(List<Users> entities);


    @Mapping(target = "comments", expression = "java(this.userService.getCommentsByUserId(entity.getId()))")
    @Mapping(target = "message", expression = "java(this.userService.getMessagesByUserId(entity.getId()))")
    public abstract UsersDto toDtoWithAllEntityUserList(Users entity);

}
