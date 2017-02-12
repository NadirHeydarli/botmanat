package com.botmanat.controller.parser;

public enum ExchangeDirection {
    BANK_NAME(0), USD_BUY(1), USD_SELL(2), EUR_BUY(3), EUR_SELL(4),
    GBP_BUY(5), GBP_SELL(6), RUB_BUY(7), RUB_SELL(8), TRY_BUY(9), TRY_SELL(10);

    private int indexInTable;

    ExchangeDirection(int indexInTable) {
        this.indexInTable = indexInTable;
    }

    public static ExchangeDirection getExchangeDirectionByCellIndex(int index) {
        for (ExchangeDirection exchangeDirection : ExchangeDirection.values()) {
            if (exchangeDirection.indexInTable == index) {
                return exchangeDirection;
            }
        }
        return null;
    }
}
