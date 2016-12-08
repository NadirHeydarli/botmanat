package com.botmanat.fb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuickReply {


    @Getter
    @Setter
    String content_type;

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String payload;

    public QuickReply(String title, String payload) {
        if (title.length() > 20) {
            throw new AssertionError("Quick Reply length cannot be more than 20");
        }
        this.content_type = "text";
        this.title = title;
        this.payload = payload;
    }

    public QuickReply(String title) {
        this(title, title);
    }
}
