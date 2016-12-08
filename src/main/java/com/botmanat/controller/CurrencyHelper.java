package com.botmanat.controller;

import com.botmanat.model.DailyCurrency;
import com.botmanat.model.OfyHelper;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class CurrencyHelper {

    private static String ARROW_UP = " ▲ ";//" \uD83D\uDD3A ";
    private static String ARROW_DOWN = " ▽ ";//" ⬇ ";
    private static String BULLET = "  ● ";

    public static DailyCurrency getPenultimativeCurrency() {
        DailyCurrency currency = OfyHelper.get(DailyCurrency.class).order("-date").list().get(1);
        return currency == null ? new DailyCurrency() : currency;
    }


    public static String getCurrencyHistoryString(String currencyCode) {

        List<DailyCurrency> list = OfyHelper.get(DailyCurrency.class).order("-date").limit(14).list();
        int nominal = 0;
        String string = "";
        for (DailyCurrency dailyCurrency : list) {
            if (dailyCurrency.getCurrencies().containsKey(currencyCode)) {
                DailyCurrency.Currency currency = dailyCurrency.getCurrencies().get(currencyCode);
                string += String.format("%s [ %s ]", dailyCurrency.getDate().toString("dd/MM"), currency.getValue()) + "\n";
                if (nominal == 0) {
                    nominal = currency.getNominal();
                }
            }
        }

        string = "cbar.az: " + nominal + " " + currencyCode.toUpperCase() + "\n" + string;

        return string;
    }

    public static String getPrettyStringFor(DailyCurrency previousCurrency, DailyCurrency latestCurrency, List<String> currencyCodes) {

        DecimalFormat df1 = new DecimalFormat("#.####");

        String prettyString = latestCurrency.getDate().toString("dd/MM") + "\n";
        Map<String, DailyCurrency.Currency> previousCurrenciesMap = previousCurrency.getCurrencies();


        for (String currencyCode : currencyCodes) {
            if (latestCurrency.getCurrencies().containsKey(currencyCode)) {
                DailyCurrency.Currency currency = latestCurrency.getCurrencies().get(currencyCode);
                int diff = (int) (currency.getValue() * 10000) - (int) (previousCurrenciesMap.get(currencyCode).getValue() * 10000);
                String diffString = String.format((diff > 0) ? " [+%s] " : " [%s] ", df1.format(diff / 10000.0));
                prettyString += String.format("%d %s%s %s%s\n", currency.getNominal(), currencyCode.toUpperCase(), diff > 0 ? (ARROW_UP) : (diff < 0 ? ARROW_DOWN : BULLET), currency.getValue(), diffString);
            }
        }

        return prettyString;
    }
}
