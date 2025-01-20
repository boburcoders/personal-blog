package com.company.PersonalBlog.service.mapper;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.models.Comment;
import org.mapstruct.*;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentDto dto);

    @Mapping(target = "commentId", source = "id")
    CommentDto toDto(Comment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment updateAllFields(@MappingTarget Comment entity, CommentDto dto);
}
