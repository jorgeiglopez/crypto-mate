package com.nacho.cryptomate;

import com.nacho.cryptomate.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@ServletComponentScan
public class CryptomateApplication {

    @Autowired
    private AppHomeHandler appHomeHandler;

    @Autowired
    private MentionHandler mentionHandler;

    @Autowired
    private MiddlemanDebugHandler debugHandler;

    @Autowired
    private SlackCommandHandler commandHandler;

    @Autowired
    private GlobalShortcutHandler globalShortcutHandler;

    public static void main(String[] args) {
        SpringApplication.run(CryptomateApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        appHomeHandler.initHandler();
        mentionHandler.initHandler();
        debugHandler.initHandler();
        commandHandler.initHandler();
        globalShortcutHandler.initHandler();
    }
}
