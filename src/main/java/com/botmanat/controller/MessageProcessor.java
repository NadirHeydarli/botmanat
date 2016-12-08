package com.botmanat.controller;

import com.botmanat.config.app.PayloadValues;
import com.botmanat.fb.Button;
import com.botmanat.fb.EMenuLanguage;
import com.botmanat.model.DailyCurrency;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.client.util.Lists;
import lombok.extern.java.Log;

import java.util.List;

@Log
public class MessageProcessor {

    public static void process(String senderID, String text) {
        if (Strings.isNullOrEmpty(text)) {
            return;
        }

        text = text.toLowerCase();
        SubscriberController subscriberController = new SubscriberController(senderID);

        if (text.startsWith("start ")) {
            String currencyCode = text.split(" ")[1];
            if (ifCurrencyCodeExists(currencyCode)) {
                subscriberController.subscribeToCurrency(currencyCode);
                Sender.sendTextMessage(EMessages.YOU_HAVE_BEEN_SUBSCRIBED.format(currencyCode.toUpperCase(), subscriberController.getLanguage()), senderID);
            } else {
                Sender.sendTextMessage(EMessages.CURRENCY_NOT_FOUND.getText(subscriberController.getLanguage()), senderID);
            }
        } else if (text.contains(PayloadValues.SUBSCRIBE_TO_COMMON_CURRENCIES.toLowerCase())) {
            List<String> commonCurrencies = getCommonCurrencies();
            for (String commonCurrency : commonCurrencies) {
                subscriberController.subscribeToCurrency(commonCurrency);
            }
            Sender.sendTextMessage(EMessages.FIRST_SUBSCRIPTION.getText(subscriberController.getLanguage()), senderID);
        } else if (text.contains("report")) {
            Sender.sendTextMessage(subscriberController.getDailyReport(), senderID);
        } else if (text.equals("rate_fluc")) {
            Sender.sendMessageWithQuickReplies(EMessages.RATE_FLUCTUATION_SELECTION.getText(subscriberController.getLanguage()), EQuickReplies.RATE_FLUCTUATION_SELECTION.get(), senderID);
        } else if (text.contains("stop") || text.contains("unsubscribe")) {
            subscriberController.unsubscribeFromAll();
            Sender.sendTextMessage(EMessages.UNSUBSCRIBE_ALL.getText(subscriberController.getLanguage()), senderID);
        } else if (text.equals("set_lang_az")) {
            subscriberController.setLanguage(EMenuLanguage.AZ);
            Sender.sendTextMessage(EMessages.LANG_SET.getText(EMenuLanguage.AZ), senderID);
        } else if (text.equals("set_lang_en")) {
            subscriberController.setLanguage(EMenuLanguage.EN);
            Sender.sendTextMessage(EMessages.LANG_SET.getText(EMenuLanguage.EN), senderID);
        } else if (text.equals("get_started_menu_en")) {
            subscriberController.setLanguage(EMenuLanguage.EN);
            Sender.sendMessageWithQuickReplies(EMessages.FULL_MENU.getText(EMenuLanguage.EN), EQuickReplies.FULL_MENU_EN.get(), senderID);
        } else if (text.equals("get_started_menu_az")) {
            subscriberController.setLanguage(EMenuLanguage.AZ);
            Sender.sendMessageWithQuickReplies(EMessages.FULL_MENU.getText(EMenuLanguage.AZ), EQuickReplies.FULL_MENU_AZ.get(), senderID);
        } else if (text.contains("help")) {
            Sender.sendMessageWithQuickReplies(EMessages.HELP.getText(subscriberController.getLanguage()),
                    subscriberController.getLanguage() == EMenuLanguage.AZ ? EQuickReplies.FULL_MENU_AZ.get() : EQuickReplies.FULL_MENU_EN.get(), senderID);
        } else if (text.contains(PayloadValues.OTHER.toLowerCase())) {
            Sender.sendTextMessage(EMessages.ENTER_THE_CURRENCY_YOU_WANT_TO_VIEW_FLUC_FOR.getText(subscriberController.getLanguage()), senderID);
        } else if (ifCurrencyCodeExists(text)) {
            sendCurrencyHistory(CurrencyHelper.getCurrencyHistoryString(text), text, senderID);
        } else if (text.equals("get_started_choose_lang")) {//Get Started payload
            Sender.sendMessageWithQuickReplies(EMessages.GET_STARTED_CHOOSE_LANG.getText(EMenuLanguage.EN),
                    EQuickReplies.GET_STARTED_LANG_SELECT.get(), senderID);
        } else if (text.equals("choose_lang")) {//Get Started payload
            Sender.sendMessageWithQuickReplies(EMessages.GET_STARTED_CHOOSE_LANG.getText(subscriberController.getLanguage()), EQuickReplies.LANG_SELECT.get(), senderID);
        } else if (text.equals("offer_to_subscribe")) {
            Sender.sendMessageWithQuickReplies(EMessages.OFFER_TO_SUBSCRIBE.getText(subscriberController.getLanguage()), EQuickReplies.OFFER_TO_SUBSCRIBE.get(), senderID);
        } else if (text.equals("subscribe_other")) {
            Sender.sendTextMessage(EMessages.CURRENCY_SUBSCRIBE.getText(subscriberController.getLanguage()), senderID);
        } else if (text.equals("best_rate")) {
            Sender.sendTextMessage(EMessages.BEST_RATE.getText(subscriberController.getLanguage()), senderID);
        } else {
            Sender.sendMessageWithQuickReplies(EMessages.CANNOT_UNDERSTAND.getText(subscriberController.getLanguage()),
                    subscriberController.getLanguage() == EMenuLanguage.AZ ? EQuickReplies.FULL_MENU_AZ.get() : EQuickReplies.FULL_MENU_EN.get(), senderID);
        }
    }

    private static void sendCurrencyHistory(String currencyHistoryString, String currencyCode, String senderID) {
        Button button = new Button();
        button.setTitle("View plot");
        button.setUrl("http://botmanat.appspot.com/plot.html#" + currencyCode);

        List<Button> buttons = Lists.newArrayList();
        buttons.add(button);
        Sender.sendMessageWithButtons(currencyHistoryString, buttons, senderID);
    }

    public static List<String> getCommonCurrencies() {
        List<String> sampleCurrencies = Lists.newArrayList();
        sampleCurrencies.add("usd");
        sampleCurrencies.add("eur");
        return sampleCurrencies;
    }


    private static boolean ifCurrencyCodeExists(String currencyCode) {
        DailyCurrency latestCurrency = CurrencyHelper.getPenultimativeCurrency();
        return latestCurrency.getCurrencies().containsKey(currencyCode);
    }


}
