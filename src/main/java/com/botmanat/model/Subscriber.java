package com.botmanat.model;

import com.botmanat.fb.EMenuLanguage;
import com.google.api.client.util.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
public class Subscriber {

    @Id
    @Getter
    String senderID;

    @Getter
    @Setter
    String firstName;

    @Getter
    @Setter
    String lastName;

    /**
     * Stored in lower case
     */
    @Getter
    @Setter
    List<String> subscribedCurrencies = Lists.newArrayList();

    @Getter
    @Setter
    EMenuLanguage language = EMenuLanguage.AZ;

    public Subscriber(String senderID) {
        this.senderID = senderID;
    }

    public void addCurrency(String validCurrencyCode) {
        if (!subscribedCurrencies.contains(validCurrencyCode)) {
            subscribedCurrencies.add(validCurrencyCode);
        }
    }

}
