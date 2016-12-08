package com.botmanat.config.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.googlecode.objectify.util.jackson.ObjectifyJacksonModule;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class JacksonFeature implements Feature {

    private static final ObjectMapper mapper =
            new ObjectMapper() {{
                registerModule(new ObjectifyJacksonModule());
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

            }};

    private static final JacksonJaxbJsonProvider provider =
            new JacksonJaxbJsonProvider() {{
                setMapper(mapper);
            }};

    @Override
    public boolean configure(FeatureContext context) {
        context.register(provider);
        return true;
    }
}