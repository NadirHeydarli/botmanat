package com.botmanat.controller.parser;

import com.google.api.client.util.Maps;
import com.google.api.client.util.Strings;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

@Log
public class AznSytesParser implements RateParser {
    private static final String URL = "http://azn.sytes.net/";
    private Map<ExchangeDirection, List<AbstractMap.SimpleEntry<String, String>>> ratesMap = Maps.newHashMap();

    public boolean parseRates() {
        Document doc = getHTMLDocument();
        if (doc == null) {
            return false;
        }
        List<String> list = new ArrayList<>();
        Collections.sort(list);
        Elements tableRows = doc.select("#rates tbody tr");


        for (Element row : tableRows) {
            Elements tableCells = row.select("td");
            String bankName = tableCells.get(ExchangeDirection.BANK_NAME.ordinal()).ownText().trim();
            for (int i = 1; i < tableCells.size(); i++) {
                putIntoRatesMap(bankName, tableCells, i);
            }
        }

        return true;
    }

    private void putIntoRatesMap(String bankName, Elements tableCells, int tableCellIndex) {
        ExchangeDirection exchangeDirection = ExchangeDirection.getExchangeDirectionByCellIndex(tableCellIndex);
        if (exchangeDirection == null) {
            log.severe("Undefined exchangeDirection for cell#" + tableCellIndex);
            return;
        }

        if (!this.ratesMap.containsKey(exchangeDirection)) {
            this.ratesMap.put(exchangeDirection, new ArrayList<AbstractMap.SimpleEntry<String, String>>());
        }

        String exchangeRate = sanitize(tableCells.get(tableCellIndex).ownText());
        if (exchangeRate == null) {
            return;
        }

        AbstractMap.SimpleEntry<String, String> bankNameBankRatePair = new AbstractMap.SimpleEntry<>(bankName, exchangeRate);
        ratesMap.get(exchangeDirection).add(bankNameBankRatePair);
    }

    private String sanitize(String input) {
        String exchangeRate = input.trim();
        if (exchangeRate.equals("-") || exchangeRate.equals("?") || Strings.isNullOrEmpty(exchangeRate)) {
            return null;
        }

        if (!Character.isDigit(exchangeRate.charAt(0))) {
            return exchangeRate.substring(1, exchangeRate.length());
        }

        return exchangeRate;
    }

    private Document getHTMLDocument() {
        try {
            return Jsoup.connect(URL).get();
        } catch (IOException e) {
            log.severe("Unable to parse " + URL + e.getMessage());
            return null;
        }
    }
}
