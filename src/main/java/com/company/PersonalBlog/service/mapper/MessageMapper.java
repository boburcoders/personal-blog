package com.company.PersonalBlog.service.mapper;

import com.company.PersonalBlog.dto.CommentDto;
import com.company.PersonalBlog.dto.MessageDto;
import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.models.Message;
import com.company.PersonalBlog.service.impl.MessageServiceImpl;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class MessageMapper {
    @Autowired
    @Lazy
    protected MessageServiceImpl messageService;

    @Mapping(target = "comments", ignore = true)
    public abstract Message ToEntity(MessageDto messageDto);

    @Mapping(target = "messageId", source = "id")
    @Mapping(target = "comments", ignore = true)
    @Named(value = "toDto")
    public abstract MessageDto ToDto(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Message updateAllFields(@MappingTarget Message message, MessageDto messageDto);

    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<MessageDto> toDtoMessageList(List<Message> entities);

    @Mapping(target = "comments", expression = "java(this.messageService.getCommentsByMessageId(entity.getId()))")
    public abstract MessageDto getMessageDtoAllFields(Message entity);


}
