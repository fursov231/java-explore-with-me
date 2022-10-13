package ru.practicum.ewm.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
