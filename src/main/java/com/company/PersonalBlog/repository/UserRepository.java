package com.company.PersonalBlog.repository;

import com.company.PersonalBlog.models.Comment;
import com.company.PersonalBlog.models.Message;
import com.company.PersonalBlog.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.user.id = ?1")
    List<Comment> findCommentsByUserId(Integer userId);


    @Query("SELECT m FROM Message m WHERE m.user.id = ?1")
    List<Message> findMessagesByUserId(Integer userId);


}
