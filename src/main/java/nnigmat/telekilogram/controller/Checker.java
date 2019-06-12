package nnigmat.telekilogram.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Checker {

    private String message;
    private Map<String, String> patterns = new HashMap<String, String>() {{
        put("help", "//help");

        put("room_public_create", "//room create \"[^\"]+\"");
        put("room_private_create", "//room create -c \"[^\"]+\"");
        put("room_remove", "//room remove \"[^\"]+\"");
        put("room_rename", "//room rename \"[^\"]+\"");
        put("room_self_connect", "//room connect \"[^\"]+\"");
        put("room_user_connect", "//room connect \"[^\"]+\" -l \"[^\"]+\"");
        put("room_disconnect", "//room disconnect");
        put("room_specified_disconnect", "//room disconnect \"[^\"]+\"");
        put("room_user_disconnect", "//room disconnect \"[^\"]+\" -l \"[^\"]+\"");
        put("room_user_with_time_disconnect", "//room disconnect \"[^\"]+\" -l \"[^\"]+\" -m \\d+");

        put("user_rename", "//user rename \"[^\"]+\" \"[^\"]+\"");
        put("user_ban", "//user ban \"[^\"]+\" -l \"[^\"]+\" -m  \\d+");
        put("user_make_moderator", "//user moderator -n \"[^\"]+\"");
        put("user_unmake_moderator", "//user moderator -d \"[^\"]+\"");
    }};


    public Checker (String message) {
        this.message = message.trim();
    }

    public boolean isEmpty() {
        return this.message.equals("");
    }

    public boolean isCommand() {
        return this.message.startsWith("//");
    }

    public String checkCommand() {
        for (Map.Entry<String, String> entry : patterns.entrySet()) {
            if (this.message.matches(entry.getValue())) {
                return entry.getKey();
            }
        }
        return "";
    }

}
