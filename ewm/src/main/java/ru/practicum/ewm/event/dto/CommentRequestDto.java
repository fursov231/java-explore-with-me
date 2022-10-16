package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank
    @Size(max = 1000)
    private String text;

    private Long authorId;
    private Long eventId;
}
