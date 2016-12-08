package com.botmanat.fb;


public enum EMessagingType {
    MESSAGE,
    POSTBACK,
    DELIVERY,
    READ;


    public static EMessagingType getType(Entry.Messaging messaging) {
        if (messaging.getMessage() != null) {
            return MESSAGE;
        }

        if (messaging.getPostback() != null) {
            return POSTBACK;
        }

        if (messaging.getDelivery() != null) {
            return DELIVERY;
        }

        if (messaging.getRead() != null) {
            return READ;
        }

        return null;
    }
}
