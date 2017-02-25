package com.botmanat.controller;

import com.botmanat.fb.EMenuLanguage;

public enum EMessages {

    CURRENCY_NOT_FOUND("I couldn't find that currency, I have all the currencies from cbar.az website and track them all", "İstədiyiniz valyuta tapılmadı. cbar.az-da olan bütün valyutalar haqqında məndə məlumat var."),
    CURRENCY_SUBSCRIBE("Type start followed by code of the currency, I have all the currencies from cbar.az", "Start və istədiyiniz valyutanın üç hərfli kodunu mənə göndərin misal:start USD cbar.az-da olan bütün valyutalar haqqında məndə məlumat var."),
    CANNOT_UNDERSTAND("I can't understand you :( \nHow about you try one of the options below?", "Mən sizi başa düşmədim :( \nAşağıdakilərdən birini seçə bilərsiniz"),
    YOU_HAVE_BEEN_SUBSCRIBED("I have subscribed you to %1$s, if you want to unsubscribe anytime just text me stop %1$s", "Sizi %1$s üçün gündəlik məzənnə xəbərinə yazdım, dayandırmaq üçün stop sözünün göndərin."),
    UNSUBSCRIBE_ALL("You have been unsubscribed from everything \nif you want to subscribe back type start followed by currency: start usd", "Bunnan sonra gündəlik məzənnə haqqında məlumat almayacaqsınız."),
    RATE_FLUCTUATION_SELECTION("Please Select the currency", "İstədiyiniz valyutanı seçin"),
    ENTER_THE_CURRENCY_YOU_WANT_TO_VIEW_FLUC_FOR("You can view fluctuations for any currency listed on cbar.az by entering its code(TRY, RUB)", "cbar.az saytında olan istənilən valyutanı daxil edin (TRY, RUB və s.)"),
    FIRST_SUBSCRIPTION("You have been subscribed to daily updates for USD and EUR from cbar.az", "Sizə gündə bir dəfə cbar.az-dan valyutalar (USD, EUR) haqqında məlumat göndəriləcək."),
    GET_STARTED_CHOOSE_LANG("Pick the language, Dili seçin"),
    BEST_RATE("Best rate functionality is underdevelopment", "Bu funksiya hələ hazır deyil"),
    FULL_MENU("What would you like to know about?", "Nə haqqında məlumat almaq istəyərdin?"),
    LANG_SET("English was set", "Azərbaycan dili seçildi"),
    HELP("List of supported commands:\n\n" +
            "start XYZ - subscribe to daily updates for XYZ currency\n\n" +
            "stop - unsubscribe from everything\n\n" +
            "report - get today's currency report(defaults to USD and EUR)\n\n" +
            "best - view the best rates offered by local banks\n\n" +
            "XYZ - get the latest 14 values for XYZ currency\n\n" +
            "help - opens this help",

            "in AZ List of supported commands:\n\n" +
                    "start XYZ - subscribe to daily updates for XYZ currency\n\n" +
                    "stop - unsubscribe from everything\n\n" +
                    "report - get today's currency report(defaults to USD and EUR)\n\n" +
                    "XYZ - get the latest 14 values for XYZ currency\n\n" +
                    "best - view the best rates offered by local banks\n\n" +
                    "help - opens this help"),
    OFFER_TO_SUBSCRIBE("Please select the currency that you would like to receive daily updates for", "Hansı valyutanın dəyişməsi barədə xəbər almaq istəyərdiniz?");

    String text;

    String text_az;

    EMessages(String text_en) {
        this.text = text_en;
        this.text_az = text_en;
    }

    EMessages(String text_en, String text_az) {
        this.text = text_en;
        this.text_az = text_az;
    }

    public String getText(EMenuLanguage menuLanguage) {
        return menuLanguage == EMenuLanguage.AZ ? text_az : text;

    }

    public String format(String parameter, EMenuLanguage language) {
        return String.format(this.getText(language), parameter);
    }
}
