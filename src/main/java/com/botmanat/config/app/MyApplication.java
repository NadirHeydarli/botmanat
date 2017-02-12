package com.botmanat.config.app;

import com.botmanat.model.DailyCurrency;
import com.botmanat.model.ExchangeRates;
import com.botmanat.model.Subscriber;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;
import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {

    public MyApplication() {
        this.property("jersey.config.server.wadl.disableWadl", true);
        packages("com.botmanat.resources");//register all resources under package
        register(JacksonFeature.class);
        initObjectify();
    }

    private void initObjectify() {
        JodaTimeTranslators.add(ObjectifyService.factory());
        ObjectifyService.register(DailyCurrency.class);
        ObjectifyService.register(Subscriber.class);
        ObjectifyService.register(ExchangeRates.class);
    }
}