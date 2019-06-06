package nnigmat.telekilogram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/room")
public class RoomController {

    private ArrayList<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{
            put("user", "user1");
            put("text", "Good morning");
        }});
        add(new HashMap<String, String>() {{
            put("user", "user2");
            put("text", "Good day");
        }});
        add(new HashMap<String, String>() {{
            put("user", "user3");
            put("text", "Good evening");
        }});
    }};

    @GetMapping
    public String list(Model model) {
        model.addAttribute("messages", messages);

        return "list";
    }

    @PostMapping
    public String addMessage(
            @RequestParam(required = true, defaultValue = "") String user,
            @RequestParam(required = true, defaultValue = "") String text,
            Model model) {
        messages.add(new HashMap<String, String>() {{
            put("user", user);
            put("text", text);
        }});

        model.addAttribute("messages", messages);

        return "list";
    }
}
