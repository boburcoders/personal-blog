package com.company.PersonalBlog.service.mapper;

import com.company.PersonalBlog.dto.MessageDto;
import com.company.PersonalBlog.models.Message;
import org.mapstruct.*;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "comments", ignore = true)
    Message ToEntity(MessageDto messageDto);

    @Mapping(target = "messageId", source = "id")
    @Mapping(target = "comments", ignore = true)
    MessageDto ToDto(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Message updateAllFields(@MappingTarget Message message, MessageDto messageDto);
}
