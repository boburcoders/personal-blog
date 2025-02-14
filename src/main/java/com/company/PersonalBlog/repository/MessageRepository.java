package com.company.PersonalBlog.repository;

import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("select c from Comment c where c.message.id = ?1")
    List<Comment> getCommentsByMessageId(Integer messageId);
}
