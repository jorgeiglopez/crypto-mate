package com.nacho.cryptomate.handler;

import com.google.gson.Gson;
import com.slack.api.bolt.App;
import com.slack.api.bolt.response.Response;
import com.slack.api.methods.response.views.ViewsOpenResponse;
import com.slack.api.methods.response.views.ViewsUpdateResponse;
import com.slack.api.model.view.View;
import com.slack.api.model.view.ViewState;
import com.slack.api.util.json.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.plainTextInput;
import static com.slack.api.model.block.element.BlockElements.staticSelect;
import static com.slack.api.model.view.Views.*;

@Slf4j
@Service
public class GlobalShortcutHandler {

    @Autowired
    App app;

    //    Basically it’s the same with Interactive Components but the only difference is that a payload coming from a modal has view
    //    and also its private_metadata

    //    app.viewClosed("meeting-arrangement", (req, ctx) -> {
    //        // Do something where
    //        return ctx.ack();
    //    });
    //
    //    app.viewSubmission("meeting-arrangement", (req, ctx) -> {
    //        // Sent inputs: req.getPayload().getView().getState().getValues()
    //        return ctx.ack();
    //    });
    //

    //    No GlobalShortcutHandler registered for callback_id: crypto_prices
    public void initHandler() {
        app.globalShortcut("crypto_prices", (req, ctx) -> {
            ViewsOpenResponse viewsOpenRes = ctx.client().viewsOpen(r -> r
                    .triggerId(ctx.getTriggerId())
                    .view(getMeetingArrangementModal()));
            if (viewsOpenRes.isOk()) {
                return ctx.ack();
            } else {
                return Response.builder().statusCode(500).body(viewsOpenRes.getError()).build();
            }
        });

        app.blockAction("category-selection-action", (req, ctx) -> {
            String categoryId = req.getPayload().getActions().get(0).getSelectedOption().getValue();
            View currentView = req.getPayload().getView();
            String privateMetadata = currentView.getPrivateMetadata();
            View viewForTheCategory = buildViewByCategory(categoryId, privateMetadata);
            ViewsUpdateResponse viewsUpdateResp = ctx.client().viewsUpdate(r -> r
                    .viewId(currentView.getId())
                    .hash(currentView.getHash())
                    .view(viewForTheCategory)
            );
            return ctx.ack();
        });

        // when a user clicks "Submit"
        app.viewSubmission("meeting-arrangement", (req, ctx) -> {
            String privateMetadata = req.getPayload().getView().getPrivateMetadata();
            Map<String, Map<String, ViewState.Value>> stateValues = req.getPayload().getView().getState().getValues();
            String agenda = stateValues.get("agenda-block").get("agenda-action").getValue();
            Map<String, String> errors = new HashMap<>();
            if (agenda.length() <= 10) {
                errors.put("agenda-block", "Agenda needs to be longer than 10 characters.");
            }
            if (!errors.isEmpty()) {
                return ctx.ack(r -> r.responseAction("errors").errors(errors));
            } else {
                // TODO: may store the stateValues and privateMetadata
                // Responding with an empty body means closing the modal now.
                // If your app has next steps, respond with other response_action and a modal view.
                return ctx.ack();
            }
        });
    }

    View buildViewByCategory(String categoryId, String privateMetadata) {
        log.debug("[DEBUG] buildViewByCategory for categoryId: {} and privateMetadata: {}", categoryId, privateMetadata);

        Gson gson = GsonFactory.createSnakeCase();
        Map<String, String> metadata = gson.fromJson(privateMetadata, Map.class);
        metadata.put("categoryId", categoryId);
        String updatedPrivateMetadata = gson.toJson(metadata);
        return view(view -> view
                .callbackId("meeting-arrangement")
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title -> title.type("plain_text").text("Meeting Arrangement").emoji(true)))
                .submit(viewSubmit(submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(close -> close.type("plain_text").text("Cancel").emoji(true)))
                .privateMetadata(updatedPrivateMetadata)
                .blocks(asBlocks(
                        section(section -> section.blockId("category-block").text(markdownText("You've selected \"" + categoryId + "\""))),
                        input(input -> input
                                .blockId("agenda-block")
                                .element(plainTextInput(pti -> pti.actionId("agenda-action").multiline(true)))
                                .label(plainText(pt -> pt.text("Detailed Agenda").emoji(true)))
                        )
                ))
        );
    }

    private View getMeetingArrangementModal() {
        return View.builder()
                .callbackId("meeting-arrangement")
                .type("modal")
                .notifyOnClose(true)
                .title(viewTitle(title -> title.type("plain_text").text("Meeting Arrangement").emoji(true)))
                .submit(viewSubmit(submit -> submit.type("plain_text").text("Submit").emoji(true)))
                .close(viewClose(close -> close.type("plain_text").text("Cancel").emoji(true)))
                // To send private info to the modal, it's always a String.
                // Another option would be using: JsonOps.toJsonString(POJO)
                .privateMetadata("{\"response_url\":\"https://hooks.slack.com/actions/T1ABCD2E12/330361579271/0dAEyLY19ofpLwxqozy3firz\"}")
                .blocks(asBlocks(
                        section(section -> section
                                .blockId("category-block")
                                .text(markdownText("Select a category of the meeting:"))
                                .accessory(staticSelect(staticSelect -> staticSelect
                                        .actionId("category-selection-action")
                                        .placeholder(plainText("Select a category"))
                                        .options(asOptions(
                                                option(plainText("Customer"), "customer"),
                                                option(plainText("Partner"), "partner"),
                                                option(plainText("Internal"), "internal")
                                        ))
                                ))
                        ),
                        input(input -> input
                                .blockId("agenda-block")
                                .element(plainTextInput(pti -> pti.actionId("agenda-action").multiline(true)))
                                .label(plainText(pt -> pt.text("Detailed Agenda:").emoji(true)))
                        )
                ))
                .build();
    }
}
