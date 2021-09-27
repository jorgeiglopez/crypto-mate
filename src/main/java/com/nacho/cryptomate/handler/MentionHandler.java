package com.nacho.cryptomate.handler;

import com.slack.api.bolt.App;
import com.slack.api.model.event.AppMentionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MentionHandler { // EVENT SUBSCRIPTION

    @Autowired
    App app;

    public void initHandler(){
        app.event(AppMentionEvent.class, (payload, ctx) -> {
            ctx.say("What's up?"); // doesn't work for DM, only channels that it's a member
            return ctx.ack();
        });
    }
}
