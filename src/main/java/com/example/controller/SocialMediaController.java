package com.example.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.entity.*;
import com.example.exception.AccountException;
import com.example.exception.MessageException;
import com.example.service.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerAccount(@RequestBody Account account){
        try {
            Account createdAccount = accountService.createAccount(account); 
            return ResponseEntity.status(200).body(createdAccount); 
        } 
        catch (AccountException e) {
            String message = e.getMessage(); 
            if (message.equals("Username already exists.")) { 
                return ResponseEntity.status(409).body(message); 
            }else if(message.equals("Username is blank.") ||
            message.equals("Password is less than 4 characters.")){
                return ResponseEntity.status(400).body(message);
            }
        }
        return ResponseEntity.status(401).body("An unknown error occurred");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> loginAccount(@RequestBody Account account){
        try {
            Account loginedInAccount = accountService.login(account); 
            return ResponseEntity.status(200).body(loginedInAccount); 
        } 
        catch (AccountException e) {
            String message = e.getMessage(); 
            if (message.equals("Password is incorrect.") ||
            message.equals("Username doesn't exist.")) { 
                return ResponseEntity.status(401).body(message); 
            }
        }
        return ResponseEntity.status(401).body("An unknown error occurred");
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public ResponseEntity<?> createMessage(@RequestBody Message msg){
        try {
            Message createdmsg = messageService.createMessage(msg); 
            return ResponseEntity.status(200).body(createdmsg); 
        } 
        catch (MessageException e) {
            String message = e.getMessage(); 
            if (message.equals("Message not posted by user.") ||
            message.equals("Message is blank.") ||
            message.equals("Message is longer than 255 characters.")) { 
                return ResponseEntity.status(400).body(message);
            }
        }
        return ResponseEntity.status(401).body("An unknown error occurred");
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMessages(){
        try{
            List<Message> msgs = messageService.getAllMsgs();
            return ResponseEntity.status(200).body(msgs);
        }catch(MessageException e){
            String eMessage = e.getMessage();
            if(eMessage.equals("There are no messages.")){
                return ResponseEntity.status(200).body(null);
            }
        }
        return ResponseEntity.status(401).body("An unknown error occurred");
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageById(@PathVariable int messageId){
        try{
            Message msg = messageService.getMsgById(messageId);
            return ResponseEntity.status(200).body(msg);
        }catch(MessageException e){
            String eMessage = e.getMessage();
            if(eMessage.equals("Message does not exist.")){
                return ResponseEntity.status(200).body(null);
            }
        }
        return ResponseEntity.status(401).body("An unknown error occurred");
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMessageById(@PathVariable int messageId){
        try{
            int rowsAffected = messageService.deleteMsgById(messageId);
            return ResponseEntity.status(200).body(rowsAffected);
        }catch(MessageException e){
            String eMessage = e.getMessage();
            if(eMessage.equals("Message does not exist to delete.")){
                return ResponseEntity.status(200).body(null);
            }
        }
        return ResponseEntity.status(401).body("An unknown error occurred");
    }

    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Message messageText){
        try{
            String newMessageText = messageText.getMessageText();
            int rowsAffected = messageService.updateMessage(messageId, newMessageText);
            return ResponseEntity.status(200).body(rowsAffected);
        }catch(MessageException e){
            String eMessage = e.getMessage();
            if (eMessage.equals("Message does not exist to update.") ||
            eMessage.equals("New message is blank.") ||
            eMessage.equals("New message is longer than 255 characters.")) { 
                return ResponseEntity.status(400).body(eMessage);
            }
        }
        return ResponseEntity.status(400).body("An unknown error occurred");
    }

    @RequestMapping(value = "/accounts/{accountId}/messages", method = RequestMethod.GET)
    public ResponseEntity<?> getAllMessagesFromUser(@PathVariable int accountId){
        try{
            List<Message> messageList = messageService.getAllMsgsFromUser(accountId);
            return ResponseEntity.status(200).body(messageList);
        }catch(MessageException e){
            String eMessage = e.getMessage();
            if(eMessage.equals("No messages from user.")){
                return ResponseEntity.status(200).body(null);
            }
        }
        return ResponseEntity.status(401).body("An unknown error occurred");

    }
}