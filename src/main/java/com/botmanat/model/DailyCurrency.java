package com.botmanat.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Map;

@Entity
@NoArgsConstructor
public class DailyCurrency {

    @Id
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Index
    LocalDate date;


    /**
     * Stores currency codes in lower case
     */
    @Getter
    @Setter
    @Serialize
    Map<String, Currency> currencies;

    public DailyCurrency(LocalDate date, Map<String, Currency> currenciesMap) {
        this.date = date;
        this.currencies = currenciesMap;
    }

    @AllArgsConstructor
    public static class Currency implements Serializable {

        private static final long serialVersionUID = 2963646092018624613L;

        @Getter
        @Setter
        double value;

        @Getter
        @Setter
        int nominal;

    }

}
