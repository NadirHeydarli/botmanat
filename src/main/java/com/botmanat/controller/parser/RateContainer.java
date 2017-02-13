package com.botmanat.controller.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class RateContainer implements Serializable {
    private static final long serialVersionUID = -5068684089768411148L;

    @Getter
    String bankName;

    @Getter
    String rate;
}
