package com.nacho.cryptomate.handler;

import com.slack.api.bolt.App;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import com.slack.api.model.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AppHomeHandler { // EVENT SUBSCRIPTION

    @Autowired
    App app;

    public void initHandler(){
        app.event(AppHomeOpenedEvent.class, ((eventsApiPayload, eventContext) -> {
            View myView = Views.view(viewBuilder -> viewBuilder
                    .type("home")
                    .blocks(Collections.singletonList(
                            DividerBlock.builder().build()))
            );
            ViewsPublishResponse response = eventContext.client().viewsPublish(viewsPublishRequestBuilder ->
                    viewsPublishRequestBuilder
                            .userId(eventsApiPayload.getEvent().getUser())
                            .view(myView));

            return eventContext.ack();
        }));
    }

}
