package com.azkivam.banksystem;

import com.azkivam.banksystem.console.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BankSystemApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BankSystemApplication.class, args);
        Console console = applicationContext.getBean(Console.class);
        console.runConsole();
    }

}
