package com.botmanat.fb;

import lombok.Getter;
import lombok.Setter;

public class Button {

    @Getter
    @Setter
    String type = "web_url";

    @Getter
    @Setter
    String url;

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String payload;

    @Getter
    @Setter
    String webview_height_ratio = "tall";


}
