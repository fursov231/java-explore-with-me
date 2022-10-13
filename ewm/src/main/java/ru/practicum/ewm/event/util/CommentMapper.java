package ru.practicum.ewm.event.util;

import ru.practicum.ewm.event.dto.CommentRequestDto;
import ru.practicum.ewm.event.dto.CommentResponseDto;
import ru.practicum.ewm.event.dto.UpdateCommentDto;
import ru.practicum.ewm.event.model.Comment;
import ru.practicum.ewm.user.util.UserMapper;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentResponseDto toResponseDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .event(EventMapper.toShortDto(comment.getEvent()))
                .author(UserMapper.toDto(comment.getAuthor()))
                .created(comment.getCreated())
                .build();
    }

    //+user, event
    public static Comment toEvent(CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .text(commentRequestDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    //+user, event
    public static Comment toEvent(UpdateCommentDto updateCommentDto) {
        return Comment.builder()
                .id(updateCommentDto.getId())
                .text(updateCommentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

}
