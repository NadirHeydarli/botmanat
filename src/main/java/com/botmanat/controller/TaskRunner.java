package com.botmanat.controller;

import com.botmanat.config.app.KeySingleton;
import com.botmanat.model.OfyHelper;
import com.botmanat.model.Subscriber;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import lombok.extern.java.Log;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Log
public class TaskRunner {
    private static String SENDER_ID = "senderId";

    public static void putSubscriberNameTaskIntoQueue(String senderId) {
        run("/tasks/subscriber-name-fetcher", SENDER_ID, senderId);
    }

    private static void run(String url, String key, String value) {
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl(url).param(key, value));
    }

    public static Response getSubscriberNameTask(HttpServletRequest request) {
        String senderId = getKeyValue(request, SENDER_ID);
        if (senderId == null) {
            log.warning("SenderId was null in a task");
            return Response.ok().build();
        }

        Map<String, String> jsonMap = null;
        URLConnection con = URLHelper.getUrlConnection("https://graph.facebook.com/v2.6/" + senderId + "?fields=first_name,last_name&access_token=" + KeySingleton.getInstance().getPageAccessToken());
        if (con == null) {
            return Response.ok().build();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonMap = mapper.readValue(con.getInputStream(), new TypeReference<HashMap<String, String>>() {
            });
        } catch (MalformedURLException | JsonMappingException | JsonParseException e) {
            log.warning("Couldnt fetch username/surname from facebook graph api " + e.getMessage());
        } catch (IOException e) {
            log.warning("IOException Couldnt fetch username/surname from facebook graph api " + e.getMessage());
        }

        Subscriber subscriber = OfyHelper.getById(Subscriber.class, senderId);
        if (subscriber != null && jsonMap != null && jsonMap.containsKey("first_name") && jsonMap.containsKey("last_name")) {
            subscriber.setFirstName(jsonMap.get("first_name"));
            subscriber.setLastName(jsonMap.get("last_name"));
            OfyHelper.save(subscriber);
            return Response.ok().build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    private static String getKeyValue(HttpServletRequest request, String key) {
        if (!Strings.isNullOrEmpty(request.getParameter(key))) {
            return request.getParameter(key);
        } else {
            log.warning("Unable to run task, required key was not provided");
            return null;
        }
    }
}
