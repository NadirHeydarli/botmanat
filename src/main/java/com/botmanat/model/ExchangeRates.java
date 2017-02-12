package com.botmanat.model;

import com.botmanat.controller.parser.ExchangeDirection;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Serialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
public class ExchangeRates {

    @Getter
    @Setter
    @Serialize
    private Map<ExchangeDirection, List<AbstractMap.SimpleEntry<String, String>>> ratesMap;

}
