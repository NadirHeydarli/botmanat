package com.botmanat.fb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry {

    @Getter
    @Setter
    long id;

    @Getter
    @Setter
    String time;


    @Getter
    @Setter
    List<Messaging> messaging;

    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Messaging {

        @Setter
        EMessagingType type;

        @Getter
        @Setter
        IDContainer sender;

        @Getter
        @Setter
        IDContainer recipient;

        @Getter
        @Setter
        String timestamp;


        /**
         * Text messages sent to bot are stored here
         */
        @Getter
        @Setter
        Message message;

        /**
         * Postbacks occur when a Postback button, Get Started button,
         * Persistent menu or Structured Message is tapped.
         * The payload field in the callback is defined on the button.
         */
        @Getter
        @Setter
        Postback postback;

        /**
         * This callback will occur when a message a page has sent has been delivered.
         * You can subscribe to this callback by selecting
         * the message_deliveries field when setting up your webhook.
         */
        @Getter
        @Setter
        Delivery delivery;

        /**
         * This callback will occur when a message a page has sent has been read by the user.
         * You can subscribe to this callback by selecting
         * the message_reads field when setting up your webhook.
         */
        @Getter
        @Setter
        Delivery read;


        public EMessagingType getType() {
            return EMessagingType.getType(this);
        }

        public boolean isTextMessage() {
            return this.getType() == EMessagingType.MESSAGE;
        }

        public boolean isPostBack() {
            return this.getType() == EMessagingType.POSTBACK || this.getMessage() != null && this.getMessage().getQuick_reply() != null;
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        @Getter
        @Setter
        String mid;

        @Getter
        @Setter
        String seq;

        @Getter
        @Setter
        String text;

        @Getter
        @Setter
        QuickReply quick_reply;

    }


    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Postback {

        @Setter
        @Getter
        String payload;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Delivery {

        @Getter
        @Setter
        String watermark;

        @Getter
        @Setter
        String seq;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IDContainer {
        @Getter
        @Setter
        String id;
    }
}



