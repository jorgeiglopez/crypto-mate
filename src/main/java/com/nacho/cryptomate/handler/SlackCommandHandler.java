package com.nacho.cryptomate.handler;

import com.nacho.cryptomate.helper.PriceFetcher;
import com.slack.api.bolt.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlackCommandHandler { // SLASH COMMANDS

    @Autowired
    App app;

    @Autowired
    PriceFetcher priceFetcher;

    public void initHandler(){

        app.command("/hello", (req, ctx) ->
                ctx.ack(r -> r.text("Hi there, welcome to Crypto-Mate! For the latest prices type in: '/crypto' ")));

        app.command("/crypto", (req, ctx) ->
                ctx.ack(r -> r.text(priceFetcher.getAllPrices("USD"))));
    }
}
