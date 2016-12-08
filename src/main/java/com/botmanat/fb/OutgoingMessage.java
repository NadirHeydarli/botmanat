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
public class OutgoingMessage {

    @Getter
    Entry.IDContainer recipient;

    @Getter
    @Setter
    MessageContainer message;

    public void setRecipient(String recipientID) {
        this.recipient = new Entry.IDContainer(recipientID);
    }

    public static class MessageContainer {
        @Getter
        @Setter
        String text;

        @Getter
        @Setter
        MessageAttachment attachment;

        @Getter
        @Setter
        List<QuickReply> quick_replies;

        public MessageContainer(String text, List<QuickReply> quickReplies) {
            this.text = text;
            this.quick_replies = quickReplies;
        }

        public MessageContainer(String text) {
            this.text = text;
        }

        public MessageContainer(MessageAttachment messageAttachment) {
            this.attachment = messageAttachment;
        }
    }

    public static class MessageAttachment {
        @Getter
        @Setter
        String type = "template";

        @Getter
        @Setter
        MessageAttachmentPayload payload;
    }

    public static class MessageAttachmentPayload {

        @Getter
        @Setter
        String template_type = "button";

        @Getter
        @Setter
        String text;

        @Getter
        @Setter
        List<Button> buttons;
    }

}
