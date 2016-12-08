package com.botmanat.controller;

import lombok.extern.java.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Log
public class URLHelper {
    public static URLConnection getUrlConnection(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
            return con;
        } catch (MalformedURLException e) {
            log.severe("Malformed Url was provided " + e.getMessage());
        } catch (IOException e) {
            log.severe("IOException " + e.getMessage());
        }

        return null;

    }
}
