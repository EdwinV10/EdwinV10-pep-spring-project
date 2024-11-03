package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.MessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message msg){
        if(accountRepository.findByAccountId(msg.getPostedBy()) == null){
            throw new MessageException("Message not posted by user.");
        }
        if(msg.getMessageText().isEmpty()){
            throw new MessageException("Message is blank.");
        }
        if(msg.getMessageText().length() > 255){
            throw new MessageException("Message is longer than 255 characters.");
        }
        return messageRepository.save(msg);
    }

    public List<Message> getAllMsgs(){
        if(messageRepository.findAll() == null){
            throw new MessageException("There are no messages.");
        }
        return messageRepository.findAll();
    }

    public Message getMsgById(int id){
        if(messageRepository.findByMessageId(id) == null){
            throw new MessageException("Message does not exist.");
        }
        return messageRepository.findByMessageId(id);
    }

    @Transactional
    public int deleteMsgById(int id){
        int rowAffected = messageRepository.deleteByMessageId(id);
        if(rowAffected == 0){
            throw new MessageException("Message does not exist to delete.");
        }else{
            return rowAffected;
        }
    }

    public int updateMessage(int id, String newMessageTxt){
        Message msg = messageRepository.findByMessageId(id);
        if(newMessageTxt.isBlank()){
            throw new MessageException("New message is blank.");
        }
        if(newMessageTxt.length() > 255){
            throw new MessageException("New message is longer than 255 characters.");
        }
        if(msg == null){
            throw new MessageException("Message does not exist to update.");
        }else{
            msg.setMessageText(newMessageTxt);
            messageRepository.save(msg);
            return 1;
        }
    }

    public List<Message> getAllMsgsFromUser(int id){
        if(messageRepository.findAllByPostedBy(id) != null){
            return messageRepository.findAllByPostedBy(id);
        }else{
            throw new MessageException("No messages from user.");
        }
    }
}
