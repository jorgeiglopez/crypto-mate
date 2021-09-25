package com.nacho.cryptomate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

@Configuration
public class SlackApp {

    @Bean
    public AppConfig loadAppConfig() {
        AppConfig config = new AppConfig();
        ClassLoader classLoader = SlackApp.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("appConfig.json");
                InputStreamReader isr = new InputStreamReader(is)) {
            String json = new BufferedReader(isr).lines().collect(joining());
            JsonObject j = new Gson().fromJson(json, JsonElement.class).getAsJsonObject();
            config.setSigningSecret(j.get("signingSecret").getAsString());
            config.setSingleTeamBotToken(j.get("singleTeamBotToken").getAsString());
        } catch (IOException e) {
            System.out.println("------------------------ UPS, AN EXCEPTION!!! ------------------------");
            System.out.println(e.getMessage());
        }
        return config;
    }

    @Bean
    public App initSlackApp(AppConfig config) {
        App app = new App(config);
        app.command("/hello", (req, ctx) -> ctx.ack(r -> r.text("Hi there, welcome to Crypto-Mate! For the latest prices type in: '/crypto' ")));
        app.command("/crypto", (req, ctx) -> ctx.ack(r -> r.text("The current price is: EXPENSIVE!")));
        return app;
    }
}

