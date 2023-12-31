package com.rikerik.BookWave.Controller;

import com.rikerik.BookWave.DAO.UserRepository;
import com.rikerik.BookWave.Model.Book;
import com.rikerik.BookWave.Model.ChatMessage;
import com.rikerik.BookWave.Model.User;
import com.rikerik.BookWave.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatController {

    private final UserRepository userRepository;


    @Autowired
    private CustomUserDetailsService userService;

    public ChatController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @MessageMapping("/scifi/register")
    @SendTo("/topic/scifi")
    public ChatMessage registerForScifi(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/scifi/send")
    @SendTo("/topic/scifi")
    public ChatMessage sendScifiMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/scifi/leave")
    @SendTo("/topic/scifi")
    public ChatMessage leaveScifiChat(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/fantasy/register")
    @SendTo("/topic/fantasy")
    public ChatMessage registerForFantasy(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/fantasy/send")
    @SendTo("/topic/fantasy")
    public ChatMessage sendFantasyMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/fantasy/leave")
    @SendTo("/topic/fantasy")
    public ChatMessage leaveFantasyChat(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @GetMapping("/chatScifi")
    public String scifiIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());


        List<Book> userBooks = userService.getUserBooks(user);

        if (!userService.hasEnoughScifiBooks(userBooks)) {
            // Redirect or handle the case where the user doesn't have enough fantasy books
            return "redirect:/library";
        }

        String username = auth.getName();
        model.addAttribute("username", username);
        model.addAttribute("topic", "scifi");
        return "chatScifi";
    }

    @GetMapping("/chatFantasy")
    public String fantasyIndex(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        List<Book> userBooks = userService.getUserBooks(user);

        if (!userService.hasEnoughFantasyBooks(userBooks)) {
            // Redirect or handle the case where the user doesn't have enough fantasy books
            return "redirect:/library";
        }

        String username = auth.getName();
        model.addAttribute("username", username);
        model.addAttribute("topic", "fantasy");

        return "chatFantasy";
    }
}


