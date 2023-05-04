package ru.practicum.ewm.entity.mapper;


import ru.practicum.ewm.entity.Comment;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.entity.dto.CommentDto;
import ru.practicum.ewm.entity.dto.CommentRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static CommentDto toDto(Comment obj) {
        return CommentDto.builder()
                .id(obj.getId())
                .text(obj.getText())
                .author(obj.getId())
                .created(obj.getCreated())
                .build();
    }

    public static Comment toObject(CommentRequestDto commentRequestDto, Event event, User author) {
        Comment obj = new Comment();
        obj.setText(commentRequestDto.getText());
        obj.setEvent(event);
        obj.setAuthor(author);
        obj.setCreated(LocalDateTime.now());
        return obj;
    }

    public static List<CommentDto> toDtos(List<Comment> objs) {
        return objs.stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }
}
