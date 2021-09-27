package com.nacho.cryptomate.handler;

import com.slack.api.bolt.App;
import com.slack.api.bolt.response.Response;
import com.slack.api.bolt.util.JsonOps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;


class DebugResponseBody {
    String responseType; // ephemeral || in_channel
    String text;
}

@Slf4j
@Service
public class MiddlemanDebugHandler {

    @Autowired
    App app;

    String debugMode = System.getenv("SLACK_APP_DEBUG_MODE");

    public void initHandler() {
        if (debugMode != null && debugMode.equals("1")) {
            log.warn("SLACK_APP_DEBUG_MODE enabled. If you haven't deliberately turn it ON, please switch it of by removing the env variable: 'SLACK_APP_DEBUG_MODE'.");
            app.use((req, _resp, chain) -> {
                log.info("MIDDLEMAN - REQUEST: ", req.toString());
                Response resp = chain.next(req);
                if (resp.getStatusCode() != 200) {
                    resp.getHeaders().put("content-type", Arrays.asList(resp.getContentType()));
                    // dump all the headers as a single string
                    String headers = resp.getHeaders().entrySet().stream()
                            .map(e -> e.getKey() + ": " + e.getValue() + "\n").collect(joining());

                    // set an ephemeral message with useful information
                    DebugResponseBody body = new DebugResponseBody();
                    body.responseType = "ephemeral";
                    body.text =
                            ":warning: *[DEBUG MODE] Something is technically wrong* :warning:\n" +
                                    "Below is a response the Slack app was going to send...\n" +
                                    "*Status Code*: " + resp.getStatusCode() + "\n" +
                                    "*Headers*: ```" + headers + "```" + "\n" +
                                    "*Body*: ```" + resp.getBody() + "```";
                    resp.setBody(JsonOps.toJsonString(body));

                    resp.setStatusCode(200);
                }
                return resp;
            });
        }
    }

}
