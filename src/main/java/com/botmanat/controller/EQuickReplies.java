package com.botmanat.controller;

import com.botmanat.config.app.PayloadValues;
import com.botmanat.fb.QuickReply;
import com.google.common.collect.ImmutableList;

public enum EQuickReplies {
    /**
     * 20 chars text limit
     */
    RATE_FLUCTUATION_SELECTION(new QuickReply("USD"), new QuickReply("EUR"), new QuickReply("GBP"), new QuickReply("TRY"), new QuickReply("RUB"), new QuickReply("Digər / Other", PayloadValues.OTHER)),
    GET_STARTED_LANG_SELECT(new QuickReply("Azərbaycanca", "get_started_menu_az"), new QuickReply("English", "get_started_menu_en")),
    LANG_SELECT(new QuickReply("Azərbaycanca", "set_lang_az"), new QuickReply("English", "set_lang_en")),
    OFFER_TO_SUBSCRIBE(new QuickReply("USD", "start usd"), new QuickReply("EUR", "start eur"), new QuickReply("GBP", "start GBP"), new QuickReply("TRY", "start TRY"), new QuickReply("RUB", "start RUB"), new QuickReply("Digər / Other", "subscribe_other")),
    FULL_MENU_AZ(new QuickReply("Rəsmi məzənnə", "report"), new QuickReply("Məzənnə dəyişməsi", "rate_fluc"), new QuickReply("Ən sərfəli məzənnə", "best_rate"), new QuickReply("Kömək", "help")),
    FULL_MENU_EN(new QuickReply("Official Rate", "report"), new QuickReply("Rate fluctuations", "rate_fluc"), new QuickReply("Best Rate", "best_rate"), new QuickReply("Help", "help"));

    private final ImmutableList<QuickReply> quickReplies;

    public ImmutableList<QuickReply> get() {
        return quickReplies;
    }

    EQuickReplies(QuickReply... quickReplies) {
        if (quickReplies.length == 0) {
            throw new AssertionError("Some of the quick replies have not been initialized");
        }
        this.quickReplies = ImmutableList.copyOf(quickReplies);
    }
}
