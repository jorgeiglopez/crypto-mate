package com.nacho.cryptomate.handler;

import com.slack.api.bolt.App;
import com.slack.api.methods.response.views.ViewsPublishResponse;
import com.slack.api.model.event.AppHomeOpenedEvent;
import com.slack.api.model.view.View;
import com.slack.api.model.view.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

@Service
public class AppHomeHandler {

    @Autowired
    App app;

    public void initHandler() {
        app.event(AppHomeOpenedEvent.class, ((eventsApiPayload, eventContext) -> {
            ZonedDateTime now = ZonedDateTime.now();
            View myView = Views.view(viewBuilder -> viewBuilder
                    .type("home")
                    .blocks(asBlocks(
                            section(section -> section.text(markdownText(mt -> mt.text(":wave: Hello, App Home! (Last updated: " + now + ")")))),
                            image(img -> img.imageUrl("https://thumbs.dreamstime.com/b/cute-monster-face-square-avatar-vector-stock-cute-monster-face-square-avatar-114650081.jpg")
                                    .altText("leanmote").imageHeight(200).imageWidth(200))

                    ))
            );
            ViewsPublishResponse response = eventContext.client().viewsPublish(viewsPublishRequestBuilder ->
                    viewsPublishRequestBuilder
                            .userId(eventsApiPayload.getEvent().getUser())
                            .view(myView));

            return eventContext.ack();
        }));
    }

}
