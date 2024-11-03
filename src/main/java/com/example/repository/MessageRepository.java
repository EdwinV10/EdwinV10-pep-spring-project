package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository  extends JpaRepository<Message, Integer> {
    public Message findByPostedBy(Integer accId);
    public Message findByMessageId(Integer msgId);
    public int deleteByMessageId(Integer msgId);
    public List<Message> findAllByPostedBy(Integer accId);
}
