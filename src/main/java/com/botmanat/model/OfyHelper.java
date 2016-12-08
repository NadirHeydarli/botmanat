package com.botmanat.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;

import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("Annotator")
public class OfyHelper {

    public static <T> Key<T> save(T model) {
        return ofy().save().entity(model).now();
    }


    public static <T> LoadType<T> get(Class<T> tClass) {
        return ofy().load().type(tClass);
    }

    public static <T> T getById(Class<T> tClass, String id) {
        if (id == null) return null;
        return ofy().load().type(tClass).id(id).now();
    }

}
