package nnigmat.telekilogram.controller;

import java.util.HashMap;
import java.util.Map;

public class CommandsController {

    public static String executeCommand(String command) {
        command = command.trim();
        String[] temp = command.split(" ");

        String res = "";
        switch (temp[0]) {
            case ("//room"):
                res = roomCommands(temp);
                break;
            case ("//user"):
                res = userCommands(temp);
                break;
            default:
                res = "No such command";
        }

        return res;
    }

    private static String roomCommands(String[] command) {
        switch (command[1]) {
            case ("create"):
            case ("remove"):

            case ("rename"):

            case ("connect"):

            case ("disconnect"):

            default:
        }
        return "";
    }

    private static String userCommands(String[] command) {
        switch (command[1]) {
            case ("rename"):
            case ("ban"):
            case ("moderator"):
            default:
        }
        return "";
    }
}
