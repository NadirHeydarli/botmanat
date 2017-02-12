package com.botmanat.model;

import com.botmanat.controller.parser.ExchangeDirection;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@Cache
public class ExchangeRates {

    @Id
    @Getter
    String id = "SINGLETON";

    @Getter
    @Setter
    @Serialize
    private Map<ExchangeDirection, List<AbstractMap.SimpleEntry<String, String>>> ratesMap;

    public ExchangeRates(Map<ExchangeDirection, List<AbstractMap.SimpleEntry<String, String>>> ratesMap) {
        this.ratesMap = ratesMap;
    }
}
