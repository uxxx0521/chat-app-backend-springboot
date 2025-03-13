package org.example.chatserver.service;

import org.example.chatserver.model.ChatMessage;
import org.example.chatserver.model.User;
import org.example.chatserver.repository.ChatMessageRepository;
import org.example.chatserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;

    public void saveMessage(String sender, String content, String timestamp) {
        chatMessageRepository.save(new ChatMessage(sender, content, timestamp));
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }
    public String handleLogin(String username, String password){
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return "success";
        } else {
            return "fail";
        }
    }
    public String handleRegister(String username, String password, String email, String nickname){

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return "Enter different username";
        } else{

            User newUser = new User(username, password, email, nickname);

            userRepository.save(newUser);
            return "success";
        }
    }
}
