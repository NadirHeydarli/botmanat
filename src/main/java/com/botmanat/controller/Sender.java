package com.botmanat.controller;

import com.botmanat.config.app.KeySingleton;
import com.botmanat.fb.Button;
import com.botmanat.fb.OutgoingMessage;
import com.botmanat.fb.QuickReply;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

@Log
public class Sender {

    private static boolean sendMessage(OutgoingMessage.MessageContainer messageContainer, String recipientId) {
        OutgoingMessage outgoingMessage = new OutgoingMessage();
        outgoingMessage.setRecipient(recipientId);
        outgoingMessage.setMessage(messageContainer);

        try {
            URL url = new URL("https://graph.facebook.com/v2.6/me/messages?access_token=" + KeySingleton.getInstance().getPageAccessToken());
            URLConnection con = url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setConnectTimeout(60000);
            http.setReadTimeout(60000);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), Charset.forName("UTF-8"));

            //JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(outgoingMessage);
            //JSON

            writer.write(jsonString);
            writer.flush();
            if (http.getResponseCode() != 200) {
                log.info(String.valueOf(http.getResponseCode()));
                log.info(http.getResponseMessage());
            }

            writer.close();

        } catch (Exception e) {
            log.severe(e.getMessage());
        }


        return true;

    }

    public static boolean sendTextMessage(String message, String recipientId) {
        return sendMessage(new OutgoingMessage.MessageContainer(message), recipientId);
    }

    public static boolean sendMessageWithQuickReplies(String message, List<QuickReply> quickReplies, String recipientId) {
        return sendMessage(new OutgoingMessage.MessageContainer(message, quickReplies), recipientId);
    }

    public static boolean sendMessageWithButtons(String message, List<Button> buttonsList, String recipientId) {
        OutgoingMessage.MessageAttachmentPayload messageAttachmentPayload = new OutgoingMessage.MessageAttachmentPayload();
        messageAttachmentPayload.setText(message);
        messageAttachmentPayload.setButtons(buttonsList);
        OutgoingMessage.MessageAttachment messageAttachment = new OutgoingMessage.MessageAttachment();
        messageAttachment.setPayload(messageAttachmentPayload);
        return sendMessage(new OutgoingMessage.MessageContainer(messageAttachment), recipientId);
    }
}
