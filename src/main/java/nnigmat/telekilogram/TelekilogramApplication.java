package nnigmat.telekilogram;

import nnigmat.telekilogram.controller.YBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class TelekilogramApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TelekilogramApplication.class, args);
    }

}
