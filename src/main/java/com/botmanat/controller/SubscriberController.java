package com.botmanat.controller;

import com.botmanat.fb.EMenuLanguage;
import com.botmanat.model.DailyCurrency;
import com.botmanat.model.OfyHelper;
import com.botmanat.model.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class SubscriberController {

    private Subscriber subscriber;

    public SubscriberController(String senderId) {
        Subscriber subscriber = OfyHelper.getById(Subscriber.class, senderId);
        if (subscriber != null) {
            this.subscriber = subscriber;
        } else {
            this.subscriber = new Subscriber(senderId);
            this.persist();
            TaskRunner.putSubscriberNameTaskIntoQueue(senderId);
        }
    }

    public void subscribeToCurrency(String currencyCode) {
        this.subscriber.addCurrency(currencyCode);
        this.persist();
    }

    public void unsubscribeFromAll() {
        this.subscriber.setSubscribedCurrencies(new ArrayList<String>());
        this.persist();
    }

    public void setLanguage(EMenuLanguage menuLanguage) {
        this.subscriber.setLanguage(menuLanguage);
        this.persist();
    }

    public String getDailyReport() {
        DailyCurrency currentCurrency = OfyHelper.get(DailyCurrency.class).order("-date").list().get(0);
        DailyCurrency previousCurrency = CurrencyHelper.getPenultimativeCurrency();

        List<String> subscribedCurrencies = this.subscriber.getSubscribedCurrencies();
        return CurrencyHelper.getPrettyStringFor(previousCurrency, currentCurrency, subscribedCurrencies.isEmpty() ? MessageProcessor.getCommonCurrencies() : subscribedCurrencies);
    }

    public EMenuLanguage getLanguage() {
        return this.subscriber.getLanguage();
    }

    private void persist() {
        OfyHelper.save(this.subscriber);
    }

}
